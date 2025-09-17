package com.muyingmall.logistics.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.logistics.entity.Logistics;
import com.muyingmall.logistics.service.LogisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 物流控制器
 */
@Tag(name = "物流管理", description = "物流信息管理相关接口")
@RestController
@RequestMapping("/api/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService logisticsService;

    @Operation(summary = "创建物流信息")
    @PostMapping
    public ResponseEntity<Logistics> createLogistics(@RequestBody Logistics logistics) {
        return ResponseEntity.ok(logisticsService.save(logistics));
    }

    @Operation(summary = "根据ID获取物流信息")
    @GetMapping("/{id}")
    public ResponseEntity<Logistics> getLogisticsById(
            @Parameter(description = "物流ID") @PathVariable Long id) {
        Logistics logistics = logisticsService.getById(id);
        return logistics != null ? ResponseEntity.ok(logistics) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "根据订单号获取物流信息")
    @GetMapping("/order/{orderNo}")
    public ResponseEntity<Logistics> getLogisticsByOrderNo(
            @Parameter(description = "订单号") @PathVariable String orderNo) {
        Logistics logistics = logisticsService.getByOrderNo(orderNo);
        return logistics != null ? ResponseEntity.ok(logistics) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "根据物流单号获取物流信息")
    @GetMapping("/tracking/{trackingNo}")
    public ResponseEntity<Logistics> getLogisticsByTrackingNo(
            @Parameter(description = "物流单号") @PathVariable String trackingNo) {
        Logistics logistics = logisticsService.getByTrackingNo(trackingNo);
        return logistics != null ? ResponseEntity.ok(logistics) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "分页查询物流信息")
    @GetMapping("/page")
    public ResponseEntity<IPage<Logistics>> getLogisticsPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "物流状态") @RequestParam(required = false) String status,
            @Parameter(description = "物流公司ID") @RequestParam(required = false) Long companyId) {
        
        Page<Logistics> page = new Page<>(current, size);
        IPage<Logistics> result = logisticsService.getLogisticsPage(page, status, companyId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "更新物流信息")
    @PutMapping("/{id}")
    public ResponseEntity<Logistics> updateLogistics(
            @Parameter(description = "物流ID") @PathVariable Long id,
            @RequestBody Logistics logistics) {
        logistics.setId(id);
        return ResponseEntity.ok(logisticsService.updateById(logistics));
    }

    @Operation(summary = "删除物流信息")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogistics(
            @Parameter(description = "物流ID") @PathVariable Long id) {
        logisticsService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "同步物流跟踪信息")
    @PostMapping("/{id}/sync")
    public ResponseEntity<Logistics> syncLogisticsTrack(
            @Parameter(description = "物流ID") @PathVariable Long id) {
        Logistics logistics = logisticsService.syncTrackingInfo(id);
        return ResponseEntity.ok(logistics);
    }

    @Operation(summary = "批量同步物流信息")
    @PostMapping("/sync/batch")
    public ResponseEntity<Void> syncLogisticsBatch() {
        logisticsService.syncAllActiveLogistics();
        return ResponseEntity.ok().build();
    }
}