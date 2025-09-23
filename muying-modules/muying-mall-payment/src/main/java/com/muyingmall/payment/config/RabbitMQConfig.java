package com.muyingmall.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 定义支付相关的交换机、队列和绑定关系
 * 
 * @author MuyingMall
 * @since 2025-09-18
 */
@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    // 交换机名称
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // 队列名称
    public static final String PAYMENT_SUCCESS_QUEUE = "payment.success.queue";
    public static final String PAYMENT_FAILED_QUEUE = "payment.failed.queue";
    public static final String PAYMENT_REFUND_QUEUE = "payment.refund.queue";
    public static final String PAYMENT_REQUEST_QUEUE = "payment.request.queue";

    // 路由键
    public static final String PAYMENT_SUCCESS_KEY = "payment.success";
    public static final String PAYMENT_FAILED_KEY = "payment.failed";
    public static final String PAYMENT_REFUND_KEY = "payment.refund";
    public static final String PAYMENT_REQUEST_KEY = "payment.request";

    // 死信队列配置
    public static final String PAYMENT_DLX_EXCHANGE = "payment.dlx.exchange";
    public static final String PAYMENT_DLX_QUEUE = "payment.dlx.queue";
    public static final String PAYMENT_DLX_KEY = "payment.dlx";

    /**
     * 支付交换机
     */
    @Bean
    public TopicExchange paymentExchange() {
        return ExchangeBuilder
                .topicExchange(PAYMENT_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 支付成功队列
     */
    @Bean
    public Queue paymentSuccessQueue() {
        return QueueBuilder
                .durable(PAYMENT_SUCCESS_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENT_DLX_KEY)
                .withArgument("x-message-ttl", 1800000) // 30分钟TTL
                .build();
    }

    /**
     * 支付失败队列
     */
    @Bean
    public Queue paymentFailedQueue() {
        return QueueBuilder
                .durable(PAYMENT_FAILED_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENT_DLX_KEY)
                .withArgument("x-message-ttl", 1800000) // 30分钟TTL
                .build();
    }

    /**
     * 支付退款队列
     */
    @Bean
    public Queue paymentRefundQueue() {
        return QueueBuilder
                .durable(PAYMENT_REFUND_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENT_DLX_KEY)
                .withArgument("x-message-ttl", 1800000) // 30分钟TTL
                .build();
    }

    /**
     * 支付请求队列
     */
    @Bean
    public Queue paymentRequestQueue() {
        return QueueBuilder
                .durable(PAYMENT_REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENT_DLX_KEY)
                .withArgument("x-message-ttl", 1800000) // 30分钟TTL
                .build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange paymentDlxExchange() {
        return ExchangeBuilder
                .directExchange(PAYMENT_DLX_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue paymentDlxQueue() {
        return QueueBuilder
                .durable(PAYMENT_DLX_QUEUE)
                .build();
    }

    /**
     * 绑定支付成功队列到交换机
     */
    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(PAYMENT_SUCCESS_KEY);
    }

    /**
     * 绑定支付失败队列到交换机
     */
    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(PAYMENT_FAILED_KEY);
    }

    /**
     * 绑定支付退款队列到交换机
     */
    @Bean
    public Binding paymentRefundBinding() {
        return BindingBuilder
                .bind(paymentRefundQueue())
                .to(paymentExchange())
                .with(PAYMENT_REFUND_KEY);
    }

    /**
     * 绑定支付请求队列到交换机
     */
    @Bean
    public Binding paymentRequestBinding() {
        return BindingBuilder
                .bind(paymentRequestQueue())
                .to(paymentExchange())
                .with(PAYMENT_REQUEST_KEY);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding paymentDlxBinding() {
        return BindingBuilder
                .bind(paymentDlxQueue())
                .to(paymentDlxExchange())
                .with(PAYMENT_DLX_KEY);
    }
}