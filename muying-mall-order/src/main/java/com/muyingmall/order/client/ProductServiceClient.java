package com.muyingmall.order.client;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "muying-mall-product", fallback = ProductServiceFallback.class)
public interface ProductServiceClient {
    
    @GetMapping("/products/{id}")
    Result<ProductInfo> getProduct(@PathVariable("id") Integer id);
    
    @PutMapping("/products/{id}/stock")
    Result<Void> updateStock(@PathVariable("id") Integer id, @RequestParam("stock") Integer stock);
}
