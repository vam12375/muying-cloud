package com.muyingmall.common.core.enums;

/**
 * 错误码枚举
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public enum ErrorCode {

    // 通用错误码 (1000-1999)
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统繁忙，请稍后再试"),
    PARAM_ERROR(400, "参数错误"),
    VALIDATION_ERROR(400, "参数校验失败"),
    
    // 认证授权错误码 (2000-2999)
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限访问"),
    TOKEN_INVALID(401, "Token无效"),
    TOKEN_EXPIRED(401, "Token已过期"),
    
    // 业务错误码 (3000-3999)
    RESOURCE_NOT_FOUND(404, "资源不存在"),
    RESOURCE_ALREADY_EXISTS(409, "资源已存在"),
    OPERATION_NOT_ALLOWED(405, "操作不被允许"),
    
    // 用户相关错误码 (4000-4999)
    USER_NOT_FOUND(4001, "用户不存在"),
    USER_ALREADY_EXISTS(4002, "用户已存在"),
    USER_PASSWORD_ERROR(4003, "密码错误"),
    USER_DISABLED(4004, "用户已被禁用"),
    
    // 商品相关错误码 (5000-5999)
    PRODUCT_NOT_FOUND(5001, "商品不存在"),
    PRODUCT_STOCK_INSUFFICIENT(5002, "商品库存不足"),
    PRODUCT_OFFLINE(5003, "商品已下架"),
    
    // 订单相关错误码 (6000-6999)
    ORDER_NOT_FOUND(6001, "订单不存在"),
    ORDER_STATUS_ERROR(6002, "订单状态错误"),
    ORDER_CANNOT_CANCEL(6003, "订单无法取消"),
    
    // 支付相关错误码 (7000-7999)
    PAYMENT_FAILED(7001, "支付失败"),
    PAYMENT_TIMEOUT(7002, "支付超时"),
    PAYMENT_NOT_FOUND(7003, "支付记录不存在"),
    REFUND_FAILED(7004, "退款失败"),
    
    // 系统错误码 (9000-9999)
    SYSTEM_BUSY(9001, "系统繁忙，请稍后重试"),
    SYSTEM_EXCEPTION(9999, "系统异常");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码获取枚举
     *
     * @param code 错误码
     * @return 错误码枚举
     */
    public static ErrorCode getByCode(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
}