package com.muyingmall.product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryResponse {
    
    private Integer id;
    private String name;
    private String description;
    private Integer parentId;
    private String parentName;
    private Integer level;
    private Integer sortOrder;
    private String icon;
    private String image;
    private String status;
    private List<CategoryResponse> children;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}