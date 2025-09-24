package com.muyingmall.common.util;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 向后兼容类 - JwtUtils
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.security.jwt.JwtUtils} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class JwtUtils {
    
    private static final com.muyingmall.common.security.jwt.JwtUtils DELEGATE = 
        new com.muyingmall.common.security.jwt.JwtUtils();
    
    /**
     * 生成JWT令牌
     * 
     * @param username 用户名
     * @return JWT令牌
     */
    public static String generateToken(String username) {
        // 使用默认用户ID 0 和角色 USER
        return DELEGATE.generateToken(0, username, "USER");
    }
    
    /**
     * 生成JWT令牌
     * 
     * @param username 用户名
     * @param expiration 过期时间（此参数在新版本中不直接支持，使用默认过期时间）
     * @return JWT令牌
     */
    public static String generateToken(String username, Date expiration) {
        // 新版本不支持自定义过期时间，使用默认配置
        return DELEGATE.generateToken(0, username, "USER");
    }
    
    /**
     * 生成JWT令牌
     * 
     * @param claims 声明
     * @return JWT令牌
     */
    public static String generateToken(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        String role = (String) claims.getOrDefault("role", "USER");
        Integer userId = (Integer) claims.getOrDefault("userId", 0);
        return DELEGATE.generateToken(userId, username, role, claims);
    }
    
    /**
     * 从令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        return DELEGATE.getUsernameFromToken(token);
    }
    
    /**
     * 从令牌中获取过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return DELEGATE.getExpirationFromToken(token);
    }
    
    /**
     * 验证令牌
     * 
     * @param token JWT令牌
     * @param username 用户名
     * @return 是否有效
     */
    public static Boolean validateToken(String token, String username) {
        return DELEGATE.validateToken(token);
    }
    
    /**
     * 检查令牌是否过期
     * 
     * @param token JWT令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        return DELEGATE.isTokenExpired(token);
    }
}