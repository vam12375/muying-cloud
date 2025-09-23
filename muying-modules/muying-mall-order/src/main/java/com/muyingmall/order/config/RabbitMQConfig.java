package com.muyingmall.order.config;

import com.muyingmall.common.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单服务RabbitMQ配置
 * 声明订单相关的交换机、队列和绑定关系
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 交换机声明 ====================
    
    /**
     * 订单事件交换机
     */
    @Bean
    public TopicExchange orderEventExchange() {
        return ExchangeBuilder
                .topicExchange(RabbitMQConstants.ORDER_EVENT_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 订单延时交换机（用于订单超时取消）
     */
    @Bean
    public DirectExchange orderDelayExchange() {
        return ExchangeBuilder
                .directExchange(RabbitMQConstants.ORDER_DELAY_EXCHANGE)
                .durable(true)
                .build();
    }

    // ==================== 队列声明 ====================
    
    /**
     * 订单创建队列
     */
    @Bean
    public Queue orderCreateQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }

    /**
     * 订单支付队列
     */
    @Bean
    public Queue orderPayQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_PAY_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }
    
    /**
     * 订单取消队列
     */
    @Bean
    public Queue orderCancelQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_CANCEL_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }
    
    /**
     * 订单完成队列
     */
    @Bean
    public Queue orderCompleteQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_COMPLETE_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }

    /**
     * 订单延时队列（用于订单超时取消）
     */
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_DELAY_QUEUE)
                .withArgument("x-message-ttl", RabbitMQConstants.ORDER_TTL) // 30分钟TTL
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.ORDER_EVENT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.ORDER_CANCEL_ROUTING_KEY)
                .build();
    }

    // ==================== 绑定关系声明 ====================
    
    /**
     * 订单创建队列绑定
     */
    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
                .bind(orderCreateQueue())
                .to(orderEventExchange())
                .with(RabbitMQConstants.ORDER_CREATE_ROUTING_KEY);
    }

    /**
     * 订单支付队列绑定
     */
    @Bean
    public Binding orderPayBinding() {
        return BindingBuilder
                .bind(orderPayQueue())
                .to(orderEventExchange())
                .with(RabbitMQConstants.ORDER_PAY_ROUTING_KEY);
    }
    
    /**
     * 订单取消队列绑定
     */
    @Bean
    public Binding orderCancelBinding() {
        return BindingBuilder
                .bind(orderCancelQueue())
                .to(orderEventExchange())
                .with(RabbitMQConstants.ORDER_CANCEL_ROUTING_KEY);
    }
    
    /**
     * 订单完成队列绑定
     */
    @Bean
    public Binding orderCompleteBinding() {
        return BindingBuilder
                .bind(orderCompleteQueue())
                .to(orderEventExchange())
                .with(RabbitMQConstants.ORDER_COMPLETE_ROUTING_KEY);
    }

    /**
     * 订单延时队列绑定
     */
    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder
                .bind(orderDelayQueue())
                .to(orderDelayExchange())
                .with(RabbitMQConstants.ORDER_DELAY_ROUTING_KEY);
    }

    // ==================== 跨服务队列监听 ====================

    /**
     * 监听支付成功事件（由支付服务发布）
     */
    @Bean
    public Queue paymentSuccessToOrderQueue() {
        return QueueBuilder
                .durable("order-payment-success-queue")
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定支付成功事件到订单服务队列
     */
    @Bean
    public Binding paymentSuccessToOrderBinding() {
        return BindingBuilder
                .bind(paymentSuccessToOrderQueue())
                .to(new TopicExchange(RabbitMQConstants.PAYMENT_EVENT_EXCHANGE))
                .with(RabbitMQConstants.PAYMENT_SUCCESS_ROUTING_KEY);
    }

    // ==================== 订单消费者队列声明 ====================

    /**
     * 订单超时队列
     */
    @Bean
    public Queue orderTimeoutQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.ORDER_TIMEOUT_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 支付成功通知队列
     */
    @Bean
    public Queue paymentSuccessNotificationQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.PAYMENT_SUCCESS_NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 物流状态更新队列
     */
    @Bean
    public Queue logisticsStatusQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.LOGISTICS_STATUS_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 物流事件交换机
     */
    @Bean
    public TopicExchange logisticsEventExchange() {
        return ExchangeBuilder
                .topicExchange("logistics-event-exchange")
                .durable(true)
                .build();
    }

    /**
     * 物流状态队列绑定
     */
    @Bean
    public Binding logisticsStatusBinding() {
        return BindingBuilder
                .bind(logisticsStatusQueue())
                .to(logisticsEventExchange())
                .with("logistics.status.update");
    }

    /**
     * 订单超时队列绑定到延时交换机
     */
    @Bean
    public Binding orderTimeoutBinding() {
        return BindingBuilder
                .bind(orderTimeoutQueue())
                .to(orderDelayExchange())
                .with("order.timeout.check");
    }

    /**
     * 支付成功通知队列绑定
     */
    @Bean
    public Binding paymentSuccessNotificationBinding() {
        return BindingBuilder
                .bind(paymentSuccessNotificationQueue())
                .to(new TopicExchange(RabbitMQConstants.PAYMENT_EVENT_EXCHANGE))
                .with("payment.success.notification");
    }
}