package com.muyingmall.points.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.points.entity.PointsAccount;
import com.muyingmall.points.entity.PointsProduct;
import com.muyingmall.points.service.PointsAccountService;
import com.muyingmall.points.service.PointsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 积分控制器
 */
@Tag(name = "积分管理", description = "用户积分管理相关接口")
@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointsController {

    private final PointsAccountService pointsAccountService;
    private final PointsProductService pointsProductService;

    @Operation(summary = "获取用户积分账户")
    @GetMapping("/account/{userId}")
    public ResponseEntity<PointsAccount> getPointsAccount(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        PointsAccount account = pointsAccountService.getByUserId(userId);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "创建积分账户")
    @PostMapping("/account")
    public ResponseEntity<PointsAccount> createPointsAccount(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        PointsAccount account = pointsAccountService.createAccount(userId);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "积分充值")
    @PostMapping("/account/{userId}/recharge")
    public ResponseEntity<PointsAccount> rechargePoints(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "充值积分") @RequestParam BigDecimal points,
            @Parameter(description = "充值类型") @RequestParam String type,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        
        PointsAccount account = pointsAccountService.rechargePoints(userId, points, type, remark);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "积分消费")
    @PostMapping("/account/{userId}/consume")
    public ResponseEntity<PointsAccount> consumePoints(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "消费积分") @RequestParam BigDecimal points,
            @Parameter(description = "消费类型") @RequestParam String type,
            @Parameter(description = "业务订单号") @RequestParam(required = false) String businessOrderNo,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        
        PointsAccount account = pointsAccountService.consumePoints(userId, points, type, businessOrderNo, remark);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "冻结积分")
    @PostMapping("/account/{userId}/freeze")
    public ResponseEntity<PointsAccount> freezePoints(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "冻结积分") @RequestParam BigDecimal points,
            @Parameter(description = "冻结原因") @RequestParam String reason) {
        
        PointsAccount account = pointsAccountService.freezePoints(userId, points, reason);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "解冻积分")
    @PostMapping("/account/{userId}/unfreeze")
    public ResponseEntity<PointsAccount> unfreezePoints(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "解冻积分") @RequestParam BigDecimal points,
            @Parameter(description = "解冻原因") @RequestParam String reason) {
        
        PointsAccount account = pointsAccountService.unfreezePoints(userId, points, reason);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "获取积分商品列表")
    @GetMapping("/products")
    public ResponseEntity<IPage<PointsProduct>> getPointsProducts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "商品状态") @RequestParam(required = false) String status,
            @Parameter(description = "商品分类") @RequestParam(required = false) String category) {
        
        Page<PointsProduct> page = new Page<>(current, size);
        IPage<PointsProduct> result = pointsProductService.getProductsPage(page, status, category);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据ID获取积分商品")
    @GetMapping("/products/{id}")
    public ResponseEntity<PointsProduct> getPointsProductById(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        PointsProduct product = pointsProductService.getById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "积分兑换商品")
    @PostMapping("/exchange")
    public ResponseEntity<String> exchangeProduct(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "商品ID") @RequestParam Long productId,
            @Parameter(description = "兑换数量") @RequestParam(defaultValue = "1") Integer quantity,
            @Parameter(description = "收货地址") @RequestParam String address) {
        
        String exchangeOrderNo = pointsProductService.exchangeProduct(userId, productId, quantity, address);
        return ResponseEntity.ok(exchangeOrderNo);
    }

    @Operation(summary = "创建积分商品")
    @PostMapping("/products")
    public ResponseEntity<PointsProduct> createPointsProduct(@RequestBody PointsProduct product) {
        return ResponseEntity.ok(pointsProductService.save(product));
    }

    @Operation(summary = "更新积分商品")
    @PutMapping("/products/{id}")
    public ResponseEntity<PointsProduct> updatePointsProduct(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @RequestBody PointsProduct product) {
        product.setId(id);
        return ResponseEntity.ok(pointsProductService.updateById(product));
    }

    @Operation(summary = "删除积分商品")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deletePointsProduct(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        pointsProductService.removeById(id);
        return ResponseEntity.ok().build();
    }
}