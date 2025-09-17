package com.muyingmall.user.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝配置类
 */
@Configuration
@Slf4j
public class AlipayConfig {

    @Value("${alipay.app-id:}")
    private String appId;

    @Value("${alipay.private-key:}")
    private String privateKey;

    @Value("${alipay.public-key:}")
    private String publicKey;

    @Value("${alipay.alipay-public-key:}")
    private String alipayPublicKey;

    @Value("${alipay.gateway-url:https://openapi.alipay.com/gateway.do}")
    private String gatewayUrl;

    @Value("${alipay.format:json}")
    private String format;

    @Value("${alipay.charset:utf-8}")
    private String charset;

    @Value("${alipay.sign-type:RSA2}")
    private String signType;

    @Value("${alipay.notify-url:}")
    private String notifyUrl;

    @Value("${alipay.return-url:}")
    private String returnUrl;

    @Bean
    public AlipayClient alipayClient() {
        try {
            if (appId == null || appId.isEmpty()) {
                log.warn("支付宝配置未设置，创建默认AlipayClient");
                // 创建一个默认的客户端，避免启动失败
                return new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    "default", "default", "json", "utf-8", "default", "RSA2");
            }

            return new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
        } catch (Exception e) {
            log.error("初始化AlipayClient失败", e);
            // 返回默认客户端以避免启动失败
            return new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                "default", "default", "json", "utf-8", "default", "RSA2");
        }
    }

    // Getters for configuration values
    public String getAppId() {
        return appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public String getFormat() {
        return format;
    }

    public String getCharset() {
        return charset;
    }

    public String getSignType() {
        return signType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
}
