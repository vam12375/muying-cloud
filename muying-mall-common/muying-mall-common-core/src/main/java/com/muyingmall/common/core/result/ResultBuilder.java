package com.muyingmall.common.core.result;

import com.muyingmall.common.core.enums.ErrorCode;
import com.muyingmall.common.core.enums.ResultCode;

/**
 * 响应结果构建工具类
 * 提供链式调用方式构建Result对象
 *
 * @param <T> 数据类型
 * @author 青柠檬
 * @since 2025-09-23
 */
public class ResultBuilder<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * 私有构造函数
     */
    private ResultBuilder() {
    }

    /**
     * 创建构建器实例
     *
     * @param <T> 数据类型
     * @return ResultBuilder实例
     */
    public static <T> ResultBuilder<T> create() {
        return new ResultBuilder<>();
    }

    /**
     * 设置响应码
     *
     * @param code 响应码
     * @return 构建器实例
     */
    public ResultBuilder<T> code(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 设置响应码（使用ResultCode枚举）
     *
     * @param resultCode 结果码枚举
     * @return 构建器实例
     */
    public ResultBuilder<T> code(ResultCode resultCode) {
        this.code = resultCode.getCode();
        if (this.message == null) {
            this.message = resultCode.getMessage();
        }
        return this;
    }

    /**
     * 设置响应码（使用ErrorCode枚举）
     *
     * @param errorCode 错误码枚举
     * @return 构建器实例
     */
    public ResultBuilder<T> code(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        if (this.message == null) {
            this.message = errorCode.getMessage();
        }
        return this;
    }

    /**
     * 设置响应消息
     *
     * @param message 响应消息
     * @return 构建器实例
     */
    public ResultBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置响应数据
     *
     * @param data 响应数据
     * @return 构建器实例
     */
    public ResultBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置为成功状态
     *
     * @return 构建器实例
     */
    public ResultBuilder<T> success() {
        this.code = ResultCode.SUCCESS.getCode();
        if (this.message == null) {
            this.message = ResultCode.SUCCESS.getMessage();
        }
        return this;
    }

    /**
     * 设置为成功状态并设置数据
     *
     * @param data 响应数据
     * @return 构建器实例
     */
    public ResultBuilder<T> success(T data) {
        return success().data(data);
    }

    /**
     * 设置为成功状态并设置消息和数据
     *
     * @param message 响应消息
     * @param data    响应数据
     * @return 构建器实例
     */
    public ResultBuilder<T> success(String message, T data) {
        return success().message(message).data(data);
    }

    /**
     * 设置为错误状态
     *
     * @return 构建器实例
     */
    public ResultBuilder<T> error() {
        this.code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
        if (this.message == null) {
            this.message = ResultCode.INTERNAL_SERVER_ERROR.getMessage();
        }
        return this;
    }

    /**
     * 设置为错误状态并设置消息
     *
     * @param message 错误消息
     * @return 构建器实例
     */
    public ResultBuilder<T> error(String message) {
        return error().message(message);
    }

    /**
     * 设置为错误状态并设置错误码和消息
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 构建器实例
     */
    public ResultBuilder<T> error(Integer code, String message) {
        return code(code).message(message);
    }

    /**
     * 构建Result对象
     *
     * @return Result对象
     */
    public Result<T> build() {
        // 设置默认值
        if (this.code == null) {
            this.code = ResultCode.SUCCESS.getCode();
        }
        if (this.message == null) {
            this.message = ResultCode.isSuccess(this.code) ? 
                ResultCode.SUCCESS.getMessage() : 
                ResultCode.INTERNAL_SERVER_ERROR.getMessage();
        }
        
        return new Result<>(this.code, this.message, this.data);
    }

    /**
     * 构建ApiResponse对象
     *
     * @return ApiResponse对象
     */
    public ApiResponse<T> buildApiResponse() {
        Result<T> result = build();
        return ApiResponse.from(result);
    }
}