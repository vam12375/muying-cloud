package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 订单服务API接口
 */
@FeignClient(name = "muying-mall-order", path = "/order")
public interface OrderServiceApi {

    /**
     * 根据订单ID获取订单信息
     */
    @GetMapping("/info/{orderId}")
    Result<OrderDto> getOrderById(@PathVariable("orderId") Long orderId);

    /**
     * 根据用户ID获取订单列表
     */
    @GetMapping("/list/by-user/{userId}")
    Result<java.util.List<OrderDto>> getOrdersByUserId(@PathVariable("userId") Long userId);

    /**
     * 创建订单
     */
    @PostMapping("/create")
    Result<OrderDto> createOrder(@RequestBody CreateOrderRequest request);

    /**
     * 更新订单状态
     */
    @PostMapping("/status/update")
    Result<Boolean> updateOrderStatus(@RequestParam("orderId") Long orderId,
                                     @RequestParam("status") String status);

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderId}")
    Result<Boolean> cancelOrder(@PathVariable("orderId") Long orderId);

    /**
     * 获取用户购物车
     */
    @GetMapping("/cart/{userId}")
    Result<java.util.List<CartItemDto>> getUserCart(@PathVariable("userId") Long userId);

    /**
     * 添加商品到购物车
     */
    @PostMapping("/cart/add")
    Result<Boolean> addToCart(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId,
                             @RequestParam("quantity") Integer quantity);

    /**
     * 清空购物车
     */
    @PostMapping("/cart/clear/{userId}")
    Result<Boolean> clearCart(@PathVariable("userId") Long userId);

    /**
     * 获取可用优惠券
     */
    @GetMapping("/coupon/available/{userId}")
    Result<java.util.List<CouponDto>> getAvailableCoupons(@PathVariable("userId") Long userId);
}

/**
 * 订单信息DTO
 */
class OrderDto {
    private Long orderId;
    private Long userId;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String orderTime;
    private java.util.List<OrderItemDto> orderItems;
    // getters and setters
}

/**
 * 订单项DTO
 */
class OrderItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private java.math.BigDecimal price;
    // getters and setters
}

/**
 * 创建订单请求DTO
 */
class CreateOrderRequest {
    private Long userId;
    private Long addressId;
    private Long couponId;
    private java.util.List<OrderItemDto> items;
    // getters and setters
}

/**
 * 购物车项DTO
 */
class CartItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private java.math.BigDecimal price;
    // getters and setters
}

/**
 * 优惠券DTO
 */
class CouponDto {
    private Long couponId;
    private String couponName;
    private java.math.BigDecimal discountAmount;
    private String expireTime;
    // getters and setters
}