package com.muyingmall.common.core.enums;

/**
 * 退款状态枚举
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public enum RefundStatus {
    
    /**
     * 待处理
     */
    PENDING("PENDING", "待处理"),
    
    /**
     * 处理中
     */
    PROCESSING("PROCESSING", "处理中"),
    
    /**
     * 已批准
     */
    APPROVED("APPROVED", "已批准"),
    
    /**
     * 已拒绝
     */
    REJECTED("REJECTED", "已拒绝"),
    
    /**
     * 已完成
     */
    COMPLETED("COMPLETED", "已完成"),
    
    /**
     * 失败
     */
    FAILED("FAILED", "失败"),
    
    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String description;

    RefundStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     * 
     * @param code 状态代码
     * @return 退款状态枚举
     * @throws IllegalArgumentException 如果状态代码无效
     */
    public static RefundStatus fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Refund status code cannot be null");
        }
        for (RefundStatus status : RefundStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown refund status code: " + code);
    }

    /**
     * 判断是否为终态
     * 
     * @return 是否为终态
     */
    public boolean isFinalStatus() {
        return this == COMPLETED || this == FAILED || this == CANCELLED || this == REJECTED;
    }

    /**
     * 判断是否为成功状态
     * 
     * @return 是否为成功状态
     */
    public boolean isSuccessStatus() {
        return this == COMPLETED;
    }

    /**
     * 判断是否为失败状态
     * 
     * @return 是否为失败状态
     */
    public boolean isFailedStatus() {
        return this == FAILED || this == CANCELLED || this == REJECTED;
    }
}