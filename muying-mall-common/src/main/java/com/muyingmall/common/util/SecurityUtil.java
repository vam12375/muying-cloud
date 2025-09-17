package com.muyingmall.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * 安全工具类，用于获取当前登录用户信息
 * 只有在启用Spring Security时才生效
 */
@Slf4j
@ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
public class SecurityUtil {

    /**
     * 获取当前登录用户名
     * 需要Spring Security环境
     */
    public static String getCurrentUsername() {
        try {
            // 使用反射来避免编译时依赖
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = context.getClass().getMethod("getAuthentication").invoke(context);

            if (authentication != null) {
                Boolean isAuthenticated = (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
                if (isAuthenticated) {
                    return (String) authentication.getClass().getMethod("getName").invoke(authentication);
                }
            }
        } catch (Exception e) {
            log.debug("Spring Security不可用或获取当前用户名失败", e);
        }
        return null;
    }

    /**
     * 判断当前用户是否已认证
     * 需要Spring Security环境
     */
    public static boolean isAuthenticated() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = context.getClass().getMethod("getAuthentication").invoke(context);

            if (authentication != null) {
                return (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
            }
        } catch (Exception e) {
            log.debug("Spring Security不可用或判断认证状态失败", e);
        }
        return false;
    }

    /**
     * 获取当前认证信息（返回Object类型避免编译依赖）
     * 需要Spring Security环境
     */
    public static Object getCurrentAuthentication() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            return context.getClass().getMethod("getAuthentication").invoke(context);
        } catch (Exception e) {
            log.debug("Spring Security不可用或获取认证信息失败", e);
            return null;
        }
    }

    /**
     * 获取当前登录用户ID
     * 需要Spring Security环境和JWT支持
     * 注意：这是一个示例实现，实际项目中需要根据具体情况调整
     */
    public static Integer getCurrentUserId() {
        try {
            // 这里只返回null，实际应用中需要从 JWT 中解析用户ID
            // 或者从 UserDetails 中获取
            log.warn("当前getCurrentUserId()方法未完全实现，请在实际项目中完善");
            return null;
        } catch (Exception e) {
            log.debug("获取当前用户ID失败", e);
            return null;
        }
    }
}
