package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.PaymentStateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付状态变更日志Mapper接口
 */
@Mapper
public interface PaymentStateLogMapper extends BaseMapper<PaymentStateLog> {
}