package com.muyingmall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.fastjson2.JSON;
import com.muyingmall.common.dto.Result;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SentinelGatewayConfig {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public SentinelGatewayConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        initGatewayRules();
        initBlockHandlers();
    }

    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // 用户服务API
        Set<ApiPredicateItem> userPredicates = new HashSet<>();
        userPredicates.add(new ApiPathPredicateItem().setPattern("/api/users/**"));
        ApiDefinition userApi = new ApiDefinition("user-api")
            .setPredicateItems(userPredicates);
        definitions.add(userApi);

        // 商品服务API
        Set<ApiPredicateItem> productPredicates = new HashSet<>();
        productPredicates.add(new ApiPathPredicateItem().setPattern("/api/products/**"));
        productPredicates.add(new ApiPathPredicateItem().setPattern("/api/categories/**"));
        productPredicates.add(new ApiPathPredicateItem().setPattern("/api/brands/**"));
        ApiDefinition productApi = new ApiDefinition("product-api")
            .setPredicateItems(productPredicates);
        definitions.add(productApi);

        // 订单服务API
        Set<ApiPredicateItem> orderPredicates = new HashSet<>();
        orderPredicates.add(new ApiPathPredicateItem().setPattern("/api/orders/**"));
        orderPredicates.add(new ApiPathPredicateItem().setPattern("/api/cart/**"));
        ApiDefinition orderApi = new ApiDefinition("order-api")
            .setPredicateItems(orderPredicates);
        definitions.add(orderApi);

        // 支付服务API
        Set<ApiPredicateItem> paymentPredicates = new HashSet<>();
        paymentPredicates.add(new ApiPathPredicateItem().setPattern("/api/payments/**"));
        ApiDefinition paymentApi = new ApiDefinition("payment-api")
            .setPredicateItems(paymentPredicates);
        definitions.add(paymentApi);

        // 搜索服务API
        Set<ApiPredicateItem> searchPredicates = new HashSet<>();
        searchPredicates.add(new ApiPathPredicateItem().setPattern("/api/search/**"));
        ApiDefinition searchApi = new ApiDefinition("search-api")
            .setPredicateItems(searchPredicates);
        definitions.add(searchApi);

        // 物流服务API
        Set<ApiPredicateItem> logisticsPredicates = new HashSet<>();
        logisticsPredicates.add(new ApiPathPredicateItem().setPattern("/api/logistics/**"));
        ApiDefinition logisticsApi = new ApiDefinition("logistics-api")
            .setPredicateItems(logisticsPredicates);
        definitions.add(logisticsApi);

        // 评价服务API
        Set<ApiPredicateItem> commentPredicates = new HashSet<>();
        commentPredicates.add(new ApiPathPredicateItem().setPattern("/api/comments/**"));
        ApiDefinition commentApi = new ApiDefinition("comment-api")
            .setPredicateItems(commentPredicates);
        definitions.add(commentApi);

        // 积分服务API
        Set<ApiPredicateItem> pointsPredicates = new HashSet<>();
        pointsPredicates.add(new ApiPathPredicateItem().setPattern("/api/points/**"));
        ApiDefinition pointsApi = new ApiDefinition("points-api")
            .setPredicateItems(pointsPredicates);
        definitions.add(pointsApi);

        // 管理后台API
        Set<ApiPredicateItem> adminPredicates = new HashSet<>();
        adminPredicates.add(new ApiPathPredicateItem().setPattern("/api/admin/**"));
        ApiDefinition adminApi = new ApiDefinition("admin-api")
            .setPredicateItems(adminPredicates);
        definitions.add(adminApi);

        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        
        // 用户服务限流规则
        rules.add(new GatewayFlowRule("user-api")
            .setCount(100) // QPS
            .setIntervalSec(1)
        );
        
        // 商品服务限流规则
        rules.add(new GatewayFlowRule("product-api")
            .setCount(200) // QPS
            .setIntervalSec(1)
        );
        
        // 订单服务限流规则
        rules.add(new GatewayFlowRule("order-api")
            .setCount(50) // QPS
            .setIntervalSec(1)
        );
        
        // 支付服务限流规则（更严格）
        rules.add(new GatewayFlowRule("payment-api")
            .setCount(20) // QPS
            .setIntervalSec(1)
        );
        
        // 搜索服务限流规则
        rules.add(new GatewayFlowRule("search-api")
            .setCount(150) // QPS
            .setIntervalSec(1)
        );
        
        // 管理后台限流规则（更严格）
        rules.add(new GatewayFlowRule("admin-api")
            .setCount(30) // QPS
            .setIntervalSec(1)
        );
        
        GatewayRuleManager.loadRules(rules);
    }

    private void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (exchange, t) -> {
            Result<Object> result = Result.error(429, "请求过于频繁，请稍后重试");
            String bodyStr = JSON.toJSONString(result);
            
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyStr));
        };
        
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}