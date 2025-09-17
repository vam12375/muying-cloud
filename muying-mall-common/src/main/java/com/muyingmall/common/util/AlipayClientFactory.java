package com.muyingmall.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 支付宝客户端工厂类
 * 只有在启用支付宝支付时才生效
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "muying-mall.payment.alipay.enabled", havingValue = "true", matchIfMissing = false)
public class AlipayClientFactory {

    private AlipayClient alipayClient;

    /**
     * 获取AlipayClient实例
     * 暂时返回null，需要配置支付宝相关参数
     */
    public AlipayClient getAlipayClient() {
        log.warn("支付宝客户端暂未配置，请在配置文件中设置相关参数");
        return null;
    }
}