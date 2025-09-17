package com.muyingmall.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final String ERROR_MESSAGE = "操作失败";
    
    private int code;
    private String message;
    private T data;
    private long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }
    
    public static Result<Void> success(String message) {
        return new Result<>(SUCCESS_CODE, message, null);
    }
    
    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, ERROR_MESSAGE, null);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message, null);
    }
    
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
    
    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }
    
    public boolean isError() {
        return this.code != SUCCESS_CODE;
    }
}