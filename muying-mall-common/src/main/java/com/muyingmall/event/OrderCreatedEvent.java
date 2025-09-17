package com.muyingmall.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单创建事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 支付超时时间
     */
    private LocalDateTime paymentTimeout;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 收货人
     */
    private String receiverName;
    
    /**
     * 收货电话
     */
    private String receiverPhone;
}