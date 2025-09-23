package com.muyingmall.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequest {
    
    @NotNull(message = "商品ID不能为空")
    private Integer productId;
    
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private Integer quantity;
    
    private String specifications;
}