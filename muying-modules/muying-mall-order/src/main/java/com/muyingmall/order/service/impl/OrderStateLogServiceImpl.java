package com.muyingmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.order.entity.OrderStateLog;
import com.muyingmall.order.mapper.OrderStateLogMapper;
import com.muyingmall.order.service.OrderStateLogService;
import com.muyingmall.statemachine.OrderStateContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单状态变更日志服务实现类
 */
@Service
@Slf4j
public class OrderStateLogServiceImpl extends ServiceImpl<OrderStateLogMapper, OrderStateLog> implements OrderStateLogService {

    @Override
    public OrderStateLog recordStateChange(OrderStateContext context) {
        if (context == null || context.getOrderId() == null) {
            return null;
        }

        OrderStateLog stateLog = new OrderStateLog();
        stateLog.setOrderId(context.getOrderId().intValue());
        stateLog.setOrderNo(context.getOrderNo());
        stateLog.setOperator(context.getOperatorType() != null ? context.getOperatorType() : "SYSTEM");
        stateLog.setReason(context.getEventDescription() != null ? context.getEventDescription() : "");
        stateLog.setCreateTime(LocalDateTime.now());

        boolean result = save(stateLog);
        if (result) {
            log.info("记录订单状态变更日志成功: orderId={}, orderNo={}", 
                    context.getOrderId(), context.getOrderNo());
            return stateLog;
        } else {
            log.error("记录订单状态变更日志失败: orderId={}, orderNo={}", 
                    context.getOrderId(), context.getOrderNo());
            return null;
        }
    }

    @Override
    public List<OrderStateLog> getOrderStateHistory(Integer orderId) {
        if (orderId == null) {
            return List.of();
        }

        LambdaQueryWrapper<OrderStateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderStateLog::getOrderId, orderId)
                .orderByDesc(OrderStateLog::getCreateTime);

        List<OrderStateLog> logs = list(queryWrapper);
        log.debug("查询订单状态变更历史: orderId={}, count={}", orderId, logs.size());
        return logs;
    }
}