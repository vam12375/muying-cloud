package com.muyingmall.common.enums;

public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "访问被拒绝"),
    NOT_FOUND(404, "资源不存在"),
    
    VALIDATION_FAILED(400, "参数校验失败"),
    LOGIN_FAILED(40001, "登录失败"),
    TOKEN_EXPIRED(40002, "令牌已过期"),
    TOKEN_INVALID(40003, "令牌无效"),
    
    USER_NOT_FOUND(50001, "用户不存在"),
    USER_ALREADY_EXISTS(50002, "用户已存在"),
    PASSWORD_ERROR(50003, "密码错误"),
    
    PRODUCT_NOT_FOUND(60001, "商品不存在"),
    PRODUCT_STOCK_INSUFFICIENT(60002, "商品库存不足"),
    
    ORDER_NOT_FOUND(70001, "订单不存在"),
    ORDER_STATUS_ERROR(70002, "订单状态错误"),
    
    PAYMENT_FAILED(80001, "支付失败"),
    PAYMENT_NOT_FOUND(80002, "支付记录不存在"),
    
    SYSTEM_ERROR(99999, "系统异常");
    
    private final int code;
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}