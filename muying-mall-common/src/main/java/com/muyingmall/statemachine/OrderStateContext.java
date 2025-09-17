package com.muyingmall.statemachine;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单状态机上下文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStateContext {
    
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
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人类型 (USER-用户, ADMIN-管理员, SYSTEM-系统)
     */
    private String operatorType;
    
    /**
     * 事件触发时间
     */
    private LocalDateTime eventTime;
    
    /**
     * 事件描述
     */
    private String eventDescription;
    
    /**
     * 扩展参数
     */
    private String extParams;
    
    /**
     * 备注
     */
    private String remark;

    public OrderStateContext(Long orderId, String orderNo, Long userId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.eventTime = LocalDateTime.now();
    }

    public OrderStateContext(Long orderId, String orderNo, Long userId, Long operatorId, String operatorType) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.eventTime = LocalDateTime.now();
    }
}