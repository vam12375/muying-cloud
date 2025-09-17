package com.muyingmall.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.muyingmall.admin.dto.AdminLoginDTO;
import com.muyingmall.admin.dto.AdminLoginParam;
import com.muyingmall.admin.entity.AdminLoginRecord;
import com.muyingmall.admin.entity.AdminOperationLog;
import com.muyingmall.admin.service.AdminLoginRecordService;
import com.muyingmall.admin.service.AdminOperationLogService;
import com.muyingmall.admin.service.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 管理员控制器
 */
@Tag(name = "管理员管理", description = "后台管理员相关接口")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminLoginRecordService loginRecordService;
    private final AdminOperationLogService operationLogService;
    private final ExcelExportService excelExportService;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public ResponseEntity<AdminLoginDTO> login(@RequestBody AdminLoginParam loginParam) {
        AdminLoginDTO loginResult = loginRecordService.login(loginParam);
        return ResponseEntity.ok(loginResult);
    }

    @Operation(summary = "管理员登出")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "管理员ID") @RequestParam Long adminId) {
        loginRecordService.logout(adminId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取管理员登录记录")
    @GetMapping("/login-records")
    public ResponseEntity<IPage<AdminLoginRecord>> getLoginRecords(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "管理员ID") @RequestParam(required = false) Long adminId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        
        Page<AdminLoginRecord> page = new Page<>(current, size);
        IPage<AdminLoginRecord> result = loginRecordService.getLoginRecordsPage(page, adminId, startTime, endTime);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取操作日志")
    @GetMapping("/operation-logs")
    public ResponseEntity<IPage<AdminOperationLog>> getOperationLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "管理员ID") @RequestParam(required = false) Long adminId,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operationType,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        
        Page<AdminOperationLog> page = new Page<>(current, size);
        IPage<AdminOperationLog> result = operationLogService.getOperationLogsPage(page, adminId, operationType, startTime, endTime);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "记录操作日志")
    @PostMapping("/operation-logs")
    public ResponseEntity<AdminOperationLog> recordOperationLog(
            @RequestBody AdminOperationLog operationLog) {
        return ResponseEntity.ok(operationLogService.save(operationLog));
    }

    @Operation(summary = "导出登录记录")
    @GetMapping("/export/login-records")
    public void exportLoginRecords(
            @Parameter(description = "管理员ID") @RequestParam(required = false) Long adminId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endTime,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=login-records.xlsx");
        
        excelExportService.exportLoginRecords(adminId, startTime, endTime, response.getOutputStream());
    }

    @Operation(summary = "导出操作日志")
    @GetMapping("/export/operation-logs")
    public void exportOperationLogs(
            @Parameter(description = "管理员ID") @RequestParam(required = false) Long adminId,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operationType,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endTime,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operation-logs.xlsx");
        
        excelExportService.exportOperationLogs(adminId, operationType, startTime, endTime, response.getOutputStream());
    }

    @Operation(summary = "获取系统统计信息")
    @GetMapping("/stats/system")
    public ResponseEntity<Object> getSystemStats() {
        Object stats = operationLogService.getSystemStats();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "获取在线管理员列表")
    @GetMapping("/online")
    public ResponseEntity<Object> getOnlineAdmins() {
        Object onlineAdmins = loginRecordService.getOnlineAdmins();
        return ResponseEntity.ok(onlineAdmins);
    }

    @Operation(summary = "强制下线管理员")
    @PostMapping("/offline/{adminId}")
    public ResponseEntity<Void> forceOffline(
            @Parameter(description = "管理员ID") @PathVariable Long adminId) {
        loginRecordService.forceOffline(adminId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "清理过期登录记录")
    @PostMapping("/cleanup/login-records")
    public ResponseEntity<Integer> cleanupExpiredLoginRecords(
            @Parameter(description = "保留天数") @RequestParam(defaultValue = "90") int retentionDays) {
        int cleanedCount = loginRecordService.cleanupExpiredRecords(retentionDays);
        return ResponseEntity.ok(cleanedCount);
    }

    @Operation(summary = "清理过期操作日志")
    @PostMapping("/cleanup/operation-logs")
    public ResponseEntity<Integer> cleanupExpiredOperationLogs(
            @Parameter(description = "保留天数") @RequestParam(defaultValue = "180") int retentionDays) {
        int cleanedCount = operationLogService.cleanupExpiredLogs(retentionDays);
        return ResponseEntity.ok(cleanedCount);
    }
}