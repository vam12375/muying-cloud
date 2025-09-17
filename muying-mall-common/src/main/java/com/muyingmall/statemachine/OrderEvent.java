package com.muyingmall.statemachine;

/**
 * 订单事件枚举
 */
public enum OrderEvent {
    
    /**
     * 支付
     */
    PAY("PAY", "支付"),
    
    /**
     * 发货
     */
    SHIP("SHIP", "发货"),
    
    /**
     * 确认收货
     */
    RECEIVE("RECEIVE", "确认收货"),
    
    /**
     * 取消订单
     */
    CANCEL("CANCEL", "取消订单"),
    
    /**
     * 申请退款
     */
    REFUND("REFUND", "申请退款"),
    
    /**
     * 确认退款
     */
    CONFIRM_REFUND("CONFIRM_REFUND", "确认退款"),
    
    /**
     * 申请退货
     */
    RETURN("RETURN", "申请退货"),
    
    /**
     * 确认退货
     */
    CONFIRM_RETURN("CONFIRM_RETURN", "确认退货"),
    
    /**
     * 完成订单
     */
    COMPLETE("COMPLETE", "完成订单");

    private final String code;
    private final String description;

    OrderEvent(String code, String description) {
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
     * @param code 事件代码
     * @return 订单事件枚举
     */
    public static OrderEvent fromCode(String code) {
        for (OrderEvent event : OrderEvent.values()) {
            if (event.code.equals(code)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown order event code: " + code);
    }
}