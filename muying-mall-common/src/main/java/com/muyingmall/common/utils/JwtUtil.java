package com.muyingmall.common.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类 - 兼容性包装器
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.core.utils.JwtUtils}，请使用新的位置。
 * 此类仅为向后兼容而保留，将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 */
@Deprecated
@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private com.muyingmall.common.core.utils.JwtUtils coreJwtUtils;

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#generateToken(Integer, String, String, Map)}
     */
    @Deprecated
    public String generateToken(Integer userId, String username, Map<String, Object> claims) {
        return coreJwtUtils.generateToken(userId, username, null, claims);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#validateToken(String)}
     */
    @Deprecated
    public boolean validateToken(String token) {
        return coreJwtUtils.validateToken(token);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#getUserIdFromToken(String)}
     */
    @Deprecated
    public Integer getUserIdFromToken(String token) {
        return coreJwtUtils.getUserIdFromToken(token);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#getUsernameFromToken(String)}
     */
    @Deprecated
    public String getUsernameFromToken(String token) {
        return coreJwtUtils.getUsernameFromToken(token);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#getClaimsFromToken(String)}
     */
    @Deprecated
    private Claims getClaimsFromToken(String token) {
        return coreJwtUtils.getClaimsFromToken(token);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.JwtUtils#isTokenExpired(String)}
     */
    @Deprecated
    public boolean isTokenExpired(String token) {
        return coreJwtUtils.isTokenExpired(token);
    }
}