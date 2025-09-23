package com.muyingmall.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * 安全工具类 - 兼容性包装器
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.core.utils.SecurityUtils}，请使用新的位置。
 * 此类仅为向后兼容而保留，将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 */
@Deprecated
@Slf4j
@ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
public class SecurityUtil {

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SecurityUtils#getCurrentUsername()}
     */
    @Deprecated
    public static String getCurrentUsername() {
        return com.muyingmall.common.core.utils.SecurityUtils.getCurrentUsername();
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SecurityUtils#isAuthenticated()}
     */
    @Deprecated
    public static boolean isAuthenticated() {
        return com.muyingmall.common.core.utils.SecurityUtils.isAuthenticated();
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SecurityUtils#getCurrentAuthentication()}
     */
    @Deprecated
    public static Object getCurrentAuthentication() {
        return com.muyingmall.common.core.utils.SecurityUtils.getCurrentAuthentication();
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SecurityUtils#getCurrentUserId()}
     */
    @Deprecated
    public static Integer getCurrentUserId() {
        return com.muyingmall.common.core.utils.SecurityUtils.getCurrentUserId();
    }
}
