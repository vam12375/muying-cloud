package com.muyingmall.order.mq;

import com.muyingmall.common.rabbitmq.message.OrderMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单消息消费者
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Slf4j
@Component
public class OrderMessageConsumer {
    
    /**
     * 处理订单超时消息
     */
    @RabbitListener(queues = "order.timeout.queue")
    public void handleOrderTimeout(OrderMessage message, Channel channel, 
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("接收到订单超时检查消息: orderId={}, orderNo={}", 
                message.getOrderId(), message.getOrderNo());
        
        try {
            // 检查订单状态
            checkAndCancelTimeoutOrder(message.getOrderId());
            
            // 手动确认消息
            channel.basicAck(tag, false);
            log.info("订单超时消息处理成功: orderId={}", message.getOrderId());
            
        } catch (BusinessException e) {
            // 业务异常，不重试，直接确认
            try {
                channel.basicAck(tag, false);
                log.error("订单超时处理业务异常: orderId={}, error={}", 
                        message.getOrderId(), e.getMessage());
            } catch (IOException ioException) {
                log.error("消息确认失败", ioException);
            }
        } catch (Exception e) {
            // 系统异常，拒绝消息，进行重试
            try {
                // 第三个参数true表示重新入队
                channel.basicNack(tag, false, true);
                log.error("订单超时处理失败，将重试: orderId={}", message.getOrderId(), e);
            } catch (IOException ioException) {
                log.error("消息拒绝失败", ioException);
            }
        }
    }
    
    /**
     * 处理支付成功消息
     */
    @RabbitListener(queues = "payment.success.queue")
    public void handlePaymentSuccess(OrderMessage message, Channel channel, 
                                    @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("接收到支付成功消息: orderId={}, orderNo={}", 
                message.getOrderId(), message.getOrderNo());
        
        try {
            // 更新订单状态为已支付
            updateOrderPaymentStatus(message.getOrderId());
            
            // 手动确认消息
            channel.basicAck(tag, false);
            log.info("支付成功消息处理成功: orderId={}", message.getOrderId());
            
        } catch (Exception e) {
            handleMessageError(channel, tag, message.getOrderId(), e);
        }
    }
    
    /**
     * 处理物流状态更新消息
     */
    @RabbitListener(queues = "logistics.status.queue")
    public void handleLogisticsStatusUpdate(OrderMessage message, Channel channel, 
                                           @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("接收到物流状态更新消息: orderId={}, status={}", 
                message.getOrderId(), message.getStatus());
        
        try {
            // 更新订单物流状态
            updateOrderLogisticsStatus(message.getOrderId(), message.getStatus());
            
            // 手动确认消息
            channel.basicAck(tag, false);
            log.info("物流状态更新消息处理成功: orderId={}", message.getOrderId());
            
        } catch (Exception e) {
            handleMessageError(channel, tag, message.getOrderId(), e);
        }
    }
    
    /**
     * 处理死信队列消息
     */
    @RabbitListener(queues = "dlx.queue")
    public void handleDeadLetter(OrderMessage message, Channel channel, 
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.error("接收到死信消息: messageId={}, eventType={}, source={}", 
                message.getMessageId(), message.getEventType(), message.getSource());
        
        try {
            // 记录死信消息到数据库
            saveDeadLetterMessage(message);
            
            // 发送告警通知
            sendAlertNotification(message);
            
            // 确认消息
            channel.basicAck(tag, false);
            
        } catch (IOException e) {
            log.error("死信消息处理失败", e);
        }
    }
    
    /**
     * 检查并取消超时订单
     */
    private void checkAndCancelTimeoutOrder(Long orderId) {
        // TODO: 实现订单超时取消逻辑
        // 1. 查询订单状态
        // 2. 如果未支付，则取消订单
        // 3. 释放库存
        // 4. 发送订单取消通知
        log.info("检查并取消超时订单: orderId={}", orderId);
    }
    
    /**
     * 更新订单支付状态
     */
    private void updateOrderPaymentStatus(Long orderId) {
        // TODO: 实现更新订单支付状态逻辑
        log.info("更新订单支付状态: orderId={}", orderId);
    }
    
    /**
     * 更新订单物流状态
     */
    private void updateOrderLogisticsStatus(Long orderId, Integer status) {
        // TODO: 实现更新订单物流状态逻辑
        log.info("更新订单物流状态: orderId={}, status={}", orderId, status);
    }
    
    /**
     * 保存死信消息
     */
    private void saveDeadLetterMessage(OrderMessage message) {
        // TODO: 实现保存死信消息到数据库逻辑
        log.warn("保存死信消息: messageId={}", message.getMessageId());
    }
    
    /**
     * 发送告警通知
     */
    private void sendAlertNotification(OrderMessage message) {
        // TODO: 实现发送告警通知逻辑（邮件、短信、钉钉等）
        log.warn("发送死信消息告警: messageId={}, eventType={}", 
                message.getMessageId(), message.getEventType());
    }
    
    /**
     * 统一的消息错误处理
     */
    private void handleMessageError(Channel channel, long tag, Long orderId, Exception e) {
        if (e instanceof BusinessException) {
            // 业务异常，不重试
            try {
                channel.basicAck(tag, false);
                log.error("消息处理业务异常: orderId={}, error={}", orderId, e.getMessage());
            } catch (IOException ioException) {
                log.error("消息确认失败", ioException);
            }
        } else {
            // 系统异常，重试
            try {
                channel.basicNack(tag, false, true);
                log.error("消息处理失败，将重试: orderId={}", orderId, e);
            } catch (IOException ioException) {
                log.error("消息拒绝失败", ioException);
            }
        }
    }
    
    /**
     * 自定义业务异常类
     */
    private static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
        
        public BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}