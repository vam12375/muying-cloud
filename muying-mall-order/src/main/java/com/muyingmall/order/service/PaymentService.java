package com.muyingmall.order.service;

import com.muyingmall.order.entity.Payment;

import java.math.BigDecimal;

/**
 * 支付服务接口（订单服务中的简化版本）
 */
public interface PaymentService {

    /**
     * 创建支付订单
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式
     * @return 支付订单
     */
    Payment createPayment(Long orderId, String orderNo, Long userId, BigDecimal amount, String paymentMethod);

    /**
     * 根据订单ID获取支付信息
     * @param orderId 订单ID
     * @return 支付信息
     */
    Payment getByOrderId(Long orderId);

    /**
     * 根据支付订单号获取支付信息
     * @param paymentOrderNo 支付订单号
     * @return 支付信息
     */
    Payment getByPaymentOrderNo(String paymentOrderNo);

    /**
     * 更新支付状态
     * @param paymentOrderNo 支付订单号
     * @param status 支付状态
     * @param thirdPartyOrderNo 第三方订单号
     * @return 是否更新成功
     */
    boolean updatePaymentStatus(String paymentOrderNo, String status, String thirdPartyOrderNo);
    
    /**
     * 根据ID获取支付信息（兼容方法）
     * @param paymentId 支付ID
     * @return 支付信息
     */
    default Payment getById(Long paymentId) {
        // 默认实现，子类可以重写
        return null;
    }
    
    /**
     * 保存支付信息（兼容方法）
     * @param payment 支付信息
     * @return 是否保存成功
     */
    default boolean save(Payment payment) {
        // 默认实现，子类可以重写
        return false;
    }
}