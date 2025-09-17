package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 商品服务API接口
 */
@FeignClient(name = "muying-mall-product", path = "/product")
public interface ProductServiceApi {

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/info/{productId}")
    Result<ProductDto> getProductById(@PathVariable("productId") Long productId);

    /**
     * 批量获取商品信息
     */
    @PostMapping("/info/batch")
    Result<java.util.List<ProductDto>> getProductsByIds(@RequestBody java.util.List<Long> productIds);

    /**
     * 检查商品库存
     */
    @GetMapping("/stock/{productId}")
    Result<Integer> getProductStock(@PathVariable("productId") Long productId);

    /**
     * 扣减商品库存
     */
    @PostMapping("/stock/deduct")
    Result<Boolean> deductStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);

    /**
     * 恢复商品库存
     */
    @PostMapping("/stock/restore")
    Result<Boolean> restoreStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);

    /**
     * 验证商品是否存在且可用
     */
    @GetMapping("/validate/{productId}")
    Result<Boolean> validateProduct(@PathVariable("productId") Long productId);

    /**
     * 获取商品分类信息
     */
    @GetMapping("/category/{categoryId}")
    Result<CategoryDto> getCategoryById(@PathVariable("categoryId") Long categoryId);

    /**
     * 获取品牌信息
     */
    @GetMapping("/brand/{brandId}")
    Result<BrandDto> getBrandById(@PathVariable("brandId") Long brandId);

    /**
     * 根据分类ID获取商品列表
     */
    @GetMapping("/list/by-category/{categoryId}")
    Result<java.util.List<ProductDto>> getProductsByCategory(@PathVariable("categoryId") Long categoryId);
}

/**
 * 商品信息DTO
 */
class ProductDto {
    private Long productId;
    private String productName;
    private java.math.BigDecimal price;
    private Integer stock;
    private String status;
    private Long categoryId;
    private Long brandId;
    // getters and setters
}

/**
 * 分类信息DTO
 */
class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private String description;
    // getters and setters
}

/**
 * 品牌信息DTO
 */
class BrandDto {
    private Long brandId;
    private String brandName;
    private String description;
    // getters and setters
}