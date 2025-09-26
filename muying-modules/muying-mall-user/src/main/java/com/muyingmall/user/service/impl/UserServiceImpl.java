package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.common.core.constant.CacheConstants;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.user.dto.LoginDTO;
import com.muyingmall.user.dto.RegisterDTO;
import com.muyingmall.user.dto.UserProfileDTO;
import com.muyingmall.user.entity.User;
import com.muyingmall.user.mapper.UserMapper;
import com.muyingmall.user.service.UserAccountService;
import com.muyingmall.user.service.UserService;
import com.muyingmall.common.util.JwtUtils;
import com.muyingmall.common.core.utils.RedisUtils;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 负责用户注册、登录、个人信息管理等核心功能
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private RedisUtils redisUtils;

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserAccountService userAccountService;

    @Value("${upload.path:E:/11/muying-web/public}")
    private String uploadPath;

    @Value("${upload.avatar.path:/avatars}")
    private String avatarPath;

    @Override
    public User register(RegisterDTO registerDTO) {
        log.info("开始用户注册，用户名: {}, 邮箱: {}", registerDTO.getUsername(), registerDTO.getEmail());

        // 检查用户名是否已存在
        if (existsByUsername(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (existsByEmail(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setStatus(1); // 默认激活状态
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户
        if (!save(user)) {
            throw new BusinessException("用户注册失败");
        }

        // 创建用户账户
        try {
            userAccountService.createUserAccount(user.getUserId());
            log.info("用户注册成功，用户ID: {}", user.getUserId());
        } catch (Exception e) {
            log.error("创建用户账户失败，用户ID: {}", user.getUserId(), e);
            // 回滚用户创建
            removeById(user.getUserId());
            throw new BusinessException("用户注册失败，请重试");
        }

        return user;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        log.info("用户登录尝试，用户名: {}", loginDTO.getUsername());

        // 根据用户名或邮箱查找用户
        User user = findByUsernameOrEmail(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("用户账户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 生成JWT令牌
        String token = JwtUtils.generateToken(user.getUserId(), user.getUsername());

        // 缓存用户信息
        if (redisUtils != null) {
            String tokenKey = CacheConstants.USER_TOKEN_KEY + token;
            redisUtils.set(tokenKey, user, CacheConstants.TOKEN_EXPIRE_TIME);
            log.debug("用户令牌已缓存: userId={}, token={}", user.getUserId(), token);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        log.info("用户登录成功，用户ID: {}", user.getUserId());
        return token;
    }

    @Override
    public User findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }

        // 先从缓存中查找
        String cacheKey = CacheConstants.USER_NAME_KEY + username;
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                log.debug("从缓存中获取用户信息: username={}", username);
                return (User) cacheResult;
            }
        }

        // 从数据库查询
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getStatus, 1)
        );

        // 如果用户存在，则缓存结果
        if (user != null && redisUtils != null) {
            redisUtils.set(cacheKey, user, CacheConstants.USER_EXPIRE_TIME);
            log.debug("将用户信息缓存到Redis: username={}", username);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }

        // 先从缓存中查找
        String cacheKey = CacheConstants.USER_EMAIL_KEY + email;
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                log.debug("从缓存中获取用户信息: email={}", email);
                return (User) cacheResult;
            }
        }

        // 从数据库查询
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, email)
                        .eq(User::getStatus, 1)
        );

        // 如果用户存在，则缓存结果
        if (user != null && redisUtils != null) {
            redisUtils.set(cacheKey, user, CacheConstants.USER_EXPIRE_TIME);
            log.debug("将用户信息缓存到Redis: email={}", email);
        }

        return user;
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return null;
        }

        // 判断是邮箱还是用户名
        if (usernameOrEmail.contains("@")) {
            return findByEmail(usernameOrEmail);
        } else {
            return findByUsername(usernameOrEmail);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }

        return userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        ) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }

        return userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, email)
        ) > 0;
    }

    @Override
    public User getUserFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        try {
            // 先从缓存中查找
            String tokenKey = CacheConstants.USER_TOKEN_KEY + token;
            if (redisUtils != null) {
                Object cacheResult = redisUtils.get(tokenKey);
                if (cacheResult != null) {
                    log.debug("从缓存中获取用户信息: token={}", token);
                    return (User) cacheResult;
                }
            }

            // 解析JWT令牌
            Claims claims = JwtUtils.parseToken(token);
            if (claims == null) {
                return null;
            }

            Integer userId = claims.get("userId", Integer.class);
            if (userId == null) {
                return null;
            }

            // 从数据库查询用户
            User user = getById(userId);
            if (user != null && user.getStatus() == 1) {
                // 重新缓存用户信息
                if (redisUtils != null) {
                    redisUtils.set(tokenKey, user, CacheConstants.TOKEN_EXPIRE_TIME);
                }
                return user;
            }

            return null;
        } catch (Exception e) {
            log.warn("解析用户令牌失败: token={}", token, e);
            return null;
        }
    }

    @Override
    public void logout(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }

        // 从缓存中移除令牌
        if (redisUtils != null) {
            String tokenKey = CacheConstants.USER_TOKEN_KEY + token;
            redisUtils.del(tokenKey);
            log.debug("用户令牌已从缓存中移除: token={}", token);
        }
    }

    @Override
    public User getUserProfile(Integer userId) {
        if (userId == null) {
            return null;
        }

        // 先从缓存中查找
        String cacheKey = CacheConstants.USER_DETAIL_KEY + userId;

        // 查询缓存
        Object cacheResult = redisUtils.get(cacheKey);
        if (cacheResult != null) {
            log.debug("从缓存中获取用户详情: userId={}", userId);
            return (User) cacheResult;
        }

        // 从数据库查询
        User user = getById(userId);
        if (user != null && user.getStatus() == 1) {
            // 清除敏感信息
            user.setPassword(null);
        }

        // 如果用户存在，缓存结果
        if (user != null) {
            redisUtils.set(cacheKey, user, CacheConstants.USER_EXPIRE_TIME);
            log.debug("将用户详情缓存到Redis: userId={}, 过期时间={}秒", userId, CacheConstants.USER_EXPIRE_TIME);
        }

        return user;
    }

    @Override
    @Transactional
    public User updateUserProfile(Integer userId, UserProfileDTO profileDTO) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新用户信息
        if (StringUtils.hasText(profileDTO.getNickname())) {
            user.setNickname(profileDTO.getNickname());
        }
        if (StringUtils.hasText(profileDTO.getPhone())) {
            user.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getGender() != null) {
            user.setGender(profileDTO.getGender());
        }
        if (profileDTO.getBirthday() != null) {
            user.setBirthday(profileDTO.getBirthday());
        }
        if (StringUtils.hasText(profileDTO.getAvatar())) {
            user.setAvatar(profileDTO.getAvatar());
        }

        user.setUpdateTime(LocalDateTime.now());

        // 保存更新
        if (!updateById(user)) {
            throw new BusinessException("更新用户信息失败");
        }

        // 清除相关缓存
        clearUserCache(user);

        log.info("用户信息更新成功: userId={}", userId);
        return user;
    }

    @Override
    public String uploadAvatar(Integer userId, MultipartFile file) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要上传的头像文件");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("只支持图片格式的头像");
        }

        // 检查文件大小（限制为2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new BusinessException("头像文件大小不能超过2MB");
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = "avatar_" + userId + "_" + System.currentTimeMillis() + extension;

            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath + avatarPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // 生成访问URL
            String avatarUrl = avatarPath + "/" + filename;

            // 更新用户头像
            User user = getById(userId);
            if (user != null) {
                user.setAvatar(avatarUrl);
                user.setUpdateTime(LocalDateTime.now());
                updateById(user);

                // 清除用户缓存
                clearUserCache(user);
            }

            log.info("用户头像上传成功: userId={}, avatarUrl={}", userId, avatarUrl);
            return avatarUrl;

        } catch (IOException e) {
            log.error("头像上传失败: userId={}", userId, e);
            throw new BusinessException("头像上传失败，请重试");
        }
    }

    /**
     * 清除用户相关缓存
     */
    private void clearUserCache(User user) {
        if (redisUtils != null && user != null) {
            // 用户详情缓存
            String detailCacheKey = CacheConstants.USER_DETAIL_KEY + user.getUserId();
            redisUtils.del(detailCacheKey);
            log.debug("清除用户详情缓存: userId={}", user.getUserId());

            // 用户名查询缓存
            if (user.getUsername() != null) {
                String usernameCacheKey = CacheConstants.USER_NAME_KEY + user.getUsername();
                redisUtils.del(usernameCacheKey);
                log.debug("清除用户名查询缓存: username={}", user.getUsername());
            }

            // 邮箱查询缓存
            if (user.getEmail() != null) {
                String emailCacheKey = CacheConstants.USER_EMAIL_KEY + user.getEmail();
                redisUtils.del(emailCacheKey);
                log.debug("清除邮箱查询缓存: email={}", user.getEmail());
            }

            // 如果是敏感信息更新（如密码、状态），清除所有相关令牌缓存
            if (user.getPassword() != null || user.getStatus() != null) {
                // 查找并清除该用户的所有令牌缓存
                Set<String> tokenKeys = redisUtils.keys(CacheConstants.USER_TOKEN_KEY + "*");
                if (tokenKeys != null && !tokenKeys.isEmpty()) {
                    // 遍历令牌，检查是否属于当前用户
                    for (String tokenKey : tokenKeys) {
                        try {
                            Object cachedUser = redisUtils.get(tokenKey);
                            if (cachedUser instanceof User) {
                                User tokenUser = (User) cachedUser;
                                String token = tokenKey.replace(CacheConstants.USER_TOKEN_KEY, "");
                                Integer tokenUserId = JwtUtils.getUserIdFromToken(token);
                                if (tokenUserId != null && tokenUserId.equals(user.getUserId())) {
                                    // 属于当前用户的令牌，清除缓存
                                    redisUtils.del(tokenKey);
                                    log.debug("清除用户令牌缓存: userId={}, token={}", user.getUserId(), token);
                                }
                            }
                        } catch (Exception e) {
                            log.warn("清除令牌缓存时发生异常: tokenKey={}", tokenKey, e);
                        }
                    }
                }
            }

            // 清除用户列表缓存
            // 适用于用户信息更新影响列表显示的情况
            // 使用模式匹配删除所有与用户列表相关的缓存
            Set<String> listKeys = redisUtils.keys(CacheConstants.USER_LIST_KEY + "*");
            if (listKeys != null && !listKeys.isEmpty()) {
                redisUtils.del(listKeys.toArray(new String[0]));
                log.debug("清除用户列表缓存，共{}个键", listKeys.size());
            }
        }
    }
}