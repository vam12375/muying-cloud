package com.muyingmall.common.constant;

/**
 * 向后兼容类 - CommonConstants
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.constant.CommonConstants} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class CommonConstants {
    
    // 重新定义常量以保持向后兼容
    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_CODE = "500";
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String ERROR_MESSAGE = "操作失败";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    
    /**
     * 私有构造函数，防止实例化
     */
    private CommonConstants() {
        // 工具类不允许实例化
    }
}