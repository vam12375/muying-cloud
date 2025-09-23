package com.muyingmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.muyingmall.common.dto.Result;
import com.muyingmall.order.client.ProductInfo;
import com.muyingmall.order.client.ProductServiceClient;
import com.muyingmall.order.entity.Product;
import com.muyingmall.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商品服务实现类（通过Feign调用远程商品服务）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductServiceClient productServiceClient;

    @Override
    public Product getById(Long productId) {
        if (productId == null) {
            return null;
        }

        try {
            Result<ProductInfo> result = productServiceClient.getProduct(productId.intValue());
            if (result != null && result.isSuccess() && result.getData() != null) {
                return convertToProduct(result.getData());
            } else {
                log.warn("从商品服务获取商品信息失败: productId={}, result={}", productId, result);
                return null;
            }
        } catch (Exception e) {
            log.error("调用商品服务异常: productId={}", productId, e);
            return null;
        }
    }

    @Override
    public boolean checkStock(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        try {
            Product product = getById(productId);
            if (product == null) {
                log.warn("商品不存在，无法检查库存: productId={}", productId);
                return false;
            }

            boolean hasEnoughStock = product.getStock() >= quantity;
            log.debug("库存检查结果: productId={}, 需要数量={}, 当前库存={}, 结果={}", 
                     productId, quantity, product.getStock(), hasEnoughStock);
            return hasEnoughStock;
        } catch (Exception e) {
            log.error("检查库存异常: productId={}, quantity={}", productId, quantity, e);
            return false;
        }
    }

    @Override
    public boolean reduceStock(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        try {
            // 先检查库存
            if (!checkStock(productId, quantity)) {
                log.warn("库存不足，无法扣减: productId={}, quantity={}", productId, quantity);
                return false;
            }

            // 获取当前库存
            Product product = getById(productId);
            if (product == null) {
                log.warn("商品不存在，无法扣减库存: productId={}", productId);
                return false;
            }

            int newStock = product.getStock() - quantity;
            Result<Void> result = productServiceClient.updateStock(productId.intValue(), newStock);
            
            if (result != null && result.isSuccess()) {
                log.info("库存扣减成功: productId={}, quantity={}, 剩余库存={}", productId, quantity, newStock);
                return true;
            } else {
                log.error("库存扣减失败: productId={}, quantity={}, result={}", productId, quantity, result);
                return false;
            }
        } catch (Exception e) {
            log.error("扣减库存异常: productId={}, quantity={}", productId, quantity, e);
            return false;
        }
    }

    @Override
    public boolean restoreStock(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        try {
            // 获取当前库存
            Product product = getById(productId);
            if (product == null) {
                log.warn("商品不存在，无法恢复库存: productId={}", productId);
                return false;
            }

            int newStock = product.getStock() + quantity;
            Result<Void> result = productServiceClient.updateStock(productId.intValue(), newStock);
            
            if (result != null && result.isSuccess()) {
                log.info("库存恢复成功: productId={}, quantity={}, 当前库存={}", productId, quantity, newStock);
                return true;
            } else {
                log.error("库存恢复失败: productId={}, quantity={}, result={}", productId, quantity, result);
                return false;
            }
        } catch (Exception e) {
            log.error("恢复库存异常: productId={}, quantity={}", productId, quantity, e);
            return false;
        }
    }

    @Override
    public boolean update(LambdaUpdateWrapper<Product> updateWrapper) {
        // 这个方法在微服务架构中不太适用，因为我们不直接操作数据库
        // 如果需要更新，应该通过具体的业务方法调用远程服务
        log.warn("ProductService.update() 方法在微服务架构中不推荐使用，请使用具体的业务方法");
        return false;
    }

    /**
     * 将远程服务返回的ProductInfo转换为本地Product实体
     */
    private Product convertToProduct(ProductInfo productInfo) {
        if (productInfo == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productInfo.getId().longValue());
        product.setName(productInfo.getName());
        product.setPrice(productInfo.getPrice());
        product.setStock(productInfo.getStock());
        product.setImage(productInfo.getMainImage());
        
        // 状态转换：远程服务可能返回不同的状态值
        if ("上架".equals(productInfo.getStatus())) {
            product.setStatus("ACTIVE");
        } else if ("下架".equals(productInfo.getStatus())) {
            product.setStatus("INACTIVE");
        } else {
            // 兼容其他可能的状态值
            product.setStatus("ACTIVE".equalsIgnoreCase(productInfo.getStatus()) ? "ACTIVE" : "INACTIVE");
        }

        return product;
    }
}