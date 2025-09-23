package com.muyingmall.payment.service;

public interface OrderNotificationService {
    void notifyPaymentSuccess(Long orderId, Long userId);
    void notifyPaymentFailed(Long orderId, Long userId, String reason);
    void notifyRefundProcessed(Long orderId, Long userId);
    void sendSMS(String phone, String message);
    void sendEmail(String email, String subject, String content);
}