package com.muyingmall.common.enums;

/**
 * 积分操作类型枚举
 */
public enum PointsOperationType {
    /**
     * 获得积分
     */
    EARN("earn", "获得积分"),

    /**
     * 消费积分
     */
    SPEND("spend", "消费积分");

    private final String code;
    private final String description;

    PointsOperationType(String code, String description) {
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
     * 根据代码获取操作类型
     */
    public static PointsOperationType getByCode(String code) {
        for (PointsOperationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}