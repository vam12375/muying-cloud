package com.muyingmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.product.entity.InventoryLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存变动日志Mapper接口
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {

}