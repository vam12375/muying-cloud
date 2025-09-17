package com.muyingmall.common.enums;

/**
 * 支付状态枚举
 */
public enum PaymentStatus {
    
    /**
     * 待支付
     */
    PENDING("PENDING", "待支付"),
    
    /**
     * 支付中
     */
    PROCESSING("PROCESSING", "支付中"),
    
    /**
     * 支付成功
     */
    SUCCESS("SUCCESS", "支付成功"),
    
    /**
     * 支付失败
     */
    FAILED("FAILED", "支付失败"),
    
    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消"),
    
    /**
     * 超时取消
     */
    TIMEOUT("TIMEOUT", "超时取消"),
    
    /**
     * 退款中
     */
    REFUNDING("REFUNDING", "退款中"),
    
    /**
     * 已退款
     */
    REFUNDED("REFUNDED", "已退款"),
    
    /**
     * 部分退款
     */
    PARTIAL_REFUNDED("PARTIAL_REFUNDED", "部分退款");

    private final String code;
    private final String description;

    PaymentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     * @param code 状态代码
     * @return 支付状态枚举
     */
    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown payment status code: " + code);
    }

    /**
     * 判断是否为终态
     * @return 是否为终态
     */
    public boolean isFinalStatus() {
        return this == SUCCESS || this == FAILED || this == CANCELLED || 
               this == TIMEOUT || this == REFUNDED || this == PARTIAL_REFUNDED;
    }

    /**
     * 判断是否为成功状态
     * @return 是否为成功状态
     */
    public boolean isSuccessStatus() {
        return this == SUCCESS;
    }

    /**
     * 判断是否可以退款
     * @return 是否可以退款
     */
    public boolean isRefundable() {
        return this == SUCCESS;
    }

    /**
     * 判断是否为失败状态
     * @return 是否为失败状态
     */
    public boolean isFailedStatus() {
        return this == FAILED || this == CANCELLED || this == TIMEOUT;
    }
}