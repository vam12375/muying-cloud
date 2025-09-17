package com.muyingmall.product.controller;

import com.muyingmall.common.dto.Result;
import com.muyingmall.product.entity.InventoryLog;
import com.muyingmall.product.entity.Product;
import com.muyingmall.product.service.InventoryService;
import com.muyingmall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Validated
@Tag(name = "库存管理", description = "商品库存管理相关接口")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    /**
     * 获取商品库存信息
     */
    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品库存信息")
    public Result<Integer> getProductInventory(
            @Parameter(description = "商品ID") @PathVariable @NotNull Integer productId) {
        try {
            Product product = productService.getById(productId);
            if (product == null) {
                return Result.error("商品不存在");
            }
            return Result.success(product.getStock());
        } catch (Exception e) {
            log.error("获取商品库存失败", e);
            return Result.error("获取库存信息失败: " + e.getMessage());
        }
    }

    /**
     * 增加库存
     */
    @PostMapping("/increase")
    @Operation(summary = "增加库存")
    public Result<Boolean> increaseInventory(
            @Parameter(description = "商品ID") @RequestParam @NotNull Integer productId,
            @Parameter(description = "增加数量") @RequestParam @NotNull @Min(1) Integer quantity,
            @Parameter(description = "操作原因") @RequestParam String reason) {
        try {
            boolean success = inventoryService.increaseStock(productId, quantity, reason);
            if (success) {
                log.info("库存增加成功: productId={}, quantity={}, reason={}", productId, quantity, reason);
                return Result.success("库存增加成功", true);
            } else {
                return Result.error("库存增加失败");
            }
        } catch (Exception e) {
            log.error("增加库存失败", e);
            return Result.error("增加库存失败: " + e.getMessage());
        }
    }

    /**
     * 减少库存
     */
    @PostMapping("/decrease")
    @Operation(summary = "减少库存")
    public Result<Boolean> decreaseInventory(
            @Parameter(description = "商品ID") @RequestParam @NotNull Integer productId,
            @Parameter(description = "减少数量") @RequestParam @NotNull @Min(1) Integer quantity,
            @Parameter(description = "操作原因") @RequestParam String reason) {
        try {
            boolean success = inventoryService.decreaseStock(productId, quantity, reason);
            if (success) {
                log.info("库存减少成功: productId={}, quantity={}, reason={}", productId, quantity, reason);
                return Result.success("库存减少成功", true);
            } else {
                return Result.error("库存减少失败，可能库存不足");
            }
        } catch (Exception e) {
            log.error("减少库存失败", e);
            return Result.error("减少库存失败: " + e.getMessage());
        }
    }

    /**
     * 设置库存
     */
    @PostMapping("/set")
    @Operation(summary = "设置库存")
    public Result<Boolean> setInventory(
            @Parameter(description = "商品ID") @RequestParam @NotNull Integer productId,
            @Parameter(description = "设置数量") @RequestParam @NotNull @Min(0) Integer quantity,
            @Parameter(description = "操作原因") @RequestParam String reason) {
        try {
            boolean success = inventoryService.setStock(productId, quantity, reason);
            if (success) {
                log.info("库存设置成功: productId={}, quantity={}, reason={}", productId, quantity, reason);
                return Result.success("库存设置成功", true);
            } else {
                return Result.error("库存设置失败");
            }
        } catch (Exception e) {
            log.error("设置库存失败", e);
            return Result.error("设置库存失败: " + e.getMessage());
        }
    }

    /**
     * 获取库存变动记录
     */
    @GetMapping("/logs/{productId}")
    @Operation(summary = "获取库存变动记录")
    public Result<List<InventoryLog>> getInventoryLogs(
            @Parameter(description = "商品ID") @PathVariable @NotNull Integer productId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size) {
        try {
            List<InventoryLog> logs = inventoryService.getInventoryLogs(productId, page, size);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("获取库存变动记录失败", e);
            return Result.error("获取库存变动记录失败: " + e.getMessage());
        }
    }

    /**
     * 检查低库存商品
     */
    @GetMapping("/low-stock")
    @Operation(summary = "检查低库存商品")
    public Result<List<Product>> checkLowStockProducts(
            @Parameter(description = "库存阈值") @RequestParam(defaultValue = "10") Integer threshold) {
        try {
            List<Product> lowStockProducts = inventoryService.getLowStockProducts(threshold);
            return Result.success(lowStockProducts);
        } catch (Exception e) {
            log.error("检查低库存商品失败", e);
            return Result.error("检查低库存商品失败: " + e.getMessage());
        }
    }
}