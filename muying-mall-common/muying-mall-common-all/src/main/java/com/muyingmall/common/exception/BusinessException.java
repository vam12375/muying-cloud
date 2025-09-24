package com.muyingmall.common.exception;

/**
 * 向后兼容类 - BusinessException
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.exception.BusinessException} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class BusinessException extends com.muyingmall.common.core.exception.BusinessException {
    
    /**
     * 构造函数
     * 
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 异常消息
     */
    public BusinessException(Integer code, String message) {
        super(code, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 异常消息
     * @param cause 原因
     */
    public BusinessException(String message, Throwable cause) {
        super(500, message, cause);
    }
}