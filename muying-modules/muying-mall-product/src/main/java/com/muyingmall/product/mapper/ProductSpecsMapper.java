package com.muyingmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.product.entity.ProductSpecs;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品规格Mapper接口
 */
@Mapper
public interface ProductSpecsMapper extends BaseMapper<ProductSpecs> {
}