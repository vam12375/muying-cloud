package com.muyingmall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_coupon")
public class UserCoupon implements Serializable {

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
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型 DISCOUNT-折扣 CASH-现金
     */
    private String couponType;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣率（百分比）
     */
    private BigDecimal discountRate;

    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;

    /**
     * 最大优惠金额
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 优惠券状态 UNUSED-未使用 USED-已使用 EXPIRED-已过期
     */
    private String status;

    /**
     * 领取时间
     */
    private LocalDateTime receivedTime;

    /**
     * 使用时间
     */
    private LocalDateTime usedTime;

    /**
     * 生效时间
     */
    private LocalDateTime validFrom;

    /**
     * 失效时间
     */
    private LocalDateTime validTo;

    /**
     * 订单ID（使用时关联的订单）
     */
    private Long orderId;

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
     * 获取过期时间（兼容方法）
     */
    public LocalDateTime getExpireTime() {
        return this.validTo;
    }
    
    /**
     * 获取创建时间（兼容方法）
     */
    public LocalDateTime getCreateTime() {
        return this.createdTime;
    }
    
    /**
     * 优惠券详细信息（关联查询时使用）
     */
    @TableField(exist = false)
    private Object coupon;
    
    /**
     * 获取优惠券信息（兼容方法）
     */
    public Object getCoupon() {
        return this.coupon;
    }
    
    /**
     * 设置优惠券信息（兼容方法）
     */
    public void setCoupon(Object coupon) {
        this.coupon = coupon;
    }
    
    /**
     * 获取批次ID（兼容方法）
     */
    public Integer getBatchId() {
        return this.couponCode != null ? couponCode.hashCode() : 0;
    }
    
    /**
     * 设置批次ID（兼容方法）
     */
    public void setBatchId(Integer batchId) {
        // 这里可以根据需要存储批次信息
    }
    
    /**
     * 设置领取时间（兼容方法）
     */
    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receivedTime = receiveTime;
    }
    
    /**
     * 设置使用时间（兼容方法）
     */
    public void setUseTime(LocalDateTime useTime) {
        this.usedTime = useTime;
    }
    
    /**
     * 设置过期时间（兼容方法）
     */
    public void setExpireTime(LocalDateTime expireTime) {
        this.validTo = expireTime;
    }
    
    /**
     * 设置创建时间（兼容方法）
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createdTime = createTime;
    }
    
    /**
     * 设置更新时间（兼容方法）
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updatedTime = updateTime;
    }
}
