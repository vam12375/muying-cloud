package com.muyingmall.common.core.utils;

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
 * JWT工具类
 * 
 * <p>提供JWT令牌的生成、验证和解析功能，支持用户认证和授权。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>生成JWT令牌</li>
 *   <li>验证JWT令牌有效性</li>
 *   <li>从令牌中提取用户信息</li>
 *   <li>支持自定义声明</li>
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
 * // 验证令牌
 * boolean isValid = jwtUtils.validateToken(token);
 * 
 * // 获取用户信息
 * Integer userId = jwtUtils.getUserIdFromToken(token);
 * String username = jwtUtils.getUsernameFromToken(token);
 * }</pre>
 * 
 * <p>配置参数：</p>
 * <ul>
 *   <li>jwt.secret: JWT签名密钥，建议使用64字节以上的强密钥</li>
 *   <li>jwt.expiration: 令牌过期时间（秒），默认86400秒（24小时）</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * JWT签名密钥配置
     */
    @Value("${jwt.secret:ThisIsAReallyLongAndSecureSecretKeyForHS512AlgorithmSoItShouldWorkFineNowPleaseEnsureItIsReallySecureAndRandomEnough}")
    private String secretString;

    /**
     * JWT令牌过期时间配置（秒）
     */
    @Value("${jwt.expiration:86400}")
    private Long expiration;

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
        log.info("正在初始化JWT工具类...");
        log.info("JWT密钥配置（前10个字符）: {}",
                (secretString != null && secretString.length() > 10) ? secretString.substring(0, 10) + "..."
                        : secretString);
        log.info("JWT密钥长度: {}", secretString != null ? secretString.length() : "null");

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
        return createToken(claims);
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
        
        return createToken(claims);
    }

    /**
     * 创建JWT令牌
     * 
     * <p>根据声明数据创建JWT令牌的内部方法。</p>
     *
     * @param claims 数据声明
     * @return JWT令牌字符串
     * @throws IllegalStateException 如果签名密钥未初始化
     */
    private String createToken(Map<String, Object> claims) {
        if (this.signingKey == null) {
            log.error("签名密钥未初始化！无法创建JWT令牌。");
            throw new IllegalStateException("JWT签名密钥未初始化。请检查配置和日志。");
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
}