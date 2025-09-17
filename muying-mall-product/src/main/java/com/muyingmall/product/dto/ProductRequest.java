package com.muyingmall.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;
    
    @Size(max = 2000, message = "商品描述长度不能超过2000个字符")
    private String description;
    
    @Size(max = 500, message = "简短描述长度不能超过500个字符")
    private String shortDescription;
    
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @Digits(integer = 10, fraction = 2, message = "价格格式不正确")
    private BigDecimal price;
    
    @DecimalMin(value = "0.01", message = "原价必须大于0")
    @Digits(integer = 10, fraction = 2, message = "原价格式不正确")
    private BigDecimal originalPrice;
    
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stock;
    
    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;
    
    @NotNull(message = "品牌ID不能为空")
    private Integer brandId;
    
    @NotBlank(message = "主图不能为空")
    private String mainImage;
    
    private List<String> images;
    
    private String specifications;
    
    private BigDecimal weight;
    
    private String unit;
    
    private String sku;
    
    private List<String> tags;
}