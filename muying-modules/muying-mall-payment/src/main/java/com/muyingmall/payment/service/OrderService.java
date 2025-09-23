package com.muyingmall.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muyingmall.payment.entity.Order;

public interface OrderService extends IService<Order> {
    Order findByOrderNumber(String orderNumber);
    boolean updateOrderStatus(Long orderId, String status);
    boolean updatePaymentTime(Long orderId);
    Order createOrder(Long userId, String orderNumber, java.math.BigDecimal totalAmount);
}