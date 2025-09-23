package com.muyingmall.common.util;

import com.muyingmall.common.core.enums.OrderStatus;
import com.muyingmall.common.core.enums.PaymentStatus;
import com.muyingmall.common.core.utils.EnumUtils;

/**
 * 枚举工具类 - 兼容性包装器
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.core.utils.EnumUtils}，请使用新的位置。
 * 此类仅为向后兼容而保留，将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 */
@Deprecated
public class EnumUtil {

    /**
     * 订单状态枚举 - 兼容性定义
     * @deprecated 使用 {@link com.muyingmall.common.core.enums.OrderStatus}
     */
    @Deprecated
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
     * 支付状态枚举 - 兼容性定义
     * @deprecated 使用 {@link com.muyingmall.common.core.enums.PaymentStatus}
     */
    @Deprecated
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
     * @deprecated 使用 {@link EnumUtils#getOrderStatusByCode(String)}
     */
    @Deprecated
    public static OrderStatus getOrderStatusByCode(String code) {
        com.muyingmall.common.core.enums.OrderStatus coreStatus = EnumUtils.getOrderStatusByCode(code);
        return convertToCoreOrderStatus(coreStatus);
    }

    /**
     * @deprecated 使用 {@link EnumUtils#getPaymentStatusByCode(String)}
     */
    @Deprecated
    public static PaymentStatus getPaymentStatusByCode(String code) {
        com.muyingmall.common.core.enums.PaymentStatus coreStatus = EnumUtils.getPaymentStatusByCode(code);
        return convertToCorePaymentStatus(coreStatus);
    }

    /**
     * @deprecated 使用 {@link EnumUtils#getOrderStatusCode(com.muyingmall.common.core.enums.OrderStatus)}
     */
    @Deprecated
    public static String getOrderStatusCode(com.muyingmall.common.core.enums.OrderStatus status) {
        return EnumUtils.getOrderStatusCode(status);
    }

    /**
     * @deprecated 使用 {@link EnumUtils#getPaymentStatusCode(com.muyingmall.common.core.enums.PaymentStatus)}
     */
    @Deprecated
    public static String getPaymentStatusCode(com.muyingmall.common.core.enums.PaymentStatus status) {
        return EnumUtils.getPaymentStatusCode(status);
    }

    // 转换方法
    private static OrderStatus convertToCoreOrderStatus(com.muyingmall.common.core.enums.OrderStatus coreStatus) {
        if (coreStatus == null) return OrderStatus.PENDING_PAYMENT;
        switch (coreStatus) {
            case PENDING_PAYMENT: return OrderStatus.PENDING_PAYMENT;
            case PAID: return OrderStatus.PAID;
            case DELIVERED: return OrderStatus.DELIVERED;
            case COMPLETED: return OrderStatus.COMPLETED;
            case CANCELLED: return OrderStatus.CANCELLED;
            default: return OrderStatus.PENDING_PAYMENT;
        }
    }

    private static PaymentStatus convertToCorePaymentStatus(com.muyingmall.common.core.enums.PaymentStatus coreStatus) {
        if (coreStatus == null) return PaymentStatus.PENDING;
        switch (coreStatus) {
            case PENDING: return PaymentStatus.PENDING;
            case PAID: return PaymentStatus.PAID;
            case FAILED: return PaymentStatus.FAILED;
            case REFUNDED: return PaymentStatus.REFUNDED;
            default: return PaymentStatus.PENDING;
        }
    }
}