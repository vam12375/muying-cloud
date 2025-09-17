package com.muyingmall.user.feign;

import com.muyingmall.user.feign.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("muying-mall-order")
public interface OrderServiceFeignClient {

    @GetMapping("/order/{id}")
    OrderDTO getOrderById(@PathVariable("id") Integer id);
}
