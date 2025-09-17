package com.muyingmall.common.util;

/**
 * 枚举工具类
 */
public class EnumUtil {

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING_PAYMENT("PENDING_PAYMENT", "待支付"),
        PAID("PAID", "已支付"),
        DELIVERED("DELIVERED", "已发货"),
        COMPLETED("COMPLETED", "已完成"),
        CANCELLED("CANCELLED", "已取消");

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
    }

    /**
     * 支付状态枚举
     */
    public enum PaymentStatus {
        PENDING("PENDING", "待支付"),
        PAID("PAID", "已支付"),
        FAILED("FAILED", "支付失败"),
        REFUNDED("REFUNDED", "已退款");

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
    }

    /**
     * 根据状态编码获取订单状态枚举
     */
    public static OrderStatus getOrderStatusByCode(String code) {
        if (code == null) {
            return OrderStatus.PENDING_PAYMENT;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return OrderStatus.PENDING_PAYMENT;
    }

    /**
     * 根据状态编码获取支付状态枚举
     */
    public static PaymentStatus getPaymentStatusByCode(String code) {
        if (code == null) {
            return PaymentStatus.PENDING;
        }
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return PaymentStatus.PENDING;
    }

    /**
     * 获取订单状态编码
     */
    public static String getOrderStatusCode(com.muyingmall.common.enums.OrderStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 获取支付状态编码
     */
    public static String getPaymentStatusCode(com.muyingmall.common.enums.PaymentStatus status) {
        return status != null ? status.getCode() : null;
    }
}
