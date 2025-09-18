package com.muyingmall.order.mq;

import com.muyingmall.common.rabbitmq.RabbitMQUtil;
import com.muyingmall.common.rabbitmq.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 订单消息生产者
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Slf4j
@Component
public class OrderMessageProducer {
    
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    
    /**
     * 发送订单创建消息
     */
    public void sendOrderCreateMessage(Long orderId, String orderNo, Long userId, BigDecimal amount) {
        try {
            OrderMessage message = OrderMessage.createOrderCreateMessage(orderId, orderNo, userId, amount);
            message.setMessageId(UUID.randomUUID().toString());
            
            // 发送到订单交换机
            rabbitMQUtil.sendMessage("order.exchange", "order.create", message);
            
            log.info("订单创建消息发送成功: orderId={}, orderNo={}, userId={}, amount={}", 
                    orderId, orderNo, userId, amount);
            
        } catch (Exception e) {
            log.error("订单创建消息发送失败: orderId={}", orderId, e);
            // 可以将失败的消息保存到数据库，后续重试
            saveFailedMessage(orderId, "ORDER_CREATE", e.getMessage());
        }
    }
    
    /**
     * 发送订单取消消息
     */
    public void sendOrderCancelMessage(Long orderId, String orderNo, Long userId) {
        try {
            OrderMessage message = OrderMessage.createOrderCancelMessage(orderId, orderNo, userId);
            message.setMessageId(UUID.randomUUID().toString());
            
            // 发送到订单交换机
            rabbitMQUtil.sendMessage("order.exchange", "order.cancel", message);
            
            log.info("订单取消消息发送成功: orderId={}, orderNo={}, userId={}", 
                    orderId, orderNo, userId);
            
        } catch (Exception e) {
            log.error("订单取消消息发送失败: orderId={}", orderId, e);
            saveFailedMessage(orderId, "ORDER_CANCEL", e.getMessage());
        }
    }
    
    /**
     * 发送订单完成消息
     */
    public void sendOrderCompleteMessage(Long orderId, String orderNo, Long userId, BigDecimal amount) {
        try {
            OrderMessage message = OrderMessage.createOrderCompleteMessage(orderId, orderNo, userId, amount);
            message.setMessageId(UUID.randomUUID().toString());
            
            // 发送到订单交换机
            rabbitMQUtil.sendMessage("order.exchange", "order.complete", message);
            
            log.info("订单完成消息发送成功: orderId={}, orderNo={}, userId={}, amount={}", 
                    orderId, orderNo, userId, amount);
            
        } catch (Exception e) {
            log.error("订单完成消息发送失败: orderId={}", orderId, e);
            saveFailedMessage(orderId, "ORDER_COMPLETE", e.getMessage());
        }
    }
    
    /**
     * 发送订单超时检查消息（延迟消息）
     * 
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param delayMinutes 延迟分钟数
     */
    public void sendOrderTimeoutMessage(Long orderId, String orderNo, int delayMinutes) {
        try {
            OrderMessage message = new OrderMessage();
            message.setOrderId(orderId);
            message.setOrderNo(orderNo);
            message.setEventType("ORDER_TIMEOUT_CHECK");
            message.setSource("order-service");
            message.setMessageId(UUID.randomUUID().toString());
            
            // 转换为毫秒
            long delayMillis = delayMinutes * 60 * 1000L;
            
            // 发送延迟消息
            rabbitMQUtil.sendDelayMessage("order.exchange", "order.timeout", message, delayMillis);
            
            log.info("订单超时检查消息发送成功: orderId={}, orderNo={}, delayMinutes={}", 
                    orderId, orderNo, delayMinutes);
            
        } catch (Exception e) {
            log.error("订单超时检查消息发送失败: orderId={}", orderId, e);
        }
    }
    
    /**
     * 保存发送失败的消息（用于后续重试）
     */
    private void saveFailedMessage(Long orderId, String messageType, String errorMsg) {
        // TODO: 实现将失败消息保存到数据库的逻辑
        // 可以创建一个message_failed表，记录失败的消息信息
        // 后续通过定时任务重试发送
        log.warn("保存失败消息到数据库: orderId={}, messageType={}, error={}", 
                orderId, messageType, errorMsg);
    }
}