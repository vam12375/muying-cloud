package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyingmall.common.core.constant.CacheConstants;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.user.entity.User;
import com.muyingmall.user.entity.UserMessage;
import com.muyingmall.common.core.enums.MessageType;
import com.muyingmall.user.mapper.UserMapper;
import com.muyingmall.user.mapper.UserMessageMapper;
import com.muyingmall.user.service.UserMessageService;
import com.muyingmall.common.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户消息服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    private final UserMessageMapper userMessageMapper;
    private final UserMapper userMapper;

    @Autowired(required = false)
    private RedisUtils redisUtils;

    @Override
    public UserMessage sendMessage(Integer userId, String title, String content, MessageType type) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        if (!StringUtils.hasText(title)) {
            throw new BusinessException("消息标题不能为空");
        }

        if (!StringUtils.hasText(content)) {
            throw new BusinessException("消息内容不能为空");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 创建消息
        UserMessage message = new UserMessage();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setType(type != null ? type.getCode() : MessageType.SYSTEM.getCode());
        message.setIsRead(0); // 未读
        message.setCreateTime(LocalDateTime.now());

        // 保存消息
        if (!save(message)) {
            throw new BusinessException("发送消息失败");
        }

        // 清除相关缓存
        clearMessageCache(userId);

        log.info("消息发送成功: userId={}, messageId={}, title={}", userId, message.getMessageId(), title);
        return message;
    }

    @Override
    public IPage<UserMessage> getUserMessages(Integer userId, Integer type, Integer isRead, Integer page, Integer size) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 构建缓存键
        String cacheKey = String.format("%s%d:%s:%s:%d:%d", 
                CacheConstants.USER_MESSAGES_KEY, userId, 
                type != null ? type : "all", 
                isRead != null ? isRead : "all", 
                page != null ? page : 1, 
                size != null ? size : 10);

        // 先从缓存中查找
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                log.debug("从缓存中获取用户消息列表: userId={}, type={}, isRead={}, page={}, size={}",
                        userId, type, isRead, page, size);
                return (IPage<UserMessage>) cacheResult;
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<UserMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserMessage::getUserId, userId);
        
        if (type != null) {
            queryWrapper.eq(UserMessage::getType, type);
        }
        
        if (isRead != null) {
            queryWrapper.eq(UserMessage::getIsRead, isRead);
        }
        
        queryWrapper.orderByDesc(UserMessage::getCreateTime);

        // 分页查询
        Page<UserMessage> pageParam = new Page<>(
                page != null ? page : 1, 
                size != null ? size : 10
        );
        
        IPage<UserMessage> result = userMessageMapper.selectPage(pageParam, queryWrapper);

        // 缓存结果
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty() && redisUtils != null) {
            redisUtils.set(cacheKey, result, CacheConstants.MESSAGE_LIST_EXPIRE_TIME);
            log.debug("将用户消息列表缓存到Redis: userId={}, type={}, isRead={}, 缓存键={}, 过期时间={}秒",
                    userId, type, isRead, cacheKey, CacheConstants.MESSAGE_LIST_EXPIRE_TIME);
        }

        return result;
    }

    @Override
    public Long getUnreadMessageCount(Integer userId) {
        if (userId == null) {
            return 0L;
        }

        // 先从缓存中查找
        String cacheKey = CacheConstants.USER_UNREAD_COUNT_KEY + userId;
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                log.debug("从缓存中获取用户未读消息数: userId={}", userId);
                return (Long) cacheResult;
            }
        }

        // 从数据库查询
        Long count = userMessageMapper.selectCount(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getUserId, userId)
                        .eq(UserMessage::getIsRead, 0)
        );

        if (count == null) {
            count = 0L;
        }

        // 缓存结果
        if (redisUtils != null) {
            redisUtils.set(cacheKey, count, CacheConstants.MESSAGE_COUNT_EXPIRE_TIME);
            log.debug("将用户未读消息数缓存到Redis: userId={}, count={}, 过期时间={}秒",
                    userId, count, CacheConstants.MESSAGE_COUNT_EXPIRE_TIME);
        }

        return count;
    }

    @Override
    public Map<String, Long> getUnreadMessageCountByType(Integer userId) {
        if (userId == null) {
            return new HashMap<>();
        }

        // 先从缓存中查找
        String cacheKey = CacheConstants.USER_UNREAD_TYPE_KEY + userId;
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                log.debug("从缓存中获取用户未读消息类型统计: userId={}", userId);
                return (Map<String, Long>) cacheResult;
            }
        }

        // 从数据库查询各类型未读消息数
        Map<String, Long> result = new HashMap<>();
        
        // 查询所有未读消息
        List<UserMessage> unreadMessages = userMessageMapper.selectList(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getUserId, userId)
                        .eq(UserMessage::getIsRead, 0)
                        .select(UserMessage::getType)
        );

        // 按类型统计
        Map<Integer, Long> typeCountMap = unreadMessages.stream()
                .collect(Collectors.groupingBy(UserMessage::getType, Collectors.counting()));

        // 转换为字符串键
        for (MessageType messageType : MessageType.values()) {
            Long count = typeCountMap.getOrDefault(messageType.getCode(), 0L);
            result.put(messageType.name().toLowerCase(), count);
        }

        // 缓存结果
        if (redisUtils != null) {
            redisUtils.set(cacheKey, result, CacheConstants.MESSAGE_COUNT_EXPIRE_TIME);
            log.debug("将用户未读消息类型统计缓存到Redis: userId={}, 过期时间={}秒",
                    userId, CacheConstants.MESSAGE_COUNT_EXPIRE_TIME);
        }

        return result;
    }

    @Override
    @Transactional
    public boolean markAsRead(Integer userId, Integer messageId) {
        if (userId == null || messageId == null) {
            throw new BusinessException("用户ID和消息ID不能为空");
        }

        // 查询消息
        UserMessage message = userMessageMapper.selectOne(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getMessageId, messageId)
                        .eq(UserMessage::getUserId, userId)
        );

        if (message == null) {
            throw new BusinessException("消息不存在或无权限访问");
        }

        if (message.getIsRead() == 1) {
            return true; // 已经是已读状态
        }

        // 更新为已读
        message.setIsRead(1);
        message.setReadTime(LocalDateTime.now());
        
        boolean success = updateById(message);
        
        if (success) {
            // 清除相关缓存
            clearMessageCache(userId);
            log.info("消息标记为已读成功: userId={}, messageId={}", userId, messageId);
        }

        return success;
    }

    @Override
    @Transactional
    public boolean markAllAsRead(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 批量更新未读消息为已读
        LambdaUpdateWrapper<UserMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserMessage::getUserId, userId)
                    .eq(UserMessage::getIsRead, 0)
                    .set(UserMessage::getIsRead, 1)
                    .set(UserMessage::getReadTime, LocalDateTime.now());

        boolean success = update(updateWrapper);
        
        if (success) {
            // 清除相关缓存
            clearMessageCache(userId);
            log.info("用户所有消息标记为已读成功: userId={}", userId);
        }

        return success;
    }

    @Override
    @Transactional
    public boolean deleteMessage(Integer userId, Integer messageId) {
        if (userId == null || messageId == null) {
            throw new BusinessException("用户ID和消息ID不能为空");
        }

        // 查询消息
        UserMessage message = userMessageMapper.selectOne(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getMessageId, messageId)
                        .eq(UserMessage::getUserId, userId)
        );

        if (message == null) {
            throw new BusinessException("消息不存在或无权限访问");
        }

        boolean success = removeById(messageId);
        
        if (success) {
            // 清除相关缓存
            clearMessageCache(userId);
            log.info("消息删除成功: userId={}, messageId={}", userId, messageId);
        }

        return success;
    }

    @Override
    @Transactional
    public boolean batchDeleteMessages(Integer userId, List<Integer> messageIds) {
        if (userId == null || messageIds == null || messageIds.isEmpty()) {
            throw new BusinessException("用户ID和消息ID列表不能为空");
        }

        // 验证所有消息都属于该用户
        Long count = userMessageMapper.selectCount(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getUserId, userId)
                        .in(UserMessage::getMessageId, messageIds)
        );

        if (count != messageIds.size()) {
            throw new BusinessException("部分消息不存在或无权限访问");
        }

        // 批量删除
        boolean success = removeByIds(messageIds);
        
        if (success) {
            // 清除相关缓存
            clearMessageCache(userId);
            log.info("批量删除消息成功: userId={}, messageIds={}", userId, messageIds);
        }

        return success;
    }

    @Override
    public UserMessage getMessageDetail(Integer userId, Integer messageId) {
        if (userId == null || messageId == null) {
            throw new BusinessException("用户ID和消息ID不能为空");
        }

        // 查询消息
        UserMessage message = userMessageMapper.selectOne(
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getMessageId, messageId)
                        .eq(UserMessage::getUserId, userId)
        );

        if (message == null) {
            throw new BusinessException("消息不存在或无权限访问");
        }

        // 如果是未读消息，自动标记为已读
        if (message.getIsRead() == 0) {
            markAsRead(userId, messageId);
            message.setIsRead(1);
            message.setReadTime(LocalDateTime.now());
        }

        return message;
    }

    @Override
    @Transactional
    public boolean sendSystemMessage(String title, String content, List<Integer> userIds) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new BusinessException("消息标题和内容不能为空");
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException("接收用户列表不能为空");
        }

        // 批量创建消息
        List<UserMessage> messages = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Integer userId : userIds) {
            UserMessage message = new UserMessage();
            message.setUserId(userId);
            message.setTitle(title);
            message.setContent(content);
            message.setType(MessageType.SYSTEM.getCode());
            message.setIsRead(0);
            message.setCreateTime(now);
            messages.add(message);
        }

        // 批量保存
        boolean success = saveBatch(messages);
        
        if (success) {
            // 清除所有相关用户的缓存
            for (Integer userId : userIds) {
                clearMessageCache(userId);
            }
            log.info("系统消息批量发送成功: title={}, userCount={}", title, userIds.size());
        }

        return success;
    }

    /**
     * 清除用户消息相关缓存
     */
    private void clearMessageCache(Integer userId) {
        if (redisUtils != null && userId != null) {
            try {
                // 清除未读消息计数缓存
                String countCacheKey = CacheConstants.USER_UNREAD_COUNT_KEY + userId;
                redisUtils.del(countCacheKey);

                // 清除未读消息类型统计缓存
                String typeCacheKey = CacheConstants.USER_UNREAD_TYPE_KEY + userId;
                redisUtils.del(typeCacheKey);

                // 清除消息列表缓存
                // 使用模式匹配删除所有与该用户相关的消息列表缓存
                Set<String> messageListKeys = redisUtils.keys(CacheConstants.USER_MESSAGES_KEY + userId + ":*");
                if (messageListKeys != null && !messageListKeys.isEmpty()) {
                    redisUtils.del(messageListKeys.toArray(new String[0]));
                    log.debug("已清除用户消息列表缓存，共{}个键: userId={}", messageListKeys.size(), userId);
                }

            } catch (Exception e) {
                log.warn("清除用户消息缓存失败: userId={}", userId, e);
            }
        }
    }
}