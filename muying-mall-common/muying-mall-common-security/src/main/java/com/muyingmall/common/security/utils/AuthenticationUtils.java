package com.muyingmall.common.security.utils;

import com.muyingmall.common.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

/**
 * 认证工具类
 * 
 * <p>提供用户认证和权限验证的工具方法，支持JWT令牌解析和权限检查。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>从HTTP请求中提取JWT令牌</li>
 *   <li>验证用户认证状态</li>
 *   <li>获取当前用户信息</li>
 *   <li>检查用户权限和角色</li>
 *   <li>提供便捷的权限验证方法</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RestController
 * public class UserController {
 *     
 *     @Autowired
 *     private AuthenticationUtils authUtils;
 *     
 *     @GetMapping("/profile")
 *     public Result<UserVO> getProfile() {
 *         // 检查用户是否已认证
 *         if (!authUtils.isAuthenticated()) {
 *             return Result.error("用户未认证");
 *         }
 *         
 *         // 获取当前用户ID
 *         Integer userId = authUtils.getCurrentUserId();
 *         UserVO user = userService.getUser(userId);
 *         return Result.success(user);
 *     }
 *     
 *     @PostMapping("/admin/users")
 *     public Result<Void> createUser(@RequestBody UserDTO userDTO) {
 *         // 检查用户是否具有管理员角色
 *         if (!authUtils.hasRole("ADMIN")) {
 *             return Result.error("权限不足");
 *         }
 *         
 *         userService.createUser(userDTO);
 *         return Result.success();
 *     }
 * }
 * }</pre>
 * 
 * <p>令牌获取规则：</p>
 * <ul>
 *   <li>优先从Authorization头获取：Bearer {token}</li>
 *   <li>其次从请求参数token获取</li>
 *   <li>最后从Cookie中的token获取</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Component
public class AuthenticationUtils {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationUtils.class);

    /**
     * Authorization头前缀
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 令牌参数名
     */
    private static final String TOKEN_PARAM = "token";

    /**
     * JWT工具类
     */
    @Autowired
    private JwtUtils jwtUtils;

    // =============================令牌获取方法=============================

    /**
     * 从当前HTTP请求中获取JWT令牌
     * 
     * <p>按以下优先级获取令牌：</p>
     * <ol>
     *   <li>Authorization头：Bearer {token}</li>
     *   <li>请求参数：token={token}</li>
     *   <li>Cookie：token={token}</li>
     * </ol>
     *
     * @return JWT令牌，如果未找到则返回null
     */
    public String getTokenFromRequest() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            log.debug("无法获取当前HTTP请求");
            return null;
        }
        
        return getTokenFromRequest(request);
    }

    /**
     * 从指定HTTP请求中获取JWT令牌
     * 
     * <p>按以下优先级获取令牌：</p>
     * <ol>
     *   <li>Authorization头：Bearer {token}</li>
     *   <li>请求参数：token={token}</li>
     *   <li>Cookie：token={token}</li>
     * </ol>
     *
     * @param request HTTP请求对象
     * @return JWT令牌，如果未找到则返回null
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        // 1. 从Authorization头获取
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            if (StringUtils.hasText(token)) {
                log.debug("从Authorization头获取到令牌");
                return token;
            }
        }
        
        // 2. 从请求参数获取
        String tokenParam = request.getParameter(TOKEN_PARAM);
        if (StringUtils.hasText(tokenParam)) {
            log.debug("从请求参数获取到令牌");
            return tokenParam;
        }
        
        // 3. 从Cookie获取
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (TOKEN_PARAM.equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                    log.debug("从Cookie获取到令牌");
                    return cookie.getValue();
                }
            }
        }
        
        log.debug("未找到JWT令牌");
        return null;
    }

    // =============================认证状态检查方法=============================

    /**
     * 检查当前用户是否已认证
     * 
     * <p>通过验证JWT令牌来判断用户是否已认证。</p>
     *
     * @return true表示已认证，false表示未认证
     */
    public boolean isAuthenticated() {
        String token = getTokenFromRequest();
        if (token == null) {
            return false;
        }
        
        return jwtUtils.validateToken(token);
    }

    /**
     * 检查指定令牌是否有效
     * 
     * <p>验证指定的JWT令牌是否有效。</p>
     *
     * @param token JWT令牌
     * @return true表示令牌有效，false表示令牌无效
     */
    public boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        
        return jwtUtils.validateToken(token);
    }

    // =============================用户信息获取方法=============================

    /**
     * 获取当前用户ID
     * 
     * <p>从当前请求的JWT令牌中提取用户ID。</p>
     *
     * @return 用户ID，如果获取失败则返回null
     */
    public Integer getCurrentUserId() {
        String token = getTokenFromRequest();
        if (token == null) {
            return null;
        }
        
        return jwtUtils.getUserIdFromToken(token);
    }

    /**
     * 获取当前用户名
     * 
     * <p>从当前请求的JWT令牌中提取用户名。</p>
     *
     * @return 用户名，如果获取失败则返回null
     */
    public String getCurrentUsername() {
        String token = getTokenFromRequest();
        if (token == null) {
            return null;
        }
        
        return jwtUtils.getUsernameFromToken(token);
    }

    /**
     * 获取当前用户角色
     * 
     * <p>从当前请求的JWT令牌中提取用户角色。</p>
     *
     * @return 用户角色，如果获取失败则返回null
     */
    public String getCurrentUserRole() {
        String token = getTokenFromRequest();
        if (token == null) {
            return null;
        }
        
        return jwtUtils.getRoleFromToken(token);
    }

    /**
     * 获取当前用户权限集合
     * 
     * <p>从当前请求的JWT令牌中提取用户权限。</p>
     *
     * @return 权限集合，如果获取失败则返回空Set
     */
    public Set<String> getCurrentUserPermissions() {
        String token = getTokenFromRequest();
        if (token == null) {
            return Set.of();
        }
        
        return jwtUtils.getPermissionsFromToken(token);
    }

    // =============================权限检查方法=============================

    /**
     * 检查当前用户是否具有指定角色
     * 
     * <p>验证当前用户的角色是否匹配指定角色。</p>
     *
     * @param role 角色名称
     * @return true表示具有指定角色，false表示不具有
     */
    public boolean hasRole(String role) {
        if (role == null) {
            return false;
        }
        
        String token = getTokenFromRequest();
        if (token == null) {
            return false;
        }
        
        return jwtUtils.hasRole(token, role);
    }

    /**
     * 检查当前用户是否具有任一指定角色
     * 
     * <p>验证当前用户是否具有角色集合中的任意一个角色。</p>
     *
     * @param roles 角色集合
     * @return true表示具有任一角色，false表示都不具有
     */
    public boolean hasAnyRole(String... roles) {
        if (roles == null || roles.length == 0) {
            return false;
        }
        
        String currentRole = getCurrentUserRole();
        if (currentRole == null) {
            return false;
        }
        
        for (String role : roles) {
            if (currentRole.equals(role)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查当前用户是否具有指定权限
     * 
     * <p>验证当前用户是否具有指定的权限。</p>
     *
     * @param permission 权限名称
     * @return true表示具有指定权限，false表示不具有
     */
    public boolean hasPermission(String permission) {
        if (permission == null) {
            return false;
        }
        
        String token = getTokenFromRequest();
        if (token == null) {
            return false;
        }
        
        return jwtUtils.hasPermission(token, permission);
    }

    /**
     * 检查当前用户是否具有任一指定权限
     * 
     * <p>验证当前用户是否具有权限集合中的任意一个权限。</p>
     *
     * @param permissions 权限集合
     * @return true表示具有任一权限，false表示都不具有
     */
    public boolean hasAnyPermission(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        
        String token = getTokenFromRequest();
        if (token == null) {
            return false;
        }
        
        return jwtUtils.hasAnyPermission(token, Set.of(permissions));
    }

    /**
     * 检查当前用户是否具有所有指定权限
     * 
     * <p>验证当前用户是否具有权限集合中的所有权限。</p>
     *
     * @param permissions 权限集合
     * @return true表示具有所有权限，false表示缺少某些权限
     */
    public boolean hasAllPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        
        String token = getTokenFromRequest();
        if (token == null) {
            return false;
        }
        
        return jwtUtils.hasAllPermissions(token, Set.of(permissions));
    }

    // =============================便捷验证方法=============================

    /**
     * 要求用户已认证，否则抛出异常
     * 
     * <p>如果用户未认证，抛出SecurityException异常。</p>
     *
     * @throws SecurityException 如果用户未认证
     */
    public void requireAuthentication() {
        if (!isAuthenticated()) {
            throw new SecurityException("用户未认证，请先登录");
        }
    }

    /**
     * 要求用户具有指定角色，否则抛出异常
     * 
     * <p>如果用户不具有指定角色，抛出SecurityException异常。</p>
     *
     * @param role 角色名称
     * @throws SecurityException 如果用户不具有指定角色
     */
    public void requireRole(String role) {
        requireAuthentication();
        if (!hasRole(role)) {
            throw new SecurityException("用户角色权限不足，需要角色: " + role);
        }
    }

    /**
     * 要求用户具有指定权限，否则抛出异常
     * 
     * <p>如果用户不具有指定权限，抛出SecurityException异常。</p>
     *
     * @param permission 权限名称
     * @throws SecurityException 如果用户不具有指定权限
     */
    public void requirePermission(String permission) {
        requireAuthentication();
        if (!hasPermission(permission)) {
            throw new SecurityException("用户权限不足，需要权限: " + permission);
        }
    }

    /**
     * 检查当前用户是否为指定用户或管理员
     * 
     * <p>用于资源访问控制，允许用户访问自己的资源或管理员访问所有资源。</p>
     *
     * @param targetUserId 目标用户ID
     * @return true表示有权访问，false表示无权访问
     */
    public boolean canAccessUserResource(Integer targetUserId) {
        if (targetUserId == null) {
            return false;
        }
        
        // 检查是否为管理员
        if (hasRole("ADMIN")) {
            return true;
        }
        
        // 检查是否为用户本人
        Integer currentUserId = getCurrentUserId();
        return targetUserId.equals(currentUserId);
    }

    // =============================工具方法=============================

    /**
     * 获取当前HTTP请求对象
     * 
     * <p>从Spring的RequestContextHolder中获取当前请求对象。</p>
     *
     * @return HTTP请求对象，如果获取失败则返回null
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            log.debug("获取当前HTTP请求失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 记录安全事件
     * 
     * <p>记录安全相关的事件，如认证失败、权限不足等。</p>
     *
     * @param event 事件描述
     * @param userId 用户ID
     */
    public void logSecurityEvent(String event, Integer userId) {
        log.info("安全事件: {} - 用户ID: {} - IP: {}", event, userId, getClientIpAddress());
    }

    /**
     * 获取客户端IP地址
     * 
     * <p>从HTTP请求中获取客户端的真实IP地址，考虑代理和负载均衡的情况。</p>
     *
     * @return 客户端IP地址
     */
    public String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return "unknown";
        }
        
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}