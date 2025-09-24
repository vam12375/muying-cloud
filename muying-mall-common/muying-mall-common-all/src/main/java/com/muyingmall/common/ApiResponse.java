package com.muyingmall.common;

/**
 * 向后兼容类 - ApiResponse
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.result.ApiResponse} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class ApiResponse<T> extends com.muyingmall.common.core.result.ApiResponse<T> {
    
    /**
     * 默认构造函数
     */
    public ApiResponse() {
        super();
    }
    
    /**
     * 构造函数
     * 
     * @param success 是否成功
     * @param message 响应消息
     * @param data 响应数据
     */
    public ApiResponse(Boolean success, String message, T data) {
        super(success ? 200 : 500, message, data, success);
    }
    
    /**
     * 成功响应
     * 
     * @param <T> 数据类型
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, "操作成功", null);
    }
    
    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data);
    }
    
    /**
     * 失败响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}