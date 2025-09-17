package com.muyingmall.order.service.impl;

import com.muyingmall.common.enums.OrderStatus;
import com.muyingmall.order.entity.Order;
import com.muyingmall.order.service.OrderService;
import com.muyingmall.order.service.OrderStateService;
import com.muyingmall.statemachine.OrderEvent;
import com.muyingmall.statemachine.OrderStateContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 订单状态服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStateServiceImpl implements OrderStateService {

    private final OrderService orderService;

    @Override
    public Order sendEvent(Integer orderId, OrderEvent event, String operator, String reason) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            log.warn("订单不存在: orderId={}", orderId);
            return null;
        }
        return sendEvent(order, event, operator, reason);
    }

    @Override
    public Order sendEvent(Order order, OrderEvent event, String operator, String reason) {
        if (order == null || event == null) {
            return null;
        }

        OrderStateContext context = new OrderStateContext();
        context.setOrderId(order.getOrderId().longValue());
        context.setOrderNo(order.getOrderNo());
        context.setUserId(order.getUserId().longValue());
        context.setOperatorType(operator != null ? operator : "SYSTEM");
        context.setEventDescription(reason != null ? reason : "");
        context.setEventTime(LocalDateTime.now());

        return sendEvent(context);
    }

    @Override
    public Order sendEvent(OrderStateContext context) {
        if (context == null || context.getOrderId() == null) {
            return null;
        }

        Order order = orderService.getById(context.getOrderId().intValue());
        if (order == null) {
            log.warn("订单不存在: orderId={}", context.getOrderId());
            return null;
        }

        // 简单的状态转换逻辑，实际应用中会更复杂
        // 这里只是记录日志并返回订单
        log.info("处理订单状态转换事件: orderId={}, operator={}, description={}", 
                context.getOrderId(), context.getOperatorType(), context.getEventDescription());

        return order;
    }

    @Override
    public boolean canTransit(OrderStatus currentStatus, OrderStatus targetStatus) {
        if (currentStatus == null || targetStatus == null) {
            return false;
        }

        // 简化版本，允许所有状态转换
        // 实际应用中应该有更严格的状态机逻辑
        return true;
    }

    @Override
    public OrderStatus[] getPossibleNextStates(OrderStatus currentStatus) {
        if (currentStatus == null) {
            return new OrderStatus[0];
        }

        // 根据当前状态返回可能的下一个状态
        switch (currentStatus) {
            case PENDING_PAYMENT:
                return new OrderStatus[]{OrderStatus.PAID, OrderStatus.CANCELLED};
            case PAID:
                return new OrderStatus[]{OrderStatus.PENDING_SHIPMENT, OrderStatus.CANCELLED, OrderStatus.REFUNDING};
            case PENDING_SHIPMENT:
                return new OrderStatus[]{OrderStatus.SHIPPED, OrderStatus.CANCELLED, OrderStatus.REFUNDING};
            case SHIPPED:
                return new OrderStatus[]{OrderStatus.PENDING_RECEIPT, OrderStatus.RETURNING};
            case PENDING_RECEIPT:
                return new OrderStatus[]{OrderStatus.COMPLETED, OrderStatus.RETURNING};
            case COMPLETED:
                return new OrderStatus[]{OrderStatus.RETURNING};
            case REFUNDING:
                return new OrderStatus[]{OrderStatus.REFUNDED, OrderStatus.CANCELLED};
            case RETURNING:
                return new OrderStatus[]{OrderStatus.RETURNED, OrderStatus.CANCELLED};
            default:
                return new OrderStatus[0];
        }
    }

    /**
     * 根据当前状态和事件获取目标状态
     * 简化版本，只做示例
     */
    private OrderStatus getTargetStatus(OrderStatus currentStatus, OrderEvent event) {
        if (currentStatus == null || event == null) {
            return null;
        }

        // 简化版本，直接返回当前状态
        return currentStatus;
    }
}