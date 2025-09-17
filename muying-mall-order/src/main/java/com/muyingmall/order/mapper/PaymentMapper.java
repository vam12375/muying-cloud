package com.muyingmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.order.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付数据访问层
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

}