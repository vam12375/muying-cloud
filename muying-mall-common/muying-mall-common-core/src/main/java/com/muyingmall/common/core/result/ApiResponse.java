package com.muyingmall.common.core.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API 响应包装类
 * 提供统一的API响应格式，包含响应码、消息、数据和成功标识
 *
 * @param <T> 响应数据类型
 * @author 青柠檬
 * @since 2025-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiResponse<T> extends Result<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 默认构造函数
     */
    public ApiResponse() {
        super();
    }

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     * @param data    响应数据
     * @param success 是否成功
     */
    public ApiResponse(Integer code, String message, T data, boolean success) {
        super(code, message, data);
        this.success = success;
    }

    /**
     * 创建成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data, true);
    }

    /**
     * 创建成功响应
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, true);
    }

    /**
     * 创建成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功", null, true);
    }

    /**
     * 创建成功响应（无数据，自定义消息）
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> successWithMessage(String message) {
        return new ApiResponse<>(200, message, null, true);
    }

    /**
     * 创建失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, false);
    }

    /**
     * 创建失败响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null, false);
    }

    /**
     * 创建失败响应（默认消息）
     *
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "操作失败", null, false);
    }

    /**
     * 从Result对象转换为ApiResponse
     *
     * @param result Result对象
     * @param <T>    数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> from(Result<T> result) {
        if (result == null) {
            return error("结果为空");
        }
        boolean success = result.getCode() != null && result.getCode() == 200;
        return new ApiResponse<>(result.getCode(), result.getMessage(), result.getData(), success);
    }
}