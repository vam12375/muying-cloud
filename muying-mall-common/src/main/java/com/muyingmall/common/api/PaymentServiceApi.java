package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 支付服务API接口
 */
@FeignClient(name = "muying-mall-payment", path = "/payment")
public interface PaymentServiceApi {

    /**
     * 创建支付
     */
    @PostMapping("/create")
    Result<PaymentDto> createPayment(@RequestBody CreatePaymentRequest request);

    /**
     * 根据支付ID获取支付信息
     */
    @GetMapping("/info/{paymentId}")
    Result<PaymentDto> getPaymentById(@PathVariable("paymentId") Long paymentId);

    /**
     * 根据订单ID获取支付信息
     */
    @GetMapping("/info/by-order/{orderId}")
    Result<PaymentDto> getPaymentByOrderId(@PathVariable("orderId") Long orderId);

    /**
     * 更新支付状态
     */
    @PostMapping("/status/update")
    Result<Boolean> updatePaymentStatus(@RequestParam("paymentId") Long paymentId,
                                       @RequestParam("status") String status);

    /**
     * 处理支付回调
     */
    @PostMapping("/callback/alipay")
    Result<Boolean> handleAlipayCallback(@RequestBody String callbackData);

    /**
     * 创建退款
     */
    @PostMapping("/refund/create")
    Result<RefundDto> createRefund(@RequestBody CreateRefundRequest request);

    /**
     * 获取退款信息
     */
    @GetMapping("/refund/info/{refundId}")
    Result<RefundDto> getRefundById(@PathVariable("refundId") Long refundId);

    /**
     * 处理退款
     */
    @PostMapping("/refund/process")
    Result<Boolean> processRefund(@RequestParam("refundId") Long refundId);

    /**
     * 钱包支付
     */
    @PostMapping("/wallet/pay")
    Result<Boolean> walletPayment(@RequestParam("userId") Long userId,
                                 @RequestParam("amount") java.math.BigDecimal amount,
                                 @RequestParam("orderId") Long orderId);
}

/**
 * 支付信息DTO
 */
class PaymentDto {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private java.math.BigDecimal amount;
    private String paymentMethod;
    private String status;
    private String createTime;
    // getters and setters
}

/**
 * 创建支付请求DTO
 */
class CreatePaymentRequest {
    private Long orderId;
    private Long userId;
    private java.math.BigDecimal amount;
    private String paymentMethod;
    // getters and setters
}

/**
 * 退款信息DTO
 */
class RefundDto {
    private Long refundId;
    private Long paymentId;
    private Long orderId;
    private java.math.BigDecimal refundAmount;
    private String reason;
    private String status;
    private String createTime;
    // getters and setters
}

/**
 * 创建退款请求DTO
 */
class CreateRefundRequest {
    private Long paymentId;
    private Long orderId;
    private java.math.BigDecimal refundAmount;
    private String reason;
    // getters and setters
}