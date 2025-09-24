package com.muyingmall.common.constants;

/**
 * 向后兼容类 - ApiErrorCodes
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.constant.ApiErrorCodes} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class ApiErrorCodes {
    
    // 重新定义错误码以保持向后兼容
    public static final Integer SUCCESS = 200;
    public static final Integer BAD_REQUEST = 400;
    public static final Integer UNAUTHORIZED = 401;
    public static final Integer FORBIDDEN = 403;
    public static final Integer NOT_FOUND = 404;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer VALIDATION_ERROR = 1001;
    public static final Integer BUSINESS_ERROR = 1002;
    public static final Integer AUTHENTICATION_ERROR = 1003;
    public static final Integer AUTHORIZATION_ERROR = 1004;
    
    /**
     * 私有构造函数，防止实例化
     */
    private ApiErrorCodes() {
        // 工具类不允许实例化
    }
}