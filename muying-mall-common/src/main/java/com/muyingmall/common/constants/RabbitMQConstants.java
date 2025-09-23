package com.muyingmall.common.constants;

/**
 * RabbitMQ常量定义
 * 包括交换机、队列、路由键等配置
 */
public class RabbitMQConstants {

    // ==================== 交换机定义 ====================
    
    /**
     * 用户事件交换机
     */
    public static final String USER_EVENT_EXCHANGE = "user-event-exchange";
    
    /**
     * 订单事件交换机
     */
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";
    
    /**
     * 支付事件交换机
     */
    public static final String PAYMENT_EVENT_EXCHANGE = "payment-event-exchange";
    
    /**
     * 商品事件交换机
     */
    public static final String PRODUCT_EVENT_EXCHANGE = "product-event-exchange";

    // ==================== 队列定义 ====================
    
    // 用户相关队列
    /**
     * 用户注册队列
     */
    public static final String USER_REGISTER_QUEUE = "user-register-queue";
    
    /**
     * 用户登录队列
     */
    public static final String USER_LOGIN_QUEUE = "user-login-queue";
    
    // 订单相关队列
    /**
     * 订单创建队列
     */
    public static final String ORDER_CREATE_QUEUE = "order-create-queue";
    
    /**
     * 订单支付队列
     */
    public static final String ORDER_PAY_QUEUE = "order-pay-queue";
    
    /**
     * 订单取消队列
     */
    public static final String ORDER_CANCEL_QUEUE = "order-cancel-queue";
    
    /**
     * 订单完成队列
     */
    public static final String ORDER_COMPLETE_QUEUE = "order-complete-queue";
    
    // 支付相关队列
    /**
     * 支付成功队列
     */
    public static final String PAYMENT_SUCCESS_QUEUE = "payment-success-queue";
    
    /**
     * 支付失败队列
     */
    public static final String PAYMENT_FAILED_QUEUE = "payment-failed-queue";
    
    /**
     * 支付退款队列
     */
    public static final String PAYMENT_REFUND_QUEUE = "payment-refund-queue";
    
    // 商品相关队列
    /**
     * 库存更新队列
     */
    public static final String PRODUCT_STOCK_UPDATE_QUEUE = "product-stock-update-queue";
    
    /**
     * 库存扣减队列
     */
    public static final String PRODUCT_STOCK_DEDUCT_QUEUE = "product-stock-deduct-queue";
    
    /**
     * 库存回滚队列
     */
    public static final String PRODUCT_STOCK_ROLLBACK_QUEUE = "product-stock-rollback-queue";
    
    // 订单消费者相关队列
    /**
     * 订单超时队列
     */
    public static final String ORDER_TIMEOUT_QUEUE = "order.timeout.queue";
    
    /**
     * 支付成功通知队列
     */
    public static final String PAYMENT_SUCCESS_NOTIFICATION_QUEUE = "payment.success.queue";
    
    /**
     * 物流状态更新队列
     */
    public static final String LOGISTICS_STATUS_QUEUE = "logistics.status.queue";

    // ==================== 路由键定义 ====================
    
    // 用户事件路由键
    public static final String USER_REGISTER_ROUTING_KEY = "user.register";
    public static final String USER_LOGIN_ROUTING_KEY = "user.login";
    
    // 订单事件路由键
    public static final String ORDER_CREATE_ROUTING_KEY = "order.create";
    public static final String ORDER_PAY_ROUTING_KEY = "order.pay";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order.cancel";
    public static final String ORDER_COMPLETE_ROUTING_KEY = "order.complete";
    
    // 支付事件路由键
    public static final String PAYMENT_SUCCESS_ROUTING_KEY = "payment.success";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";
    public static final String PAYMENT_REFUND_ROUTING_KEY = "payment.refund";
    
    // 商品事件路由键
    public static final String PRODUCT_STOCK_UPDATE_ROUTING_KEY = "product.stock.update";
    public static final String PRODUCT_STOCK_DEDUCT_ROUTING_KEY = "product.stock.deduct";
    public static final String PRODUCT_STOCK_ROLLBACK_ROUTING_KEY = "product.stock.rollback";

    // ==================== 死信交换机和队列 ====================
    
    /**
     * 死信交换机
     */
    public static final String DLX_EXCHANGE = "dlx-exchange";
    
    /**
     * 死信队列
     */
    public static final String DLX_QUEUE = "dlx-queue";
    
    /**
     * 死信路由键
     */
    public static final String DLX_ROUTING_KEY = "dlx";

    // ==================== 延时队列相关 ====================
    
    /**
     * 订单超时取消延时交换机
     */
    public static final String ORDER_DELAY_EXCHANGE = "order-delay-exchange";
    
    /**
     * 订单超时取消延时队列
     */
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";
    
    /**
     * 订单超时取消路由键
     */
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.cancel";
    
    // ==================== TTL配置 ====================
    
    /**
     * 订单超时时间（30分钟）
     */
    public static final long ORDER_TTL = 30 * 60 * 1000L;
    
    /**
     * 消息默认TTL（24小时）
     */
    public static final long DEFAULT_MESSAGE_TTL = 24 * 60 * 60 * 1000L;
}