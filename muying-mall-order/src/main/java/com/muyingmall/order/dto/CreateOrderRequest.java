package com.muyingmall.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    
    @NotEmpty(message = "购物车商品不能为空")
    private List<Integer> cartItemIds;
    
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    
    @NotBlank(message = "收货人手机号不能为空")
    private String receiverPhone;
    
    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;
    
    private String remark;
}