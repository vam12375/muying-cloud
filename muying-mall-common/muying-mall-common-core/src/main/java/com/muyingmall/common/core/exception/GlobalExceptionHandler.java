package com.muyingmall.common.core.exception;

import com.muyingmall.common.core.enums.ErrorCode;
import com.muyingmall.common.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;

/**
 * 全局异常处理器
 * 
 * @author muying-mall
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 - @Valid 注解校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder("参数校验失败: ");
        
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage());
            }
        }
        
        String errorMessage = message.toString();
        log.warn("参数校验异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder("参数绑定失败: ");
        
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage());
            }
        }
        
        String errorMessage = message.toString();
        log.warn("参数绑定异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理约束违反异常 - @Validated 注解校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder message = new StringBuilder("参数校验失败: ");
        
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
        }
        
        String errorMessage = message.toString();
        log.warn("约束违反异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String errorMessage = "缺少必需的请求参数: " + e.getParameterName();
        log.warn("缺少请求参数异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMessage = "参数类型不匹配: " + e.getName() + " 应该是 " + e.getRequiredType().getSimpleName() + " 类型";
        log.warn("参数类型不匹配异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理HTTP请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorMessage = "不支持的请求方法: " + e.getMethod();
        log.warn("请求方法不支持异常 - URI: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(ErrorCode.OPERATION_NOT_ALLOWED.getCode(), errorMessage);
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("认证异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        log.warn("授权异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("资源未找到异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(ValidationException e, HttpServletRequest request) {
        log.warn("参数校验异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常 - URI: {}, 错误信息: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 - URI: {}, 错误信息: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }
}