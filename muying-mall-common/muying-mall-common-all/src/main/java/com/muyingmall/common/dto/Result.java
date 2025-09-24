package com.muyingmall.common.dto;

/**
 * 向后兼容类 - Result
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.result.Result} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class Result<T> extends com.muyingmall.common.core.result.Result<T> {
    
    /**
     * 默认构造函数
     */
    public Result() {
        super();
    }
    
    /**
     * 构造函数
     * 
     * @param code 响应码
     * @param message 响应消息
     * @param data 响应数据
     */
    public Result(Integer code, String message, T data) {
        super(code, message, data);
    }
    
    /**
     * 成功响应
     * 
     * @param <T> 数据类型
     * @return Result实例
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result实例
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    /**
     * 失败响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result实例
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result实例
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}