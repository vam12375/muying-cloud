package com.muyingmall.common.rabbitmq.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单消息类
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderMessage extends BaseMessage {
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long customerId;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 订单状态
     */
    private Integer status;
    
    /**
     * 订单商品列表
     */
    private List<OrderItem> items;
    
    /**
     * 收货地址ID
     */
    private Long addressId;
    
    /**
     * 支付方式
     */
    private Integer paymentMethod;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 订单商品项
     */
    @Data
    public static class OrderItem {
        /**
         * 商品ID
         */
        private Long productId;
        
        /**
         * SKU ID
         */
        private Long skuId;
        
        /**
         * 商品名称
         */
        private String productName;
        
        /**
         * 购买数量
         */
        private Integer quantity;
        
        /**
         * 单价
         */
        private BigDecimal price;
        
        /**
         * 小计金额
         */
        private BigDecimal subtotal;
    }
    
    /**
     * 创建订单创建消息
     */
    public static OrderMessage createOrderCreateMessage(Long orderId, String orderNo, Long userId, BigDecimal amount) {
        OrderMessage message = new OrderMessage();
        message.setOrderId(orderId);
        message.setOrderNo(orderNo);
        message.setCustomerId(userId);
        message.setUserId(userId);
        message.setAmount(amount);
        message.setEventType("ORDER_CREATED");
        message.setSource("order-service");
        return message;
    }
    
    /**
     * 创建订单取消消息
     */
    public static OrderMessage createOrderCancelMessage(Long orderId, String orderNo, Long userId) {
        OrderMessage message = new OrderMessage();
        message.setOrderId(orderId);
        message.setOrderNo(orderNo);
        message.setCustomerId(userId);
        message.setUserId(userId);
        message.setEventType("ORDER_CANCELLED");
        message.setSource("order-service");
        return message;
    }
    
    /**
     * 创建订单完成消息
     */
    public static OrderMessage createOrderCompleteMessage(Long orderId, String orderNo, Long userId, BigDecimal amount) {
        OrderMessage message = new OrderMessage();
        message.setOrderId(orderId);
        message.setOrderNo(orderNo);
        message.setCustomerId(userId);
        message.setUserId(userId);
        message.setAmount(amount);
        message.setEventType("ORDER_COMPLETED");
        message.setSource("order-service");
        return message;
    }
}