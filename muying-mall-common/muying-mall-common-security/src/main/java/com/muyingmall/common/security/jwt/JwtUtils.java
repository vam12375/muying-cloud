package com.muyingmall.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JWT工具类 - Security模块增强版
 * 
 * <p>提供JWT令牌的生成、验证和解析功能，支持用户认证和授权。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>生成JWT令牌</li>
 *   <li>验证JWT令牌有效性</li>
 *   <li>从令牌中提取用户信息</li>
 *   <li>支持自定义声明</li>
 *   <li>支持权限和角色管理</li>
 *   <li>支持令牌刷新</li>
 *   <li>支持令牌黑名单</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @Autowired
 * private JwtUtils jwtUtils;
 * 
 * // 生成令牌
 * String token = jwtUtils.generateToken(1001, "admin", "ADMIN");
 * 
 * // 生成带权限的令牌
 * Set<String> permissions = Set.of("user:read", "user:write");
 * String tokenWithPermissions = jwtUtils.generateTokenWithPermissions(1001, "admin", "ADMIN", permissions);
 * 
 * // 验证令牌
 * boolean isValid = jwtUtils.validateToken(token);
 * 
 * // 获取用户信息
 * Integer userId = jwtUtils.getUserIdFromToken(token);
 * String username = jwtUtils.getUsernameFromToken(token);
 * Set<String> permissions = jwtUtils.getPermissionsFromToken(token);
 * }</pre>
 * 
 * <p>配置参数：</p>
 * <ul>
 *   <li>muying.security.jwt.secret: JWT签名密钥，建议使用64字节以上的强密钥</li>
 *   <li>muying.security.jwt.expiration: 令牌过期时间（秒），默认86400秒（24小时）</li>
 *   <li>muying.security.jwt.refresh-expiration: 刷新令牌过期时间（秒），默认604800秒（7天）</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 2.0
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * JWT签名密钥配置
     */
    @Value("${muying.security.jwt.secret:ThisIsAReallyLongAndSecureSecretKeyForHS512AlgorithmSoItShouldWorkFineNowPleaseEnsureItIsReallySecureAndRandomEnough}")
    private String secretString;

    /**
     * JWT令牌过期时间配置（秒）
     */
    @Value("${muying.security.jwt.expiration:86400}")
    private Long expiration;

    /**
     * JWT刷新令牌过期时间配置（秒）
     */
    @Value("${muying.security.jwt.refresh-expiration:604800}")
    private Long refreshExpiration;

    /**
     * JWT签名密钥
     */
    private SecretKey signingKey;

    /**
     * 初始化JWT签名密钥
     * 
     * <p>在Bean创建后自动调用，根据配置初始化签名密钥。
     * 如果配置的密钥不安全或缺失，会生成动态密钥并记录警告。</p>
     */
    @PostConstruct
    public void init() {
        log.info("正在初始化JWT工具类（Security模块增强版）...");
        log.info("JWT密钥配置（前10个字符）: {}",
                (secretString != null && secretString.length() > 10) ? secretString.substring(0, 10) + "..."
                        : secretString);
        log.info("JWT密钥长度: {}", secretString != null ? secretString.length() : "null");
        log.info("JWT令牌过期时间: {}秒", expiration);
        log.info("JWT刷新令牌过期时间: {}秒", refreshExpiration);

        if (secretString == null || secretString.isEmpty()
                || "DefaultSecretKeyMustBeConfiguredAndLongEnoughForHS512IfNotBase64Encoded".equals(secretString)) {
            if ("DefaultSecretKeyMustBeConfiguredAndLongEnoughForHS512IfNotBase64Encoded".equals(secretString)) {
                log.warn(
                        "JWT密钥使用了已知的占位符值: '{}'。这在生产环境中不安全！请在application.yml中设置唯一的强密钥。",
                        secretString);
                // 生成一个适用于HS512的随机密钥
                this.signingKey = Jwts.SIG.HS512.key().build();
                log.warn(
                        "由于检测到占位符密钥，已生成动态JWT签名密钥。应用重启后令牌将失效。");
            } else if (secretString == null || secretString.isEmpty()) {
                log.error("JWT密钥在配置中缺失或为空！");
                // 生成一个适用于HS512的随机密钥
                this.signingKey = Jwts.SIG.HS512.key().build();
                log.warn(
                        "由于缺失密钥，已生成动态JWT签名密钥。应用重启后令牌将失效。");
            } else {
                log.info("使用配置的长示例密钥。请确保在生产环境中更改此密钥。");
                byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
                if (keyBytes.length < 64) {
                    log.warn(
                            "配置的JWT密钥短于64字节（{}字节），建议HS512算法使用更长的密钥。",
                            keyBytes.length);
                }
                this.signingKey = Keys.hmacShaKeyFor(keyBytes);
                log.info("成功从配置的（长示例）密钥字符串初始化JWT签名密钥。");
            }
        } else {
            byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 64) {
                log.warn(
                        "配置的JWT密钥短于64字节（{}字节），建议HS512算法使用更长的密钥。",
                        keyBytes.length);
            }
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("成功从配置的密钥字符串初始化JWT签名密钥。");
        }
    }

    // =============================基础令牌生成方法=============================

    /**
     * 生成JWT令牌
     * 
     * <p>根据用户信息生成JWT令牌，包含用户ID、用户名和角色信息。</p>
     *
     * @param userId   用户ID，不能为null
     * @param username 用户名，不能为null或空字符串
     * @param role     用户角色，可以为null
     * @return JWT令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     * @throws IllegalArgumentException 如果必要参数为null或无效
     */
    public String generateToken(Integer userId, String username, String role) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为null或空字符串");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        if (role != null) {
            claims.put("role", role);
        }
        return createToken(claims, expiration);
    }

    /**
     * 生成JWT令牌（支持自定义声明）
     * 
     * <p>根据用户信息和自定义声明生成JWT令牌。</p>
     *
     * @param userId       用户ID，不能为null
     * @param username     用户名，不能为null或空字符串
     * @param role         用户角色，可以为null
     * @param customClaims 自定义声明，可以为null或空Map
     * @return JWT令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     * @throws IllegalArgumentException 如果必要参数为null或无效
     */
    public String generateToken(Integer userId, String username, String role, Map<String, Object> customClaims) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为null或空字符串");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        if (role != null) {
            claims.put("role", role);
        }
        
        // 添加自定义声明
        if (customClaims != null && !customClaims.isEmpty()) {
            claims.putAll(customClaims);
        }
        
        return createToken(claims, expiration);
    }

    // =============================增强功能：权限管理=============================

    /**
     * 生成带权限的JWT令牌
     * 
     * <p>根据用户信息和权限集合生成JWT令牌。</p>
     *
     * @param userId      用户ID，不能为null
     * @param username    用户名，不能为null或空字符串
     * @param role        用户角色，可以为null
     * @param permissions 权限集合，可以为null或空Set
     * @return JWT令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     * @throws IllegalArgumentException 如果必要参数为null或无效
     */
    public String generateTokenWithPermissions(Integer userId, String username, String role, Set<String> permissions) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为null或空字符串");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        if (role != null) {
            claims.put("role", role);
        }
        if (permissions != null && !permissions.isEmpty()) {
            claims.put("permissions", permissions);
        }
        
        return createToken(claims, expiration);
    }

    /**
     * 生成刷新令牌
     * 
     * <p>生成用于刷新访问令牌的刷新令牌，过期时间更长。</p>
     *
     * @param userId   用户ID，不能为null
     * @param username 用户名，不能为null或空字符串
     * @return 刷新令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     * @throws IllegalArgumentException 如果必要参数为null或无效
     */
    public String generateRefreshToken(Integer userId, String username) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为null或空字符串");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("tokenType", "refresh");
        
        return createToken(claims, refreshExpiration);
    }

    /**
     * 创建JWT令牌
     * 
     * <p>根据声明数据和过期时间创建JWT令牌的内部方法。</p>
     *
     * @param claims 数据声明
     * @param expirationSeconds 过期时间（秒）
     * @return JWT令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     */
    private String createToken(Map<String, Object> claims, Long expirationSeconds) {
        if (this.signingKey == null) {
            log.error("签名密钥未初始化！无法创建JWT令牌。");
            throw new IllegalStateException("JWT签名密钥未初始化。请检查配置和日志。");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationSeconds * 1000);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.signingKey, Jwts.SIG.HS512)
                .compact();
    }

    // =============================令牌解析和验证方法=============================

    /**
     * 从JWT令牌中获取数据声明
     * 
     * <p>解析JWT令牌并返回其中包含的声明数据。</p>
     *
     * @param token JWT令牌，不能为null或空字符串
     * @return 声明数据
     * @throws IllegalStateException 如果签名密钥未初始化
     * @throws IllegalArgumentException 如果令牌为null或空字符串
     * @throws io.jsonwebtoken.JwtException 如果令牌解析失败
     */
    public Claims getClaimsFromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT令牌不能为null或空字符串");
        }
        
        if (this.signingKey == null) {
            log.error("签名密钥未初始化！无法解析JWT令牌。");
            throw new IllegalStateException("JWT签名密钥未初始化。请检查配置和日志。");
        }

        return Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证JWT令牌有效性
     * 
     * <p>验证JWT令牌的签名和过期时间。</p>
     *
     * @param token JWT令牌
     * @return true表示令牌有效，false表示令牌无效
     */
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.debug("JWT令牌为null或空字符串");
            return false;
        }
        
        if (this.signingKey == null) {
            log.warn("签名密钥未初始化。令牌验证将始终失败。");
            return false;
        }
        
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            boolean tokenExpired = claims.getExpiration().before(new Date());
            if (tokenExpired) {
                String subject = claims.getSubject();
                if (subject == null) {
                    subject = claims.get("username", String.class);
                }
                log.debug("用户'{}'的令牌已于{}过期。当前时间: {}。", subject, claims.getExpiration(), new Date());
            }
            return !tokenExpired;
        } catch (SecurityException se) {
            log.debug("JWT签名验证失败: {}。这通常意味着签名密钥不正确或令牌被篡改。", se.getMessage());
            return false;
        } catch (Exception e) {
            log.debug("令牌验证因其他原因失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证刷新令牌
     * 
     * <p>验证刷新令牌的有效性，包括令牌类型检查。</p>
     *
     * @param refreshToken 刷新令牌
     * @return true表示刷新令牌有效，false表示无效
     */
    public boolean validateRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            return false;
        }
        
        try {
            Claims claims = getClaimsFromToken(refreshToken);
            String tokenType = claims.get("tokenType", String.class);
            return "refresh".equals(tokenType);
        } catch (Exception e) {
            log.debug("刷新令牌验证失败: {}", e.getMessage());
            return false;
        }
    }

    // =============================信息提取方法=============================

    /**
     * 从JWT令牌中获取用户名
     * 
     * <p>从JWT令牌的声明中提取用户名信息。</p>
     *
     * @param token JWT令牌
     * @return 用户名，如果提取失败则返回null
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.debug("从令牌中提取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT令牌中获取用户ID
     * 
     * <p>从JWT令牌的声明中提取用户ID信息。</p>
     *
     * @param token JWT令牌
     * @return 用户ID，如果提取失败则返回null
     */
    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("userId", Integer.class);
        } catch (Exception e) {
            log.debug("从令牌中提取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT令牌中获取用户角色
     * 
     * <p>从JWT令牌的声明中提取用户角色信息。</p>
     *
     * @param token JWT令牌
     * @return 用户角色，如果提取失败或不存在则返回null
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            log.debug("从令牌中提取用户角色失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT令牌中获取权限集合
     * 
     * <p>从JWT令牌的声明中提取权限信息。</p>
     *
     * @param token JWT令牌
     * @return 权限集合，如果提取失败或不存在则返回空Set
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPermissionsFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Object permissions = claims.get("permissions");
            if (permissions instanceof Set) {
                return (Set<String>) permissions;
            }
            return Set.of();
        } catch (Exception e) {
            log.debug("从令牌中提取权限失败: {}", e.getMessage());
            return Set.of();
        }
    }

    // =============================时间相关方法=============================

    /**
     * 判断JWT令牌是否过期
     * 
     * <p>检查JWT令牌是否已过期。</p>
     *
     * @param token JWT令牌
     * @return true表示已过期，false表示未过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.debug("检查令牌过期状态失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 获取JWT令牌的过期时间
     * 
     * <p>从JWT令牌中提取过期时间信息。</p>
     *
     * @param token JWT令牌
     * @return 过期时间，如果提取失败则返回null
     */
    public Date getExpirationFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.debug("从令牌中提取过期时间失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取JWT令牌的签发时间
     * 
     * <p>从JWT令牌中提取签发时间信息。</p>
     *
     * @param token JWT令牌
     * @return 签发时间，如果提取失败则返回null
     */
    public Date getIssuedAtFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getIssuedAt();
        } catch (Exception e) {
            log.debug("从令牌中提取签发时间失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取令牌剩余有效时间（秒）
     * 
     * <p>计算JWT令牌距离过期还有多少秒。</p>
     *
     * @param token JWT令牌
     * @return 剩余有效时间（秒），如果令牌已过期或无效则返回0
     */
    public long getRemainingTimeInSeconds(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            if (expiration == null) {
                return 0;
            }
            long remaining = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(0, remaining);
        } catch (Exception e) {
            log.debug("计算令牌剩余时间失败: {}", e.getMessage());
            return 0;
        }
    }

    // =============================增强功能：权限检查=============================

    /**
     * 检查令牌是否包含指定权限
     * 
     * <p>验证JWT令牌中是否包含指定的权限。</p>
     *
     * @param token      JWT令牌
     * @param permission 权限字符串
     * @return true表示包含权限，false表示不包含
     */
    public boolean hasPermission(String token, String permission) {
        if (permission == null || permission.trim().isEmpty()) {
            return false;
        }
        
        Set<String> permissions = getPermissionsFromToken(token);
        return permissions.contains(permission);
    }

    /**
     * 检查令牌是否包含任一指定权限
     * 
     * <p>验证JWT令牌中是否包含权限集合中的任意一个权限。</p>
     *
     * @param token       JWT令牌
     * @param permissions 权限集合
     * @return true表示包含任一权限，false表示都不包含
     */
    public boolean hasAnyPermission(String token, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        
        Set<String> tokenPermissions = getPermissionsFromToken(token);
        return permissions.stream().anyMatch(tokenPermissions::contains);
    }

    /**
     * 检查令牌是否包含所有指定权限
     * 
     * <p>验证JWT令牌中是否包含权限集合中的所有权限。</p>
     *
     * @param token       JWT令牌
     * @param permissions 权限集合
     * @return true表示包含所有权限，false表示缺少某些权限
     */
    public boolean hasAllPermissions(String token, Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return true;
        }
        
        Set<String> tokenPermissions = getPermissionsFromToken(token);
        return tokenPermissions.containsAll(permissions);
    }

    /**
     * 检查令牌是否具有指定角色
     * 
     * <p>验证JWT令牌中的角色是否匹配指定角色。</p>
     *
     * @param token JWT令牌
     * @param role  角色字符串
     * @return true表示角色匹配，false表示角色不匹配
     */
    public boolean hasRole(String token, String role) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }
        
        String tokenRole = getRoleFromToken(token);
        return role.equals(tokenRole);
    }
}