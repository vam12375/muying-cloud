package com.muyingmall.user.feign;

import com.muyingmall.user.feign.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("muying-mall-product")
public interface ProductServiceFeignClient {

    @GetMapping("/product/{id}")
    ProductDTO getProductById(@PathVariable("id") Integer id);
}
