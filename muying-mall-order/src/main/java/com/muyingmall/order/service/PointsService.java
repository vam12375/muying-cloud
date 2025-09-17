package com.muyingmall.order.service;

import java.math.BigDecimal;

/**
 * 积分服务接口（订单服务中的简化版本）
 */
public interface PointsService {

    /**
     * 获取用户积分余额
     * @param userId 用户ID
     * @return 积分余额
     */
    BigDecimal getPointsBalance(Long userId);

    /**
     * 消费积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param orderId 订单ID
     * @param remark 备注
     * @return 是否消费成功
     */
    boolean consumePoints(Long userId, BigDecimal points, Long orderId, String remark);

    /**
     * 退还积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param orderId 订单ID
     * @param remark 备注
     * @return 是否退还成功
     */
    boolean refundPoints(Long userId, BigDecimal points, Long orderId, String remark);

    /**
     * 奖励积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param orderId 订单ID
     * @param remark 备注
     * @return 是否奖励成功
     */
    boolean rewardPoints(Long userId, BigDecimal points, Long orderId, String remark);
    
    // 兼容方法，支持Integer类型的参数
    
    /**
     * 获取用户积分余额（兼容方法）
     * @param userId 用户ID
     * @return 积分余额
     */
    default BigDecimal getUserPoints(Integer userId) {
        return getPointsBalance(userId.longValue());
    }
    
    /**
     * 扣除积分（兼容方法）
     * @param userId 用户ID
     * @param points 积分数量
     * @param orderId 订单ID
     * @param type 类型
     * @param remark 备注
     * @return 是否成功
     */
    default boolean deductPoints(Integer userId, Integer points, String orderId, String type, String remark) {
        return consumePoints(userId.longValue(), BigDecimal.valueOf(points), Long.valueOf(orderId), remark);
    }
}