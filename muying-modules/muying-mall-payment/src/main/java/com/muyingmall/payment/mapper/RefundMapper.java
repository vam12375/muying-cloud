package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款Mapper接口
 */
@Mapper
public interface RefundMapper extends BaseMapper<Refund> {
}