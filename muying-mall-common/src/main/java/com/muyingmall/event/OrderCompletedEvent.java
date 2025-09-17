package com.muyingmall.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单完成事件
 */
@Data
@NoArgsConstructor
public class OrderCompletedEvent {
    
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
    private BigDecimal orderAmount;
    
    /**
     * 实际支付金额
     */
    private BigDecimal paidAmount;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedTime;
    
    /**
     * 是否使用优惠券
     */
    private Boolean usedCoupon;
    
    /**
     * 优惠券ID
     */
    private Long couponId;
    
    /**
     * 是否使用积分
     */
    private Boolean usedPoints;
    
    /**
     * 使用的积分数量
     */
    private BigDecimal pointsUsed;
    
    /**
     * 全参数构造器
     */
    public OrderCompletedEvent(Long orderId, String orderNo, Long userId, BigDecimal orderAmount, 
                              BigDecimal paidAmount, LocalDateTime completedTime, Boolean usedCoupon, 
                              Long couponId, Boolean usedPoints, BigDecimal pointsUsed) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.orderAmount = orderAmount;
        this.paidAmount = paidAmount;
        this.completedTime = completedTime;
        this.usedCoupon = usedCoupon;
        this.couponId = couponId;
        this.usedPoints = usedPoints;
        this.pointsUsed = pointsUsed;
    }
    
    /**
     * 兼容构造器（为了兼容OrderServiceImpl的调用）
     */
    public OrderCompletedEvent(Integer orderId, Integer userId, BigDecimal amount, String orderNo) {
        this.orderId = orderId.longValue();
        this.userId = userId.longValue();
        this.orderAmount = amount;
        this.paidAmount = amount;
        this.orderNo = orderNo;
        this.completedTime = LocalDateTime.now();
        this.usedCoupon = false;
        this.usedPoints = false;
        this.pointsUsed = BigDecimal.ZERO;
    }
}