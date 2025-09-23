package com.muyingmall.user.feign;

import com.muyingmall.user.feign.dto.PointsProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("muying-mall-points")
public interface PointsServiceFeignClient {

    @GetMapping("/products/{id}")
    PointsProductDTO getPointsProductById(@PathVariable("id") Long id);
}
