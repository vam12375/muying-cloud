package com.muyingmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.order.entity.OrderProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品 Mapper 接口
 */
@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
}