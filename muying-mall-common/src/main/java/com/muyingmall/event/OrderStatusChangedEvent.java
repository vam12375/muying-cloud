package com.muyingmall.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.muyingmall.common.enums.OrderStatus;

import java.time.LocalDateTime;

/**
 * 订单状态变更事件
 */
@Data
@NoArgsConstructor
public class OrderStatusChangedEvent {
    
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
     * 原状态
     */
    private OrderStatus oldStatus;
    
    /**
     * 新状态
     */
    private OrderStatus newStatus;
    
    /**
     * 变更时间
     */
    private LocalDateTime changeTime;
    
    /**
     * 变更原因
     */
    private String changeReason;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人类型 (USER-用户, ADMIN-管理员, SYSTEM-系统)
     */
    private String operatorType;

    /**
     * 全参数构造器
     */
    public OrderStatusChangedEvent(Long orderId, String orderNo, Long userId, OrderStatus oldStatus, 
                                   OrderStatus newStatus, LocalDateTime changeTime, String changeReason, 
                                   Long operatorId, String operatorType) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changeTime = changeTime;
        this.changeReason = changeReason;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
    }
    
    public OrderStatusChangedEvent(Long orderId, String orderNo, Long userId, OrderStatus oldStatus, OrderStatus newStatus) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changeTime = LocalDateTime.now();
    }
    
    /**
     * 兼容构造器（为了兼容OrderServiceImpl的调用）
     */
    public OrderStatusChangedEvent(Object source, Integer userId, Integer orderId, 
                                   String orderNo, String oldStatus, String newStatus, String extra) {
        this.orderId = orderId.longValue();
        this.orderNo = orderNo;
        this.userId = userId.longValue();
        // 将String状态转为OrderStatus枚举
        try {
            this.oldStatus = OrderStatus.fromCode(oldStatus.toUpperCase());
        } catch (Exception e) {
            this.oldStatus = OrderStatus.PENDING_PAYMENT; // 默认值
        }
        try {
            this.newStatus = OrderStatus.fromCode(newStatus.toUpperCase());
        } catch (Exception e) {
            this.newStatus = OrderStatus.PENDING_PAYMENT; // 默认值
        }
        this.changeTime = LocalDateTime.now();
        this.changeReason = extra;
        this.operatorType = "SYSTEM";
    }
}