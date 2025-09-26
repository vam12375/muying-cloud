package com.muyingmall.common.core.utils;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类兼容层
 * 
 * <p>为了保持向后兼容性而提供的兼容类。</p>
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.security.jwt.JwtUtils}，
 *             请使用Security模块中的增强版本。此兼容类将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0 (兼容层)
 */
@Deprecated(since = "1.0", forRemoval = true)
public class JwtUtilsCompat {

    private final JwtUtils delegate;

    public JwtUtilsCompat() {
        this.delegate = new JwtUtils();
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public String generateToken(Integer userId, String username, String role) {
        return delegate.generateToken(userId, username, role);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public String generateToken(Integer userId, String username, String role, Map<String, Object> customClaims) {
        return delegate.generateToken(userId, username, role, customClaims);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Claims getClaimsFromToken(String token) {
        return delegate.getClaimsFromToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public boolean validateToken(String token) {
        return delegate.validateToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public String getUsernameFromToken(String token) {
        return delegate.getUsernameFromToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Integer getUserIdFromToken(String token) {
        return delegate.getUserIdFromToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public String getRoleFromToken(String token) {
        return delegate.getRoleFromToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public boolean isTokenExpired(String token) {
        return delegate.isTokenExpired(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Date getExpirationFromToken(String token) {
        return delegate.getExpirationFromToken(token);
    }

    /**
     * @deprecated 请使用
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Date getIssuedAtFromToken(String token) {
        return delegate.getIssuedAtFromToken(token);
    }
}