package com.muyingmall.common.core.result;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 
 * @param <T> 数据类型
 * @author muying-mall
 * @since 1.0.0
 */
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 成功状态码
     */
    private static final Integer SUCCESS_CODE = 200;
    
    /**
     * 错误状态码
     */
    private static final Integer ERROR_CODE = 500;
    
    /**
     * 成功消息
     */
    private static final String SUCCESS_MESSAGE = "操作成功";
    
    /**
     * 错误消息
     */
    private static final String ERROR_MESSAGE = "操作失败";
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 默认构造函数
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     */
    public Result(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }
    
    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }
    
    /**
     * 成功响应
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }
    
    /**
     * 成功响应
     *
     * @param message 消息
     * @return 响应结果
     */
    public static Result<Void> success(String message) {
        return new Result<>(SUCCESS_CODE, message, null);
    }
    
    /**
     * 错误响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, ERROR_MESSAGE, null);
    }
    
    /**
     * 错误响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message, null);
    }
    
    /**
     * 错误响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 错误响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }
    
    /**
     * 判断是否失败
     *
     * @return 是否失败
     */
    public boolean isError() {
        return !SUCCESS_CODE.equals(this.code);
    }

    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}