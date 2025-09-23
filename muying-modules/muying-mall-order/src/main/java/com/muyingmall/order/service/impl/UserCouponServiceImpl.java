package com.muyingmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.order.entity.UserCoupon;
import com.muyingmall.order.mapper.UserCouponMapper;
import com.muyingmall.order.service.UserCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户优惠券服务实现类
 */
@Service
@Slf4j
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Override
    public List<UserCoupon> getAvailableCoupons(Long userId) {
        if (userId == null) {
            return List.of();
        }

        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, "UNUSED")
                .le(UserCoupon::getValidFrom, LocalDateTime.now())
                .ge(UserCoupon::getValidTo, LocalDateTime.now())
                .orderByDesc(UserCoupon::getCreatedTime);

        List<UserCoupon> userCoupons = list(queryWrapper);
        log.debug("查询用户可用优惠券: userId={}, count={}", userId, userCoupons.size());
        return userCoupons;
    }

    @Override
    public UserCoupon getByCouponCode(String couponCode, Long userId) {
        if (couponCode == null || couponCode.trim().isEmpty() || userId == null) {
            return null;
        }

        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getCouponCode, couponCode)
                .eq(UserCoupon::getUserId, userId);

        UserCoupon userCoupon = getOne(queryWrapper);
        log.debug("根据优惠券码查询: couponCode={}, userId={}, found={}", 
                couponCode, userId, userCoupon != null);
        return userCoupon;
    }

    @Override
    public boolean useCoupon(Long couponId, Long orderId) {
        if (couponId == null || orderId == null) {
            return false;
        }

        UserCoupon userCoupon = getById(couponId);
        if (userCoupon == null) {
            log.warn("用户优惠券不存在: couponId={}", couponId);
            return false;
        }

        if (!"UNUSED".equals(userCoupon.getStatus())) {
            log.warn("优惠券不可用，状态: {}", userCoupon.getStatus());
            return false;
        }

        // 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (userCoupon.getValidFrom() != null && now.isBefore(userCoupon.getValidFrom())) {
            log.warn("优惠券尚未生效: couponId={}, validFrom={}", couponId, userCoupon.getValidFrom());
            return false;
        }

        if (userCoupon.getValidTo() != null && now.isAfter(userCoupon.getValidTo())) {
            log.warn("优惠券已过期: couponId={}, validTo={}", couponId, userCoupon.getValidTo());
            return false;
        }

        // 更新优惠券状态
        userCoupon.setStatus("USED");
        userCoupon.setUsedTime(now);
        userCoupon.setOrderId(orderId);
        userCoupon.setUpdatedTime(now);

        boolean result = updateById(userCoupon);
        if (result) {
            log.info("使用优惠券成功: couponId={}, orderId={}", couponId, orderId);
        } else {
            log.error("使用优惠券失败: couponId={}, orderId={}", couponId, orderId);
        }

        return result;
    }

    @Override
    public BigDecimal calculateDiscountAmount(UserCoupon userCoupon, BigDecimal orderAmount) {
        if (userCoupon == null || orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 检查最低消费金额
        if (userCoupon.getMinAmount() != null && orderAmount.compareTo(userCoupon.getMinAmount()) < 0) {
            log.debug("订单金额未达到优惠券最低消费要求: orderAmount={}, minAmount={}", 
                    orderAmount, userCoupon.getMinAmount());
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount = BigDecimal.ZERO;

        if ("CASH".equals(userCoupon.getCouponType())) {
            // 现金券，直接使用优惠金额
            discountAmount = userCoupon.getDiscountAmount() != null ? 
                    userCoupon.getDiscountAmount() : BigDecimal.ZERO;
        } else if ("DISCOUNT".equals(userCoupon.getCouponType())) {
            // 折扣券，按百分比计算
            if (userCoupon.getDiscountRate() != null) {
                // 计算折扣金额：订单金额 * (1 - 折扣率)
                BigDecimal discountRate = userCoupon.getDiscountRate().divide(BigDecimal.valueOf(100));
                discountAmount = orderAmount.multiply(BigDecimal.ONE.subtract(discountRate));
                discountAmount = orderAmount.subtract(discountAmount);
            }
        }

        // 限制最大优惠金额
        if (userCoupon.getMaxDiscountAmount() != null && 
                discountAmount.compareTo(userCoupon.getMaxDiscountAmount()) > 0) {
            discountAmount = userCoupon.getMaxDiscountAmount();
        }

        // 折扣金额不能超过订单金额
        if (discountAmount.compareTo(orderAmount) > 0) {
            discountAmount = orderAmount;
        }

        log.debug("计算优惠券折扣金额: couponType={}, orderAmount={}, discountAmount={}", 
                userCoupon.getCouponType(), orderAmount, discountAmount);

        return discountAmount;
    }

    @Override
    public boolean isValidForOrder(UserCoupon userCoupon, BigDecimal orderAmount) {
        if (userCoupon == null || orderAmount == null) {
            return false;
        }

        // 检查优惠券状态
        if (!"UNUSED".equals(userCoupon.getStatus())) {
            return false;
        }

        // 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (userCoupon.getValidFrom() != null && now.isBefore(userCoupon.getValidFrom())) {
            return false;
        }

        if (userCoupon.getValidTo() != null && now.isAfter(userCoupon.getValidTo())) {
            return false;
        }

        // 检查最低消费金额
        if (userCoupon.getMinAmount() != null && orderAmount.compareTo(userCoupon.getMinAmount()) < 0) {
            return false;
        }

        return true;
    }
}