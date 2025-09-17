package com.muyingmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.product.entity.InventoryLog;
import com.muyingmall.product.entity.Product;
import com.muyingmall.product.mapper.InventoryLogMapper;
import com.muyingmall.product.mapper.ProductMapper;
import com.muyingmall.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存管理服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final ProductMapper productMapper;
    private final InventoryLogMapper inventoryLogMapper;

    @Override
    @Transactional
    public boolean increaseStock(Integer productId, Integer quantity, String reason) {
        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("增加库存参数无效: productId={}, quantity={}", productId, quantity);
            return false;
        }

        try {
            // 获取当前商品信息
            Product product = productMapper.selectById(productId);
            if (product == null) {
                log.warn("商品不存在: productId={}", productId);
                return false;
            }

            Integer beforeStock = product.getStock() != null ? product.getStock() : 0;
            Integer afterStock = beforeStock + quantity;

            // 更新库存
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getProductId, productId)
                         .set(Product::getStock, afterStock)
                         .set(Product::getUpdateTime, LocalDateTime.now());
            
            int updateCount = productMapper.update(null, updateWrapper);
            if (updateCount > 0) {
                // 记录库存变动日志
                recordInventoryLog(productId, "增加", quantity, beforeStock, afterStock, reason);
                log.info("库存增加成功: productId={}, quantity={}, beforeStock={}, afterStock={}", 
                         productId, quantity, beforeStock, afterStock);
                return true;
            }
        } catch (Exception e) {
            log.error("增加库存失败: productId={}, quantity={}, reason={}", productId, quantity, reason, e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer productId, Integer quantity, String reason) {
        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("减少库存参数无效: productId={}, quantity={}", productId, quantity);
            return false;
        }

        try {
            // 获取当前商品信息
            Product product = productMapper.selectById(productId);
            if (product == null) {
                log.warn("商品不存在: productId={}", productId);
                return false;
            }

            Integer beforeStock = product.getStock() != null ? product.getStock() : 0;
            
            // 检查库存是否足够
            if (beforeStock < quantity) {
                log.warn("库存不足: productId={}, currentStock={}, requiredQuantity={}", 
                         productId, beforeStock, quantity);
                return false;
            }

            Integer afterStock = beforeStock - quantity;

            // 更新库存
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getProductId, productId)
                         .eq(Product::getStock, beforeStock) // 乐观锁，确保并发安全
                         .set(Product::getStock, afterStock)
                         .set(Product::getUpdateTime, LocalDateTime.now());
            
            int updateCount = productMapper.update(null, updateWrapper);
            if (updateCount > 0) {
                // 记录库存变动日志
                recordInventoryLog(productId, "减少", -quantity, beforeStock, afterStock, reason);
                log.info("库存减少成功: productId={}, quantity={}, beforeStock={}, afterStock={}", 
                         productId, quantity, beforeStock, afterStock);
                return true;
            } else {
                log.warn("库存减少失败，可能存在并发修改: productId={}", productId);
            }
        } catch (Exception e) {
            log.error("减少库存失败: productId={}, quantity={}, reason={}", productId, quantity, reason, e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean setStock(Integer productId, Integer quantity, String reason) {
        if (productId == null || quantity == null || quantity < 0) {
            log.warn("设置库存参数无效: productId={}, quantity={}", productId, quantity);
            return false;
        }

        try {
            // 获取当前商品信息
            Product product = productMapper.selectById(productId);
            if (product == null) {
                log.warn("商品不存在: productId={}", productId);
                return false;
            }

            Integer beforeStock = product.getStock() != null ? product.getStock() : 0;
            Integer changeAmount = quantity - beforeStock;

            // 更新库存
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getProductId, productId)
                         .set(Product::getStock, quantity)
                         .set(Product::getUpdateTime, LocalDateTime.now());
            
            int updateCount = productMapper.update(null, updateWrapper);
            if (updateCount > 0) {
                // 记录库存变动日志
                recordInventoryLog(productId, "设置", changeAmount, beforeStock, quantity, reason);
                log.info("库存设置成功: productId={}, beforeStock={}, afterStock={}", 
                         productId, beforeStock, quantity);
                return true;
            }
        } catch (Exception e) {
            log.error("设置库存失败: productId={}, quantity={}, reason={}", productId, quantity, reason, e);
        }
        return false;
    }

    @Override
    public boolean checkStock(Integer productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        try {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                return false;
            }

            Integer currentStock = product.getStock() != null ? product.getStock() : 0;
            return currentStock >= quantity;
        } catch (Exception e) {
            log.error("检查库存失败: productId={}, quantity={}", productId, quantity, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean lockStock(Integer productId, Integer quantity, String reason) {
        // 锁定库存实际上就是减少库存，但记录的操作类型不同
        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("锁定库存参数无效: productId={}, quantity={}", productId, quantity);
            return false;
        }

        try {
            // 获取当前商品信息
            Product product = productMapper.selectById(productId);
            if (product == null) {
                log.warn("商品不存在: productId={}", productId);
                return false;
            }

            Integer beforeStock = product.getStock() != null ? product.getStock() : 0;
            
            // 检查库存是否足够
            if (beforeStock < quantity) {
                log.warn("库存不足，无法锁定: productId={}, currentStock={}, requiredQuantity={}", 
                         productId, beforeStock, quantity);
                return false;
            }

            Integer afterStock = beforeStock - quantity;

            // 更新库存
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getProductId, productId)
                         .eq(Product::getStock, beforeStock) // 乐观锁
                         .set(Product::getStock, afterStock)
                         .set(Product::getUpdateTime, LocalDateTime.now());
            
            int updateCount = productMapper.update(null, updateWrapper);
            if (updateCount > 0) {
                // 记录库存变动日志
                recordInventoryLog(productId, "锁定", -quantity, beforeStock, afterStock, reason);
                log.info("库存锁定成功: productId={}, quantity={}, beforeStock={}, afterStock={}", 
                         productId, quantity, beforeStock, afterStock);
                return true;
            } else {
                log.warn("库存锁定失败，可能存在并发修改: productId={}", productId);
            }
        } catch (Exception e) {
            log.error("锁定库存失败: productId={}, quantity={}, reason={}", productId, quantity, reason, e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean unlockStock(Integer productId, Integer quantity, String reason) {
        // 释放库存实际上就是增加库存
        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("释放库存参数无效: productId={}, quantity={}", productId, quantity);
            return false;
        }

        try {
            // 获取当前商品信息
            Product product = productMapper.selectById(productId);
            if (product == null) {
                log.warn("商品不存在: productId={}", productId);
                return false;
            }

            Integer beforeStock = product.getStock() != null ? product.getStock() : 0;
            Integer afterStock = beforeStock + quantity;

            // 更新库存
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getProductId, productId)
                         .set(Product::getStock, afterStock)
                         .set(Product::getUpdateTime, LocalDateTime.now());
            
            int updateCount = productMapper.update(null, updateWrapper);
            if (updateCount > 0) {
                // 记录库存变动日志
                recordInventoryLog(productId, "释放", quantity, beforeStock, afterStock, reason);
                log.info("库存释放成功: productId={}, quantity={}, beforeStock={}, afterStock={}", 
                         productId, quantity, beforeStock, afterStock);
                return true;
            }
        } catch (Exception e) {
            log.error("释放库存失败: productId={}, quantity={}, reason={}", productId, quantity, reason, e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean batchCheckStock(Map<Integer, Integer> productStockMap) {
        if (productStockMap == null || productStockMap.isEmpty()) {
            return false;
        }

        try {
            for (Map.Entry<Integer, Integer> entry : productStockMap.entrySet()) {
                if (!checkStock(entry.getKey(), entry.getValue())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("批量检查库存失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean batchDecreaseStock(Map<Integer, Integer> productStockMap, String reason) {
        if (productStockMap == null || productStockMap.isEmpty()) {
            return false;
        }

        try {
            // 先批量检查库存
            if (!batchCheckStock(productStockMap)) {
                log.warn("批量扣减库存失败：库存不足");
                return false;
            }

            // 批量扣减库存
            for (Map.Entry<Integer, Integer> entry : productStockMap.entrySet()) {
                if (!decreaseStock(entry.getKey(), entry.getValue(), reason)) {
                    log.error("批量扣减库存失败：商品ID={}", entry.getKey());
                    throw new RuntimeException("批量扣减库存失败，已回滚");
                }
            }
            
            log.info("批量扣减库存成功：商品数量={}", productStockMap.size());
            return true;
        } catch (Exception e) {
            log.error("批量扣减库存失败", e);
            // 事务会自动回滚
            return false;
        }
    }

    @Override
    public List<InventoryLog> getInventoryLogs(Integer productId, Integer page, Integer size) {
        if (productId == null || page == null || size == null || page <= 0 || size <= 0) {
            return null;
        }

        try {
            Page<InventoryLog> pageParam = new Page<>(page, size);
            
            LambdaQueryWrapper<InventoryLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryLog::getProductId, productId)
                        .orderByDesc(InventoryLog::getCreateTime);
            
            Page<InventoryLog> result = inventoryLogMapper.selectPage(pageParam, queryWrapper);
            return result.getRecords();
        } catch (Exception e) {
            log.error("获取库存变动记录失败: productId={}, page={}, size={}", productId, page, size, e);
            return null;
        }
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        if (threshold == null || threshold < 0) {
            threshold = 10; // 默认阈值
        }

        try {
            LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.le(Product::getStock, threshold)
                        .eq(Product::getProductStatus, "上架")
                        .orderByAsc(Product::getStock, Product::getProductId);
            
            return productMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取低库存商品失败: threshold={}", threshold, e);
            return null;
        }
    }

    @Override
    public Integer getCurrentStock(Integer productId) {
        if (productId == null) {
            return null;
        }

        try {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                return null;
            }
            return product.getStock() != null ? product.getStock() : 0;
        } catch (Exception e) {
            log.error("获取当前库存失败: productId={}", productId, e);
            return null;
        }
    }

    @Override
    public void recordInventoryLog(Integer productId, String changeType, Integer changeAmount, 
                                  Integer beforeStock, Integer afterStock, String reason) {
        try {
            InventoryLog log = new InventoryLog();
            log.setProductId(productId);
            log.setChangeAmount(changeAmount);
            log.setType(changeType);
            log.setReferenceId(null); // 可以根据业务需求设置关联ID
            log.setRemaining(afterStock);
            log.setOperator("system"); // 可以根据实际需求获取当前用户
            log.setRemark(reason);
            log.setCreateTime(LocalDateTime.now());
            
            inventoryLogMapper.insert(log);
        } catch (Exception e) {
            // 记录日志失败不应该影响主业务
            log.error("记录库存变动日志失败: productId={}, changeType={}, changeAmount={}, reason={}", 
                     productId, changeType, changeAmount, reason, e);
        }
    }
}