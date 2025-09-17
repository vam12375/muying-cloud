package com.muyingmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.enums.PaymentStatus;
import com.muyingmall.order.entity.Payment;
import com.muyingmall.order.mapper.PaymentMapper;
import com.muyingmall.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 支付服务实现类
 */
@Service
@Slf4j
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Override
    public Payment createPayment(Long orderId, String orderNo, Long userId, BigDecimal amount, String paymentMethod) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setOrderNo(orderNo);
        payment.setPaymentOrderNo(generatePaymentOrderNo());
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setActualAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(PaymentStatus.PENDING.getCode());
        payment.setCreatedTime(LocalDateTime.now());
        payment.setUpdatedTime(LocalDateTime.now());
        
        // 保存支付记录
        boolean result = save(payment);
        if (result) {
            log.info("创建支付记录成功: orderId={}, paymentOrderNo={}", orderId, payment.getPaymentOrderNo());
            return payment;
        } else {
            log.error("创建支付记录失败: orderId={}", orderId);
            return null;
        }
    }

    @Override
    public Payment getByOrderId(Long orderId) {
        if (orderId == null) {
            return null;
        }
        
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Payment::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public Payment getByPaymentOrderNo(String paymentOrderNo) {
        if (paymentOrderNo == null || paymentOrderNo.trim().isEmpty()) {
            return null;
        }
        
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Payment::getPaymentOrderNo, paymentOrderNo);
        return getOne(queryWrapper);
    }

    @Override
    public boolean updatePaymentStatus(String paymentOrderNo, String status, String thirdPartyOrderNo) {
        if (paymentOrderNo == null || paymentOrderNo.trim().isEmpty()) {
            return false;
        }
        
        Payment payment = getByPaymentOrderNo(paymentOrderNo);
        if (payment == null) {
            log.warn("支付记录不存在: paymentOrderNo={}", paymentOrderNo);
            return false;
        }
        
        payment.setPaymentStatus(status);
        payment.setThirdPartyOrderNo(thirdPartyOrderNo);
        payment.setUpdatedTime(LocalDateTime.now());
        
        if ("SUCCESS".equals(status) || "success".equalsIgnoreCase(status)) {
            payment.setPaymentTime(LocalDateTime.now());
            payment.setCompletedTime(LocalDateTime.now());
        }
        
        boolean result = updateById(payment);
        if (result) {
            log.info("更新支付状态成功: paymentOrderNo={}, status={}", paymentOrderNo, status);
        } else {
            log.error("更新支付状态失败: paymentOrderNo={}, status={}", paymentOrderNo, status);
        }
        
        return result;
    }

    @Override
    public Payment getById(Long paymentId) {
        if (paymentId == null) {
            return null;
        }
        return super.getById(paymentId);
    }

    @Override
    public boolean save(Payment payment) {
        return super.save(payment);
    }

    /**
     * 生成支付订单号
     */
    private String generatePaymentOrderNo() {
        return "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}