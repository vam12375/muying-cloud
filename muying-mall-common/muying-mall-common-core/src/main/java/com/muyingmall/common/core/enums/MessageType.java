package com.muyingmall.common.core.enums;

/**
 * 消息类型枚举
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public enum MessageType {
    /**
     * 系统消息
     */
    SYSTEM("system", "系统消息"),

    /**
     * 订单消息
     */
    ORDER("order", "订单消息"),

    /**
     * 促销消息
     */
    PROMOTION("promotion", "促销消息"),

    /**
     * 积分消息
     */
    POINTS("points", "积分消息"),

    /**
     * 用户消息
     */
    USER("user", "用户消息"),

    /**
     * 通知消息
     */
    NOTIFICATION("notification", "通知消息"),

    /**
     * 催发货提醒
     */
    SHIPPING_REMINDER("shipping_reminder", "催发货提醒");

    private final String code;
    private final String description;

    MessageType(String code, String description) {
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
     * 根据代码获取消息类型
     *
     * @param code 消息类型代码
     * @return 消息类型枚举，如果未找到返回null
     */
    public static MessageType getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (MessageType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}