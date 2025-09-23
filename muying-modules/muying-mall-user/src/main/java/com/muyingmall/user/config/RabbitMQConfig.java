package com.muyingmall.user.config;

import com.muyingmall.common.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户服务RabbitMQ配置
 * 声明用户相关的交换机、队列和绑定关系
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 交换机声明 ====================
    
    /**
     * 用户事件交换机
     */
    @Bean
    public TopicExchange userEventExchange() {
        return ExchangeBuilder
                .topicExchange(RabbitMQConstants.USER_EVENT_EXCHANGE)
                .durable(true)
                .build();
    }

    // ==================== 队列声明 ====================
    
    /**
     * 用户注册队列
     */
    @Bean
    public Queue userRegisterQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.USER_REGISTER_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }
    
    /**
     * 用户登录队列
     */
    @Bean
    public Queue userLoginQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.USER_LOGIN_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }

    // ==================== 绑定关系声明 ====================
    
    /**
     * 用户注册队列绑定
     */
    @Bean
    public Binding userRegisterBinding() {
        return BindingBuilder
                .bind(userRegisterQueue())
                .to(userEventExchange())
                .with(RabbitMQConstants.USER_REGISTER_ROUTING_KEY);
    }
    
    /**
     * 用户登录队列绑定
     */
    @Bean
    public Binding userLoginBinding() {
        return BindingBuilder
                .bind(userLoginQueue())
                .to(userEventExchange())
                .with(RabbitMQConstants.USER_LOGIN_ROUTING_KEY);
    }

    // ==================== 死信交换机和队列 ====================
    
    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return ExchangeBuilder
                .directExchange(RabbitMQConstants.DLX_EXCHANGE)
                .durable(true)
                .build();
    }
    
    /**
     * 死信队列
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.DLX_QUEUE)
                .build();
    }
    
    /**
     * 死信队列绑定
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder
                .bind(dlxQueue())
                .to(dlxExchange())
                .with(RabbitMQConstants.DLX_ROUTING_KEY);
    }
}