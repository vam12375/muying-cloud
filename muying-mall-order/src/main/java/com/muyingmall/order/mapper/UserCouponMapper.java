package com.muyingmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.order.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券Mapper接口
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 查询用户可用的优惠券
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCoupon> selectAvailableCoupons(@Param("userId") Long userId);

    /**
     * 根据优惠券码查询
     * @param couponCode 优惠券码
     * @param userId 用户ID
     * @return 用户优惠券
     */
    UserCoupon selectByCouponCode(@Param("couponCode") String couponCode, @Param("userId") Long userId);

    /**
     * 更新优惠券状态为已使用
     * @param id 优惠券ID
     * @param orderId 订单ID
     * @return 影响行数
     */
    int updateStatusToUsed(@Param("id") Long id, @Param("orderId") Long orderId);

    /**
     * 查询即将过期的优惠券
     * @param days 天数
     * @return 即将过期的优惠券列表
     */
    List<UserCoupon> selectExpiringSoon(@Param("days") int days);
}