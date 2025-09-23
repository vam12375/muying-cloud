package com.muyingmall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户地址实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_address")
public class UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县编码
     */
    private String districtCode;

    /**
     * 区县名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 是否默认地址 0-否 1-是
     */
    private Integer isDefault;

    /**
     * 地址标签
     */
    private String tag;

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
     * 获取完整地址
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (provinceName != null) sb.append(provinceName);
        if (cityName != null) sb.append(cityName);
        if (districtName != null) sb.append(districtName);
        if (detailAddress != null) sb.append(detailAddress);
        return sb.toString();
    }
    
    /**
     * 获取收货人姓名（兼容方法）
     */
    public String getReceiver() {
        return this.receiverName;
    }
    
    /**
     * 获取收货人电话（兼容方法）
     */
    public String getPhone() {
        return this.receiverPhone;
    }
    
    /**
     * 获取省份（兼容方法）
     */
    public String getProvince() {
        return this.provinceName;
    }
    
    /**
     * 获取城市（兼容方法）
     */
    public String getCity() {
        return this.cityName;
    }
    
    /**
     * 获取区县（兼容方法）
     */
    public String getDistrict() {
        return this.districtName;
    }
    
    /**
     * 获取详细地址（兼容方法）
     */
    public String getAddress() {
        return this.detailAddress;
    }
    
    /**
     * 获取邮政编码（兼容方法）
     */
    public String getZip() {
        return this.zipCode;
    }
    
    /**
     * 获取地址ID（兼容方法）
     */
    public Long getAddressId() {
        return this.id;
    }
}