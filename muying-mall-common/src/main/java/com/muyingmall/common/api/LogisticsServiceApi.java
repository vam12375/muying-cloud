package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 物流服务API接口
 */
@FeignClient(name = "muying-mall-logistics", path = "/logistics")
public interface LogisticsServiceApi {

    /**
     * 创建物流记录
     */
    @PostMapping("/create")
    Result<LogisticsDto> createLogistics(@RequestBody CreateLogisticsRequest request);

    /**
     * 根据订单ID获取物流信息
     */
    @GetMapping("/info/by-order/{orderId}")
    Result<LogisticsDto> getLogisticsByOrderId(@PathVariable("orderId") Long orderId);

    /**
     * 更新物流状态
     */
    @PostMapping("/status/update")
    Result<Boolean> updateLogisticsStatus(@RequestParam("logisticsId") Long logisticsId,
                                         @RequestParam("status") String status);

    /**
     * 获取物流追踪信息
     */
    @GetMapping("/track/{logisticsId}")
    Result<java.util.List<LogisticsTrackDto>> getLogisticsTrack(@PathVariable("logisticsId") Long logisticsId);

    /**
     * 添加物流追踪记录
     */
    @PostMapping("/track/add")
    Result<Boolean> addLogisticsTrack(@RequestBody LogisticsTrackDto trackDto);

    /**
     * 获取物流公司列表
     */
    @GetMapping("/company/list")
    Result<java.util.List<LogisticsCompanyDto>> getLogisticsCompanies();
}

/**
 * 物流信息DTO
 */
class LogisticsDto {
    private Long logisticsId;
    private Long orderId;
    private String trackingNumber;
    private String companyCode;
    private String companyName;
    private String status;
    private String createTime;
    // getters and setters
}

/**
 * 创建物流请求DTO
 */
class CreateLogisticsRequest {
    private Long orderId;
    private String companyCode;
    private String trackingNumber;
    // getters and setters
}

/**
 * 物流追踪DTO
 */
class LogisticsTrackDto {
    private Long logisticsId;
    private String status;
    private String description;
    private String location;
    private String trackTime;
    // getters and setters
}

/**
 * 物流公司DTO
 */
class LogisticsCompanyDto {
    private String companyCode;
    private String companyName;
    private String website;
    private String phone;
    // getters and setters
}