package com.muyingmall.common.core.enums;

/**
 * 积分操作类型枚举
 * 定义系统中各种积分操作的类型
 *
 * @author 青柠檬
 * @since 2025-09-25
 */
public enum PointsOperationType {

    /**
     * 签到获得积分
     */
    SIGN_IN("SIGN_IN", "签到获得积分"),

    /**
     * 订单奖励积分
     */
    ORDER_REWARD("ORDER_REWARD", "订单奖励积分"),

    /**
     * 兑换商品消费积分
     */
    EXCHANGE_PRODUCT("EXCHANGE_PRODUCT", "兑换商品消费积分"),

    /**
     * 管理员调整积分
     */
    ADMIN_ADJUSTMENT("ADMIN_ADJUSTMENT", "管理员调整积分"),

    /**
     * 活动奖励积分
     */
    EVENT_REWARD("EVENT_REWARD", "活动奖励积分"),

    /**
     * 推荐奖励积分
     */
    REFERRAL_REWARD("REFERRAL_REWARD", "推荐奖励积分"),

    /**
     * 评价奖励积分
     */
    REVIEW_REWARD("REVIEW_REWARD", "评价奖励积分"),

    /**
     * 积分过期扣除
     */
    EXPIRE_DEDUCTION("EXPIRE_DEDUCTION", "积分过期扣除"),

    /**
     * 其他操作
     */
    OTHER("OTHER", "其他操作");

    /**
     * 操作类型代码
     */
    private final String code;

    /**
     * 操作类型描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param code        操作类型代码
     * @param description 操作类型描述
     */
    PointsOperationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取操作类型代码
     *
     * @return 操作类型代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取操作类型描述
     *
     * @return 操作类型描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举值
     *
     * @param code 操作类型代码
     * @return 枚举值，如果未找到则返回null
     */
    public static PointsOperationType fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (PointsOperationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}