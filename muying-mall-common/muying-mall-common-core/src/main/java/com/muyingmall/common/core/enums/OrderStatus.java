package com.muyingmall.common.core.enums;

/**
 * 订单状态枚举
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public enum OrderStatus {
    
    /**
     * 待付款
     */
    PENDING_PAYMENT("PENDING_PAYMENT", "待付款"),
    
    /**
     * 已付款
     */
    PAID("PAID", "已付款"),
    
    /**
     * 待发货
     */
    PENDING_SHIPMENT("PENDING_SHIPMENT", "待发货"),
    
    /**
     * 已发货
     */
    SHIPPED("SHIPPED", "已发货"),
    
    /**
     * 待收货
     */
    PENDING_RECEIPT("PENDING_RECEIPT", "待收货"),
    
    /**
     * 已完成
     */
    COMPLETED("COMPLETED", "已完成"),
    
    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消"),
    
    /**
     * 退款中
     */
    REFUNDING("REFUNDING", "退款中"),
    
    /**
     * 已退款
     */
    REFUNDED("REFUNDED", "已退款"),
    
    /**
     * 退货中
     */
    RETURNING("RETURNING", "退货中"),
    
    /**
     * 已退货
     */
    RETURNED("RETURNED", "已退货");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
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
     * 
     * @param code 状态代码
     * @return 订单状态枚举
     * @throws IllegalArgumentException 如果状态代码无效
     */
    public static OrderStatus fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Order status code cannot be null");
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status code: " + code);
    }

    /**
     * 判断是否为终态
     * 
     * @return 是否为终态
     */
    public boolean isFinalStatus() {
        return this == COMPLETED || this == CANCELLED || this == REFUNDED || this == RETURNED;
    }

    /**
     * 判断是否可以取消
     * 
     * @return 是否可以取消
     */
    public boolean isCancellable() {
        return this == PENDING_PAYMENT || this == PAID || this == PENDING_SHIPMENT;
    }

    /**
     * 判断是否可以申请退款
     * 
     * @return 是否可以申请退款
     */
    public boolean isRefundable() {
        return this == PAID || this == PENDING_SHIPMENT || this == SHIPPED || this == PENDING_RECEIPT || this == COMPLETED;
    }
    
    /**
     * 判断是否可以转换到指定状态
     * 
     * @param targetStatus 目标状态
     * @return 是否可以转换
     */
    public boolean canTransitionTo(OrderStatus targetStatus) {
        if (targetStatus == null) {
            return false;
        }
        
        switch (this) {
            case PENDING_PAYMENT:
                // 待付款可以转为：已付款、已取消
                return targetStatus == PAID || targetStatus == CANCELLED;
                
            case PAID:
                // 已付款可以转为：待发货、已取消、退款中
                return targetStatus == PENDING_SHIPMENT || targetStatus == CANCELLED || targetStatus == REFUNDING;
                
            case PENDING_SHIPMENT:
                // 待发货可以转为：已发货、已取消、退款中
                return targetStatus == SHIPPED || targetStatus == CANCELLED || targetStatus == REFUNDING;
                
            case SHIPPED:
                // 已发货可以转为：待收货、退货中
                return targetStatus == PENDING_RECEIPT || targetStatus == RETURNING;
                
            case PENDING_RECEIPT:
                // 待收货可以转为：已完成、退货中
                return targetStatus == COMPLETED || targetStatus == RETURNING;
                
            case COMPLETED:
                // 已完成可以转为：退货中（在一定时间内）
                return targetStatus == RETURNING;
                
            case CANCELLED:
            case REFUNDED:
            case RETURNED:
                // 终态，不能转换到其他状态
                return false;
                
            case REFUNDING:
                // 退款中可以转为：已退款、已取消（退款失败）
                return targetStatus == REFUNDED || targetStatus == CANCELLED;
                
            case RETURNING:
                // 退货中可以转为：已退货、已取消（退货失败）
                return targetStatus == RETURNED || targetStatus == CANCELLED;
                
            default:
                return false;
        }
    }
}