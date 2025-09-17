package com.muyingmall.payment.service;

public interface CacheRefreshService {
    void refreshPaymentCache(Long paymentId);
    void refreshOrderCache(Long orderId);
    void refreshUserCache(Long userId);
    void clearAllCache();
}