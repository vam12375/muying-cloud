package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索服务API接口
 */
@FeignClient(name = "muying-mall-search", path = "/search")
public interface SearchServiceApi {

    /**
     * 搜索商品
     */
    @GetMapping("/products")
    Result<SearchResultDto> searchProducts(@RequestParam("keyword") String keyword,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size);

    /**
     * 根据分类搜索商品
     */
    @GetMapping("/products/by-category")
    Result<SearchResultDto> searchByCategory(@RequestParam("categoryId") Long categoryId,
                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size);

    /**
     * 获取热门搜索词
     */
    @GetMapping("/hot-keywords")
    Result<java.util.List<String>> getHotKeywords();

    /**
     * 同步商品索引
     */
    @PostMapping("/sync/product/{productId}")
    Result<Boolean> syncProductIndex(@PathVariable("productId") Long productId);

    /**
     * 批量同步商品索引
     */
    @PostMapping("/sync/products")
    Result<Boolean> batchSyncProducts(@RequestBody java.util.List<Long> productIds);

    /**
     * 删除商品索引
     */
    @DeleteMapping("/index/product/{productId}")
    Result<Boolean> deleteProductIndex(@PathVariable("productId") Long productId);
}

/**
 * 搜索结果DTO
 */
class SearchResultDto {
    private java.util.List<ProductSearchDto> products;
    private Long total;
    private Integer page;
    private Integer size;
    // getters and setters
}

/**
 * 商品搜索DTO
 */
class ProductSearchDto {
    private Long productId;
    private String productName;
    private java.math.BigDecimal price;
    private String image;
    private Integer salesCount;
    private Double score;
    // getters and setters
}