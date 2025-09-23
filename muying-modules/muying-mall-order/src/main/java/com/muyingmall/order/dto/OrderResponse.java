package com.muyingmall.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    
    private Integer id;
    private String orderNo;
    private Integer userId;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payableAmount;
    private BigDecimal shippingFee;
    private String paymentMethod;
    private String paymentStatus;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime finishTime;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private List<OrderItemResponse> items;
}

@Data
class OrderItemResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String specifications;
}