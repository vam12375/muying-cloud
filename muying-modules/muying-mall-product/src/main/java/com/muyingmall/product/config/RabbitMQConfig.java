package com.muyingmall.product.config;

import com.muyingmall.common.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 商品服务RabbitMQ配置
 * 声明商品库存相关的交换机、队列和绑定关系
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 交换机声明 ====================
    
    /**
     * 商品事件交换机
     */
    @Bean
    public TopicExchange productEventExchange() {
        return ExchangeBuilder
                .topicExchange(RabbitMQConstants.PRODUCT_EVENT_EXCHANGE)
                .durable(true)
                .build();
    }

    // ==================== 队列声明 ====================
    
    /**
     * 库存更新队列
     */
    @Bean
    public Queue productStockUpdateQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.PRODUCT_STOCK_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }
    
    /**
     * 库存扣减队列
     */
    @Bean
    public Queue productStockDeductQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.PRODUCT_STOCK_DEDUCT_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }
    
    /**
     * 库存回滚队列
     */
    @Bean
    public Queue productStockRollbackQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.PRODUCT_STOCK_ROLLBACK_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", RabbitMQConstants.DEFAULT_MESSAGE_TTL)
                .build();
    }

    // ==================== 绑定关系声明 ====================
    
    /**
     * 库存更新队列绑定
     */
    @Bean
    public Binding productStockUpdateBinding() {
        return BindingBuilder
                .bind(productStockUpdateQueue())
                .to(productEventExchange())
                .with(RabbitMQConstants.PRODUCT_STOCK_UPDATE_ROUTING_KEY);
    }
    
    /**
     * 库存扣减队列绑定
     */
    @Bean
    public Binding productStockDeductBinding() {
        return BindingBuilder
                .bind(productStockDeductQueue())
                .to(productEventExchange())
                .with(RabbitMQConstants.PRODUCT_STOCK_DEDUCT_ROUTING_KEY);
    }
    
    /**
     * 库存回滚队列绑定
     */
    @Bean
    public Binding productStockRollbackBinding() {
        return BindingBuilder
                .bind(productStockRollbackQueue())
                .to(productEventExchange())
                .with(RabbitMQConstants.PRODUCT_STOCK_ROLLBACK_ROUTING_KEY);
    }

    // ==================== 跨服务队列监听 ====================

    /**
     * 监听订单创建事件（用于库存扣减）
     */
    @Bean
    public Queue orderCreateToProductQueue() {
        return QueueBuilder
                .durable("product-order-create-queue")
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定订单创建事件到商品服务队列
     */
    @Bean
    public Binding orderCreateToProductBinding() {
        return BindingBuilder
                .bind(orderCreateToProductQueue())
                .to(new TopicExchange(RabbitMQConstants.ORDER_EVENT_EXCHANGE))
                .with(RabbitMQConstants.ORDER_CREATE_ROUTING_KEY);
    }

    /**
     * 监听订单取消事件（用于库存回滚）
     */
    @Bean
    public Queue orderCancelToProductQueue() {
        return QueueBuilder
                .durable("product-order-cancel-queue")
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定订单取消事件到商品服务队列
     */
    @Bean
    public Binding orderCancelToProductBinding() {
        return BindingBuilder
                .bind(orderCancelToProductQueue())
                .to(new TopicExchange(RabbitMQConstants.ORDER_EVENT_EXCHANGE))
                .with(RabbitMQConstants.ORDER_CANCEL_ROUTING_KEY);
    }

    /**
     * 监听支付失败事件（用于库存回滚）
     */
    @Bean
    public Queue paymentFailedToProductQueue() {
        return QueueBuilder
                .durable("product-payment-failed-queue")
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.DLX_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定支付失败事件到商品服务队列
     */
    @Bean
    public Binding paymentFailedToProductBinding() {
        return BindingBuilder
                .bind(paymentFailedToProductQueue())
                .to(new TopicExchange(RabbitMQConstants.PAYMENT_EVENT_EXCHANGE))
                .with(RabbitMQConstants.PAYMENT_FAILED_ROUTING_KEY);
    }
}