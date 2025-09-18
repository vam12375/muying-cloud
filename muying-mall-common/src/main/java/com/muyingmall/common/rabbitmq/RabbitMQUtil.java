package com.muyingmall.common.rabbitmq;

import com.muyingmall.common.rabbitmq.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * RabbitMQ工具类
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Slf4j
@Component
public class RabbitMQUtil {
    
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送消息
     * 
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息对象
     */
    public void sendMessage(String exchange, String routingKey, BaseMessage message) {
        if (rabbitTemplate == null) {
            log.warn("RabbitTemplate未配置，消息发送失败");
            return;
        }
        
        try {
            // 设置消息ID
            if (message.getMessageId() == null) {
                message.setMessageId(UUID.randomUUID().toString());
            }
            
            // 设置时间戳
            if (message.getTimestamp() == null) {
                message.setTimestamp(new java.util.Date());
            }
            
            // 创建关联数据
            CorrelationData correlationData = new CorrelationData(message.getMessageId());
            
            // 发送消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                // 设置消息持久化
                msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                msg.getMessageProperties().setMessageId(message.getMessageId());
                msg.getMessageProperties().setCorrelationId(message.getMessageId());
                
                // 设置消息优先级（0-9）
                if (message.getMetadata("priority") != null) {
                    msg.getMessageProperties().setPriority((Integer) message.getMetadata("priority"));
                }
                
                return msg;
            }, correlationData);
            
            log.info("消息发送成功 - Exchange: {}, RoutingKey: {}, MessageId: {}", 
                    exchange, routingKey, message.getMessageId());
            
        } catch (Exception e) {
            log.error("消息发送失败 - Exchange: {}, RoutingKey: {}, Message: {}", 
                    exchange, routingKey, message, e);
            throw new RuntimeException("消息发送失败", e);
        }
    }
    
    /**
     * 发送延迟消息
     * 
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息对象
     * @param delayMillis 延迟时间（毫秒）
     */
    public void sendDelayMessage(String exchange, String routingKey, BaseMessage message, long delayMillis) {
        if (rabbitTemplate == null) {
            log.warn("RabbitTemplate未配置，消息发送失败");
            return;
        }
        
        try {
            // 设置消息ID
            if (message.getMessageId() == null) {
                message.setMessageId(UUID.randomUUID().toString());
            }
            
            // 创建关联数据
            CorrelationData correlationData = new CorrelationData(message.getMessageId());
            
            // 发送延迟消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                // 设置消息持久化
                msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                msg.getMessageProperties().setMessageId(message.getMessageId());
                // 设置延迟时间
                msg.getMessageProperties().setDelay((int) delayMillis);
                return msg;
            }, correlationData);
            
            log.info("延迟消息发送成功 - Exchange: {}, RoutingKey: {}, MessageId: {}, Delay: {}ms", 
                    exchange, routingKey, message.getMessageId(), delayMillis);
            
        } catch (Exception e) {
            log.error("延迟消息发送失败 - Exchange: {}, RoutingKey: {}, Message: {}", 
                    exchange, routingKey, message, e);
            throw new RuntimeException("延迟消息发送失败", e);
        }
    }
    
    /**
     * 发送带优先级的消息
     * 
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息对象
     * @param priority 优先级（0-9，数字越大优先级越高）
     */
    public void sendPriorityMessage(String exchange, String routingKey, BaseMessage message, int priority) {
        if (priority < 0 || priority > 9) {
            throw new IllegalArgumentException("优先级必须在0-9之间");
        }
        
        message.addMetadata("priority", priority);
        sendMessage(exchange, routingKey, message);
    }
    
    /**
     * 批量发送消息
     * 
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param messages 消息列表
     */
    public void sendBatchMessages(String exchange, String routingKey, java.util.List<? extends BaseMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        for (BaseMessage message : messages) {
            sendMessage(exchange, routingKey, message);
        }
    }
    
    /**
     * 获取消息ID
     */
    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 构建死信路由键
     */
    public static String buildDeadLetterRoutingKey(String originalRoutingKey) {
        return "dlx." + originalRoutingKey;
    }
}