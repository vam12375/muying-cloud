package com.muyingmall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体（订单服务本地视图）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品状态 ACTIVE-上架 INACTIVE-下架
     */
    private String status;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 获取商品状态（兼容方法）
     */
    public Integer getProductStatus() {
        if ("ACTIVE".equals(this.status)) {
            return 1;
        } else if ("INACTIVE".equals(this.status)) {
            return 0;
        }
        return 0;
    }
    
    /**
     * 获取新价格（兼容方法）
     */
    public BigDecimal getPriceNew() {
        return this.price;
    }
    
    /**
     * 获取商品ID（兼容方法）
     */
    public Long getProductId() {
        return this.id;
    }
    
    /**
     * 获取商品名称（兼容方法）
     */
    public String getProductName() {
        return this.name;
    }
    
    /**
     * 获取商品图片（兼容方法）
     */
    public String getProductImg() {
        return this.image;
    }
}
