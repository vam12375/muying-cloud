package com.muyingmall.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.points.entity.PointsProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分商品Mapper接口
 */
@Mapper
public interface PointsProductMapper extends BaseMapper<PointsProduct> {
}