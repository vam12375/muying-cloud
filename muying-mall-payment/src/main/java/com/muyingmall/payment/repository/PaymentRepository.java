package com.muyingmall.payment.repository;

import com.muyingmall.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentNo(String paymentNo);
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByOrderNo(String orderNo);
    Optional<Payment> findByThirdPartyTransactionId(String thirdPartyTransactionId);
}