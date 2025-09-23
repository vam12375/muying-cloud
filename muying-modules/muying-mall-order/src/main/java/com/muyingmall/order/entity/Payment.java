package com.muyingmall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付订单号
     */
    private String paymentOrderNo;

    /**
     * 第三方支付流水号
     */
    private String thirdPartyOrderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 支付方式 ALIPAY-支付宝 WECHAT-微信 BANK-银行卡
     */
    private String paymentMethod;

    /**
     * 支付状态 PENDING-待支付 SUCCESS-支付成功 FAILED-支付失败 CANCELLED-已取消
     */
    private String paymentStatus;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 支付完成时间
     */
    private LocalDateTime completedTime;

    /**
     * 支付失败原因
     */
    private String failureReason;

    /**
     * 备注
     */
    private String remark;

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
     * 设置支付订单号（兼容方法）
     */
    public void setPaymentNo(String paymentNo) {
        this.paymentOrderNo = paymentNo;
    }
    
    /**
     * 获取支付订单号（兼容方法）
     */
    public String getPaymentNo() {
        return this.paymentOrderNo;
    }
    
    /**
     * 设置支付状态（兼容方法）
     */
    public void setStatus(com.muyingmall.common.enums.PaymentStatus status) {
        this.paymentStatus = status.getCode();
    }
    
    /**
     * 获取支付状态（兼容方法）
     */
    public com.muyingmall.common.enums.PaymentStatus getStatus() {
        return com.muyingmall.common.enums.PaymentStatus.fromCode(this.paymentStatus);
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
    
    /**
     * 设置过期时间（兼容方法）
     */
    public void setExpireTime(LocalDateTime expireTime) {
        // 暂时使用备注字段存储过期时间信息
        this.remark = "expire_time:" + expireTime.toString();
    }
    
    /**
     * 获取过期时间（兼容方法）
     */
    public LocalDateTime getExpireTime() {
        if (this.remark != null && this.remark.startsWith("expire_time:")) {
            try {
                return LocalDateTime.parse(this.remark.substring("expire_time:".length()));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * 获取第三方交易号（兼容方法）
     */
    public String getTransactionId() {
        return this.thirdPartyOrderNo;
    }
    
    /**
     * 获取支付时间（兼容方法）
     */
    public LocalDateTime getPayTime() {
        return this.paymentTime;
    }
}