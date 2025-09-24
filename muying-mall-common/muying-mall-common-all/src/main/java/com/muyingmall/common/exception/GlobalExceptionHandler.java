package com.muyingmall.common.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 向后兼容类 - GlobalExceptionHandler
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.exception.GlobalExceptionHandler} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestControllerAdvice
public class GlobalExceptionHandler extends com.muyingmall.common.core.exception.GlobalExceptionHandler {
    
    /**
     * 构造函数
     */
    public GlobalExceptionHandler() {
        super();
    }
}