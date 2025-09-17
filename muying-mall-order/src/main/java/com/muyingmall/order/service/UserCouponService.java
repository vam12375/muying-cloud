package com.muyingmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muyingmall.order.entity.UserCoupon;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户优惠券服务接口
 */
public interface UserCouponService extends IService<UserCoupon> {

    /**
     * 查询用户可用的优惠券
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCoupon> getAvailableCoupons(Long userId);

    /**
     * 根据优惠券码查询
     * @param couponCode 优惠券码
     * @param userId 用户ID
     * @return 用户优惠券
     */
    UserCoupon getByCouponCode(String couponCode, Long userId);

    /**
     * 使用优惠券
     * @param couponId 优惠券ID
     * @param orderId 订单ID
     * @return 是否使用成功
     */
    boolean useCoupon(Long couponId, Long orderId);

    /**
     * 计算优惠券折扣金额
     * @param userCoupon 用户优惠券
     * @param orderAmount 订单金额
     * @return 折扣金额
     */
    BigDecimal calculateDiscountAmount(UserCoupon userCoupon, BigDecimal orderAmount);

    /**
     * 验证优惠券是否可用
     * @param userCoupon 用户优惠券
     * @param orderAmount 订单金额
     * @return 是否可用
     */
    boolean isValidForOrder(UserCoupon userCoupon, BigDecimal orderAmount);
}