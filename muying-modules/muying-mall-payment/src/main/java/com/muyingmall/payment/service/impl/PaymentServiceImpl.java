package com.muyingmall.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.payment.entity.Payment;
import com.muyingmall.payment.mapper.PaymentMapper;
import com.muyingmall.payment.service.PaymentService;
import com.muyingmall.payment.service.MessageProducerService;
import com.muyingmall.payment.dto.PaymentMessage;
import com.muyingmall.common.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Autowired(required = false)
    private MessageProducerService messageProducerService;

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getPaymentNo() == null || payment.getPaymentNo().isEmpty()) {
            payment.setPaymentNo(generatePaymentNo());
        }
        payment.setCreateTime(LocalDateTime.now());
        payment.setUpdateTime(LocalDateTime.now());
        save(payment);
        return payment;
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return lambdaQuery()
                .eq(Payment::getOrderId, orderId)
                .one();
    }

    @Override
    public Payment getPaymentByPaymentNo(String paymentNo) {
        return lambdaQuery()
                .eq(Payment::getPaymentNo, paymentNo)
                .one();
    }

    @Override
    public void updatePaymentStatus(String paymentNo, PaymentStatus status) {
        lambdaUpdate()
                .eq(Payment::getPaymentNo, paymentNo)
                .set(Payment::getStatus, status)
                .set(Payment::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public boolean processPaymentSuccess(String paymentNo, String transactionId) {
        return lambdaUpdate()
                .eq(Payment::getPaymentNo, paymentNo)
                .set(Payment::getStatus, PaymentStatus.SUCCESS)
                .set(Payment::getTransactionId, transactionId)
                .set(Payment::getPaymentTime, LocalDateTime.now())
                .set(Payment::getUpdateTime, LocalDateTime.now())
                .update();
    }

    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}