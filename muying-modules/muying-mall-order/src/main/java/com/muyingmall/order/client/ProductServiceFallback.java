package com.muyingmall.order.client;

import com.muyingmall.common.dto.Result;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceFallback implements ProductServiceClient {
    
    @Override
    public Result<ProductInfo> getProduct(Integer id) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Void> updateStock(Integer id, Integer stock) {
        return Result.error("商品服务暂时不可用，无法更新库存");
    }
}