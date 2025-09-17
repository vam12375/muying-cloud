package com.muyingmall.common.util;

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

/**
 * JWT工具类 - 使用JJWT 0.12.x API
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret:ThisIsAReallyLongAndSecureSecretKeyForHS512AlgorithmSoItShouldWorkFineNowPleaseEnsureItIsReallySecureAndRandomEnough}")
    private String secretString;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        log.info("Initializing JwtUtils...");
        log.info("JWT Secret from config (first 10 chars): {}",
                (secretString != null && secretString.length() > 10) ? secretString.substring(0, 10) + "..."
                        : secretString);
        log.info("JWT Secret length from config: {}", secretString != null ? secretString.length() : "null");

        if (secretString == null || secretString.isEmpty()
                || "DefaultSecretKeyMustBeConfiguredAndLongEnoughForHS512IfNotBase64Encoded".equals(secretString)) {
            if ("DefaultSecretKeyMustBeConfiguredAndLongEnoughForHS512IfNotBase64Encoded".equals(secretString)) {
                log.warn(
                        "JWT Secret is using a known placeholder value: '{}'. This is NOT secure for production! Please set a unique, strong secret in application.yml.",
                        secretString);
                // 生成一个适用于HS512的随机密钥
                this.signingKey = Jwts.SIG.HS512.key().build();
                log.warn(
                        "Generated a dynamic JWT signing key because a placeholder secret was detected. Tokens will invalidate on app restart.");
            } else if (secretString == null || secretString.isEmpty()) {
                log.error("JWT Secret is missing or empty in configuration!");
                // 生成一个适用于HS512的随机密钥
                this.signingKey = Jwts.SIG.HS512.key().build();
                log.warn(
                        "Generated a dynamic JWT signing key due to missing secret. Tokens will invalidate on app restart.");
            } else {
                log.info("Using the configured long example secret key. Ensure this is changed for production.");
                byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
                if (keyBytes.length < 64) {
                    log.warn(
                            "Configured JWT Secret is shorter than 64 bytes ({} bytes), which is recommended for HS512. Consider a longer secret.",
                            keyBytes.length);
                }
                this.signingKey = Keys.hmacShaKeyFor(keyBytes);
                log.info("Successfully initialized JWT signing key from configured (long example) secret string.");
            }
        } else {
            byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 64) {
                log.warn(
                        "Configured JWT Secret is shorter than 64 bytes ({} bytes), which is recommended for HS512. Consider a longer secret.",
                        keyBytes.length);
            }
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("Successfully initialized JWT signing key from configured secret string.");
        }
    }

    /**
     * 生成令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     角色
     * @return JWT令牌
     */
    public String generateToken(Integer userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return createToken(claims);
    }

    /**
     * 创建令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        if (this.signingKey == null) {
            log.error("Signing key is not initialized! JWT cannot be created.");
            throw new IllegalStateException("JWT signing key is not initialized. Check configuration and logs.");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.signingKey, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims getClaimsFromToken(String token) {
        if (this.signingKey == null) {
            log.error("Signing key is not initialized! JWT cannot be parsed.");
            throw new IllegalStateException("JWT signing key is not initialized. Check configuration and logs.");
        }

        return Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        if (this.signingKey == null) {
            log.warn("Signing key is not initialized. Token validation will always fail.");
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
                if (subject == null)
                    subject = claims.get("username", String.class);
                log.warn("Token for user '{}' has expired at {}. Current time: {}.", subject, claims.getExpiration(),
                        new Date());
            }
            return !tokenExpired;
        } catch (SecurityException se) {
            log.warn(
                    "JWT signature validation failed: {}. This usually means the signing key is incorrect or the token was tampered with.",
                    se.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Token validation failed for other reasons: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("userId", Integer.class);
        } catch (Exception e) {
            log.warn("Failed to extract userId from token: {}", e.getMessage());
            return null;
        }
    }
}