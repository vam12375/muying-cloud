package com.muyingmall.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemResponse {
    
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private Integer quantity;
    private String specifications;
    private Integer selected;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}