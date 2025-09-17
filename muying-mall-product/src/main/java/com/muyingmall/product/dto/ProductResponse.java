package com.muyingmall.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    
    private Integer id;
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
    private String mainImage;
    private List<String> images;
    private String specifications;
    private String status;
    private Integer salesCount;
    private Integer viewCount;
    private BigDecimal weight;
    private String unit;
    private String sku;
    private List<String> tags;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}