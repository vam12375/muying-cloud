package com.muyingmall.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单支付事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaidEvent {
    
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
     * 支付订单号
     */
    private String paymentOrderNo;
    
    /**
     * 支付金额
     */
    private BigDecimal paidAmount;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 支付时间
     */
    private LocalDateTime paidTime;
    
    /**
     * 第三方支付流水号
     */
    private String thirdPartyOrderNo;
}