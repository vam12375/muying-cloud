package com.muyingmall.common.core.exception;

import com.muyingmall.common.core.enums.ErrorCode;

/**
 * 异常工具类
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public class ExceptionUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private ExceptionUtils() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    /**
     * 抛出业务异常
     *
     * @param message 错误信息
     */
    public static void throwBusiness(String message) {
        throw new BusinessException(message);
    }

    /**
     * 抛出业务异常
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static void throwBusiness(Integer code, String message) {
        throw new BusinessException(code, message);
    }

    /**
     * 抛出业务异常
     *
     * @param errorCode 错误码枚举
     */
    public static void throwBusiness(ErrorCode errorCode) {
        throw new BusinessException(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 抛出参数校验异常
     *
     * @param message 错误信息
     */
    public static void throwValidation(String message) {
        throw new ValidationException(message);
    }

    /**
     * 抛出认证异常
     *
     * @param message 错误信息
     */
    public static void throwAuthentication(String message) {
        throw new AuthenticationException(message);
    }

    /**
     * 抛出授权异常
     *
     * @param message 错误信息
     */
    public static void throwAuthorization(String message) {
        throw new AuthorizationException(message);
    }

    /**
     * 抛出资源未找到异常
     *
     * @param message 错误信息
     */
    public static void throwResourceNotFound(String message) {
        throw new ResourceNotFoundException(message);
    }

    /**
     * 条件抛出业务异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void throwBusinessIf(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 条件抛出业务异常
     *
     * @param condition 条件
     * @param errorCode 错误码枚举
     */
    public static void throwBusinessIf(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode.getCode(), errorCode.getMessage());
        }
    }

    /**
     * 条件抛出参数校验异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void throwValidationIf(boolean condition, String message) {
        if (condition) {
            throw new ValidationException(message);
        }
    }

    /**
     * 条件抛出认证异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void throwAuthenticationIf(boolean condition, String message) {
        if (condition) {
            throw new AuthenticationException(message);
        }
    }

    /**
     * 条件抛出授权异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void throwAuthorizationIf(boolean condition, String message) {
        if (condition) {
            throw new AuthorizationException(message);
        }
    }

    /**
     * 条件抛出资源未找到异常
     *
     * @param condition 条件
     * @param message   错误信息
     */
    public static void throwResourceNotFoundIf(boolean condition, String message) {
        if (condition) {
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * 获取异常的根本原因
     *
     * @param throwable 异常
     * @return 根本原因
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * 获取异常的根本原因消息
     *
     * @param throwable 异常
     * @return 根本原因消息
     */
    public static String getRootCauseMessage(Throwable throwable) {
        Throwable rootCause = getRootCause(throwable);
        return rootCause.getMessage();
    }
}