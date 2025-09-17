package com.muyingmall.product.service;

import com.muyingmall.product.entity.InventoryLog;
import com.muyingmall.product.entity.Product;

import java.util.List;

/**
 * 库存管理服务接口
 */
public interface InventoryService {

    /**
     * 增加库存
     *
     * @param productId 商品ID
     * @param quantity  增加数量
     * @param reason    操作原因
     * @return 是否成功
     */
    boolean increaseStock(Integer productId, Integer quantity, String reason);

    /**
     * 减少库存
     *
     * @param productId 商品ID
     * @param quantity  减少数量
     * @param reason    操作原因
     * @return 是否成功
     */
    boolean decreaseStock(Integer productId, Integer quantity, String reason);

    /**
     * 设置库存
     *
     * @param productId 商品ID
     * @param quantity  库存数量
     * @param reason    操作原因
     * @return 是否成功
     */
    boolean setStock(Integer productId, Integer quantity, String reason);

    /**
     * 检查库存是否足够
     *
     * @param productId 商品ID
     * @param quantity  所需数量
     * @return 是否足够
     */
    boolean checkStock(Integer productId, Integer quantity);

    /**
     * 锁定库存（预占用）
     *
     * @param productId 商品ID
     * @param quantity  锁定数量
     * @param reason    锁定原因
     * @return 是否成功
     */
    boolean lockStock(Integer productId, Integer quantity, String reason);

    /**
     * 释放锁定的库存
     *
     * @param productId 商品ID
     * @param quantity  释放数量
     * @param reason    释放原因
     * @return 是否成功
     */
    boolean unlockStock(Integer productId, Integer quantity, String reason);

    /**
     * 批量检查库存
     *
     * @param productStockMap 商品ID和数量的映射
     * @return 是否全部满足库存要求
     */
    boolean batchCheckStock(java.util.Map<Integer, Integer> productStockMap);

    /**
     * 批量扣减库存
     *
     * @param productStockMap 商品ID和数量的映射
     * @param reason          扣减原因
     * @return 是否成功
     */
    boolean batchDecreaseStock(java.util.Map<Integer, Integer> productStockMap, String reason);

    /**
     * 获取库存变动记录
     *
     * @param productId 商品ID
     * @param page      页码
     * @param size      每页大小
     * @return 库存变动记录列表
     */
    List<InventoryLog> getInventoryLogs(Integer productId, Integer page, Integer size);

    /**
     * 获取低库存商品列表
     *
     * @param threshold 库存阈值
     * @return 低库存商品列表
     */
    List<Product> getLowStockProducts(Integer threshold);

    /**
     * 获取商品当前库存
     *
     * @param productId 商品ID
     * @return 库存数量
     */
    Integer getCurrentStock(Integer productId);

    /**
     * 记录库存变动日志
     *
     * @param productId    商品ID
     * @param changeType   变动类型（增加/减少/设置）
     * @param changeAmount 变动数量
     * @param beforeStock  变动前库存
     * @param afterStock   变动后库存
     * @param reason       变动原因
     */
    void recordInventoryLog(Integer productId, String changeType, Integer changeAmount, 
                          Integer beforeStock, Integer afterStock, String reason);
}