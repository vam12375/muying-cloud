package com.muyingmall.common.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * RabbitMQ基础配置类
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private Integer port;

    @Value("${spring.rabbitmq.username:admin}")
    private String username;

    @Value("${spring.rabbitmq.password:admin123}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host:/muying}")
    private String virtualHost;

    /**
     * 配置连接工厂
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        
        // 开启消息确认
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        
        // 连接池配置
        connectionFactory.setConnectionCacheSize(5);
        connectionFactory.setChannelCacheSize(50);
        
        log.info("RabbitMQ连接工厂配置完成: {}:{}", host, port);
        return connectionFactory;
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        
        // 设置消息发送确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送成功: correlationData={}", correlationData);
            } else {
                log.error("消息发送失败: correlationData={}, cause={}", correlationData, cause);
            }
        });
        
        // 设置消息返回确认
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由失败: message={}, replyCode={}, replyText={}, exchange={}, routingKey={}",
                    returned.getMessage(), returned.getReplyCode(), returned.getReplyText(),
                    returned.getExchange(), returned.getRoutingKey());
        });
        
        // 设置重试模板
        rabbitTemplate.setRetryTemplate(retryTemplate());
        
        // 强制消息确认
        rabbitTemplate.setMandatory(true);
        
        return rabbitTemplate;
    }

    /**
     * 配置重试模板
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        // 指数退避策略
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(10000);
        
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(new org.springframework.retry.policy.SimpleRetryPolicy(3));
        
        return retryTemplate;
    }

    /**
     * 配置消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * 配置消费者容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        
        // 设置并发消费者数量
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        
        // 设置预取数量
        factory.setPrefetchCount(1);
        
        // 设置手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        
        // 设置消费失败重试
        factory.setDefaultRequeueRejected(true);
        
        return factory;
    }

    // ===================== 交换机定义 =====================

    /**
     * 订单交换机
     */
    @Bean
    public TopicExchange orderExchange() {
        return ExchangeBuilder
                .topicExchange("order.exchange")
                .durable(true)
                .build();
    }

    /**
     * 支付交换机
     */
    @Bean
    public TopicExchange paymentExchange() {
        return ExchangeBuilder
                .topicExchange("payment.exchange")
                .durable(true)
                .build();
    }

    /**
     * 商品交换机
     */
    @Bean
    public TopicExchange productExchange() {
        return ExchangeBuilder
                .topicExchange("product.exchange")
                .durable(true)
                .build();
    }

    /**
     * 用户交换机
     */
    @Bean
    public TopicExchange userExchange() {
        return ExchangeBuilder
                .topicExchange("user.exchange")
                .durable(true)
                .build();
    }

    /**
     * 物流交换机
     */
    @Bean
    public TopicExchange logisticsExchange() {
        return ExchangeBuilder
                .topicExchange("logistics.exchange")
                .durable(true)
                .build();
    }

    /**
     * 积分交换机
     */
    @Bean
    public TopicExchange pointsExchange() {
        return ExchangeBuilder
                .topicExchange("points.exchange")
                .durable(true)
                .build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return ExchangeBuilder
                .directExchange("dlx.exchange")
                .durable(true)
                .build();
    }

    // ===================== 队列定义 =====================

    /**
     * 订单创建队列
     */
    @Bean
    public Queue orderCreateQueue() {
        return QueueBuilder
                .durable("order.create.queue")
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "dlx.order.create")
                .withArgument("x-message-ttl", 600000) // 10分钟TTL
                .build();
    }

    /**
     * 订单取消队列
     */
    @Bean
    public Queue orderCancelQueue() {
        return QueueBuilder
                .durable("order.cancel.queue")
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "dlx.order.cancel")
                .build();
    }

    /**
     * 支付成功队列
     */
    @Bean
    public Queue paymentSuccessQueue() {
        return QueueBuilder
                .durable("payment.success.queue")
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "dlx.payment.success")
                .build();
    }

    /**
     * 库存更新队列
     */
    @Bean
    public Queue productStockUpdateQueue() {
        return QueueBuilder
                .durable("product.stock.update.queue")
                .build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder
                .durable("dlx.queue")
                .build();
    }

    // ===================== 绑定关系 =====================

    /**
     * 订单创建队列绑定
     */
    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
                .bind(orderCreateQueue())
                .to(orderExchange())
                .with("order.create");
    }

    /**
     * 订单取消队列绑定
     */
    @Bean
    public Binding orderCancelBinding() {
        return BindingBuilder
                .bind(orderCancelQueue())
                .to(orderExchange())
                .with("order.cancel");
    }

    /**
     * 支付成功队列绑定
     */
    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with("payment.success");
    }

    /**
     * 库存更新队列绑定
     */
    @Bean
    public Binding productStockUpdateBinding() {
        return BindingBuilder
                .bind(productStockUpdateQueue())
                .to(productExchange())
                .with("product.stock.*");
    }

    /**
     * 死信队列绑定
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder
                .bind(dlxQueue())
                .to(dlxExchange())
                .with("dlx.#");
    }
}