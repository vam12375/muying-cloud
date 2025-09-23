package com.muyingmall.user.feign.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDTO {
    private Integer orderId;
    private Integer userId;
    private BigDecimal orderAmount;
    private Integer pointsUsed;
}
