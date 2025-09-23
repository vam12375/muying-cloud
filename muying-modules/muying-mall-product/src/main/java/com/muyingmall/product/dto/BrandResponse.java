package com.muyingmall.product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandResponse {
    
    private Integer id;
    private String name;
    private String description;
    private String logo;
    private String website;
    private String country;
    private Integer sortOrder;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}