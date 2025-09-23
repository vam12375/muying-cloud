package com.muyingmall.user.feign.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Integer productId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
}
