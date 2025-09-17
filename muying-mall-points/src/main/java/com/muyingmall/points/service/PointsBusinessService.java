package com.muyingmall.points.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 积分业务服务接口
 * 提供完整的积分业务逻辑
 */
public interface PointsBusinessService {

    /**
     * 用户注册赠送积分
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean grantRegistrationPoints(Long userId);

    /**
     * 签到赠送积分
     * @param userId 用户ID
     * @param isConsecutive 是否连续签到
     * @param consecutiveDays 连续签到天数
     * @return 获得的积分数
     */
    BigDecimal grantCheckInPoints(Long userId, boolean isConsecutive, int consecutiveDays);

    /**
     * 购买商品赠送积分
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @param orderNo 订单号
     * @return 获得的积分数
     */
    BigDecimal grantPurchasePoints(Long userId, BigDecimal orderAmount, String orderNo);

    /**
     * 评价商品赠送积分
     * @param userId 用户ID
     * @param productId 商品ID
     * @param hasImages 是否有图片
     * @param rating 评分
     * @return 获得的积分数
     */
    BigDecimal grantCommentPoints(Long userId, Long productId, boolean hasImages, int rating);

    /**
     * 分享商品赠送积分
     * @param userId 用户ID
     * @param productId 商品ID
     * @param shareType 分享类型（wechat, qq, weibo等）
     * @return 获得的积分数
     */
    BigDecimal grantSharePoints(Long userId, Long productId, String shareType);

    /**
     * 邀请好友赠送积分
     * @param inviterUserId 邀请人用户ID
     * @param inviteeUserId 被邀请人用户ID
     * @return 邀请人获得的积分数
     */
    BigDecimal grantInvitePoints(Long inviterUserId, Long inviteeUserId);

    /**
     * 完成任务赠送积分
     * @param userId 用户ID
     * @param taskType 任务类型
     * @param taskId 任务ID
     * @return 获得的积分数
     */
    BigDecimal grantTaskPoints(Long userId, String taskType, Long taskId);

    /**
     * 使用积分抵扣订单
     * @param userId 用户ID
     * @param orderNo 订单号
     * @param pointsToUse 使用的积分数
     * @param deductionAmount 抵扣金额
     * @return 是否成功
     */
    boolean usePointsForOrder(Long userId, String orderNo, BigDecimal pointsToUse, BigDecimal deductionAmount);

    /**
     * 积分兑换商品
     * @param userId 用户ID
     * @param productId 积分商品ID
     * @param quantity 兑换数量
     * @param addressId 收货地址ID
     * @return 兑换订单号
     */
    String exchangePointsProduct(Long userId, Long productId, Integer quantity, Long addressId);

    /**
     * 积分转赠
     * @param fromUserId 转赠人用户ID
     * @param toUserId 接收人用户ID
     * @param points 转赠积分数
     * @param message 转赠留言
     * @return 是否成功
     */
    boolean transferPoints(Long fromUserId, Long toUserId, BigDecimal points, String message);

    /**
     * 获取用户积分账户信息
     * @param userId 用户ID
     * @return 积分账户信息
     */
    Map<String, Object> getUserPointsInfo(Long userId);

    /**
     * 获取用户积分变动记录
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @param type 类型筛选
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 积分变动记录
     */
    Map<String, Object> getUserPointsHistory(Long userId, Integer page, Integer size, 
                                           String type, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户积分统计
     * @param userId 用户ID
     * @param days 统计天数
     * @return 积分统计信息
     */
    Map<String, Object> getUserPointsStats(Long userId, Integer days);

    /**
     * 获取积分排行榜
     * @param type 排行类型（total, month, week）
     * @param limit 返回数量
     * @return 排行榜数据
     */
    List<Map<String, Object>> getPointsRanking(String type, Integer limit);

    /**
     * 获取积分商品推荐
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐商品列表
     */
    List<Map<String, Object>> getRecommendedPointsProducts(Long userId, Integer limit);

    /**
     * 计算积分兑换比例
     * @param points 积分数
     * @return 可兑换的金额
     */
    BigDecimal calculatePointsToMoney(BigDecimal points);

    /**
     * 计算金额对应的积分
     * @param amount 金额
     * @return 对应的积分数
     */
    BigDecimal calculateMoneyToPoints(BigDecimal amount);

    /**
     * 获取积分获取规则
     * @return 积分规则列表
     */
    List<Map<String, Object>> getPointsRules();

    /**
     * 过期积分处理
     * @return 处理的用户数量
     */
    int processExpiredPoints();

    /**
     * 积分清零（年度结算）
     * @param beforeDate 清零日期之前的积分
     * @return 处理的用户数量
     */
    int clearExpiredPoints(LocalDateTime beforeDate);

    /**
     * 获取积分系统统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    Map<String, Object> getPointsSystemStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 验证积分余额是否足够
     * @param userId 用户ID
     * @param points 所需积分
     * @return 是否足够
     */
    boolean validatePointsBalance(Long userId, BigDecimal points);

    /**
     * 积分回滚（订单取消等场景）
     * @param userId 用户ID
     * @param businessOrderNo 业务订单号
     * @param reason 回滚原因
     * @return 是否成功
     */
    boolean rollbackPoints(Long userId, String businessOrderNo, String reason);
}