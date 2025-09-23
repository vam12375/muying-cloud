package com.muyingmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.order.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券Mapper接口
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {
}