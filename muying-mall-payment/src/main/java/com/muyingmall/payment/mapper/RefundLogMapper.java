package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.RefundLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款日志Mapper接口
 */
@Mapper
public interface RefundLogMapper extends BaseMapper<RefundLog> {
}