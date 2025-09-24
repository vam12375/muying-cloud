package com.muyingmall.common.enums;

/**
 * 向后兼容类 - OrderStatus
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.enums.OrderStatus} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public enum OrderStatus {
    
    /** 待支付 */
    PENDING_PAYMENT(1, "待支付"),
    
    /** 已支付 */
    PAID(2, "已支付"),
    
    /** 已发货 */
    SHIPPED(3, "已发货"),
    
    /** 已完成 */
    COMPLETED(4, "已完成"),
    
    /** 已取消 */
    CANCELLED(5, "已取消"),
    
    /** 已退款 */
    REFUNDED(6, "已退款");
    
    private final Integer code;
    private final String description;
    
    OrderStatus(Integer code, String description) {
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
     * @return OrderStatus枚举
     */
    public static OrderStatus fromCode(Integer code) {
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的订单状态码: " + code);
    }
}