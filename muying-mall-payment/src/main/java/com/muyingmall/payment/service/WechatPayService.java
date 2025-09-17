package com.muyingmall.payment.service;

import com.muyingmall.payment.entity.Payment;
import com.muyingmall.payment.entity.Refund;

import java.util.Map;

/**
 * 微信支付服务接口
 */
public interface WechatPayService {

    /**
     * 创建微信支付订单
     *
     * @param payment 支付信息
     * @return 支付参数，包含预支付ID等
     * @throws Exception 微信支付API异常
     */
    Map<String, Object> createWechatPayment(Payment payment) throws Exception;

    /**
     * 查询微信支付订单状态
     *
     * @param outTradeNo 商户订单号
     * @return 订单状态
     * @throws Exception 微信支付API异常
     */
    String queryPaymentStatus(String outTradeNo) throws Exception;

    /**
     * 处理微信支付异步通知
     *
     * @param notifyData 通知数据
     * @return 处理结果
     * @throws Exception 处理异常
     */
    boolean handlePaymentNotify(String notifyData) throws Exception;

    /**
     * 微信支付退款
     *
     * @param refund 退款信息
     * @return 退款结果
     * @throws Exception 微信支付API异常
     */
    String refund(Refund refund) throws Exception;

    /**
     * 查询微信退款状态
     *
     * @param refundNo 退款单号
     * @return 退款状态
     * @throws Exception 微信支付API异常
     */
    String queryRefundStatus(String refundNo) throws Exception;

    /**
     * 处理微信退款异步通知
     *
     * @param notifyData 通知数据
     * @return 处理结果
     * @throws Exception 处理异常
     */
    boolean handleRefundNotify(String notifyData) throws Exception;

    /**
     * 关闭微信支付订单
     *
     * @param outTradeNo 商户订单号
     * @return 是否成功
     * @throws Exception 微信支付API异常
     */
    boolean closeOrder(String outTradeNo) throws Exception;

    /**
     * 验证微信支付签名
     *
     * @param data 待验证数据
     * @param signature 签名
     * @return 验证结果
     * @throws Exception 验证异常
     */
    boolean verifySignature(String data, String signature) throws Exception;

    /**
     * 生成微信支付签名
     *
     * @param data 待签名数据
     * @return 签名
     * @throws Exception 签名异常
     */
    String generateSignature(String data) throws Exception;
}