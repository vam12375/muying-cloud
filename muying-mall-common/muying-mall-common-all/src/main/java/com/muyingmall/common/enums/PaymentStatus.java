package com.muyingmall.common.enums;

/**
 * 向后兼容类 - PaymentStatus
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.enums.PaymentStatus} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public enum PaymentStatus {
    
    /** 待支付 */
    PENDING(1, "待支付"),
    
    /** 支付成功 */
    SUCCESS(2, "支付成功"),
    
    /** 支付失败 */
    FAILED(3, "支付失败"),
    
    /** 已退款 */
    REFUNDED(4, "已退款"),
    
    /** 部分退款 */
    PARTIAL_REFUNDED(5, "部分退款");
    
    private final Integer code;
    private final String description;
    
    PaymentStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     * 
     * @param code 状态码
     * @return PaymentStatus枚举
     */
    public static PaymentStatus fromCode(Integer code) {
        for (PaymentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的支付状态码: " + code);
    }
}