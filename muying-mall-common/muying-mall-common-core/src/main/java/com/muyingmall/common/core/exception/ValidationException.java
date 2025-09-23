package com.muyingmall.common.core.exception;

/**
 * 参数校验异常
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public class ValidationException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认错误码
     */
    private static final Integer DEFAULT_CODE = 400;

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public ValidationException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误信息
     * @param cause   原因异常
     */
    public ValidationException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public ValidationException(Integer code, String message) {
        super(code, message);
    }
}