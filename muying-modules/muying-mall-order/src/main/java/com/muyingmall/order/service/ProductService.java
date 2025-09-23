package com.muyingmall.order.service;

import com.muyingmall.order.entity.Product;

/**
 * 商品服务接口（订单服务中的简化版本）
 */
public interface ProductService {

    /**
     * 根据ID获取商品信息
     * @param productId 商品ID
     * @return 商品信息
     */
    Product getById(Long productId);

    /**
     * 检查商品库存
     * @param productId 商品ID
     * @param quantity 需要的数量
     * @return 是否有足够库存
     */
    boolean checkStock(Long productId, Integer quantity);

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    boolean reduceStock(Long productId, Integer quantity);

    /**
     * 恢复库存
     * @param productId 商品ID
     * @param quantity 恢复数量
     * @return 是否恢复成功
     */
    boolean restoreStock(Long productId, Integer quantity);
    
    /**
     * 更新商品信息（兼容方法）
     * @param updateWrapper 更新条件包装器
     * @return 是否更新成功
     */
    default boolean update(com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Product> updateWrapper) {
        // 默认实现，子类可以重写
        return false;
    }
}