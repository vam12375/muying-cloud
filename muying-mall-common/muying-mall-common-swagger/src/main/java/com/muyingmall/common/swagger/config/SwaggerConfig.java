package com.muyingmall.common.swagger.config;

import com.muyingmall.common.swagger.properties.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger配置类
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "muying.swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    /**
     * 创建OpenAPI配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers());
    }

    /**
     * 创建API信息
     */
    private Info createApiInfo() {
        SwaggerProperties.Contact contactProps = swaggerProperties.getContact();
        SwaggerProperties.License licenseProps = swaggerProperties.getLicense();

        Contact contact = new Contact()
                .name(contactProps.getName())
                .email(contactProps.getEmail())
                .url(contactProps.getUrl());

        License license = new License()
                .name(licenseProps.getName())
                .url(licenseProps.getUrl());

        return new Info()
                .title(swaggerProperties.getTitle())
                .version(swaggerProperties.getVersion())
                .description(swaggerProperties.getDescription())
                .termsOfService(swaggerProperties.getTermsOfServiceUrl())
                .contact(contact)
                .license(license);
    }

    /**
     * 创建服务器信息
     */
    private List<Server> createServers() {
        SwaggerProperties.Server serverProps = swaggerProperties.getServer();
        
        Server server = new Server()
                .url(serverProps.getUrl())
                .description(serverProps.getDescription());

        return List.of(server);
    }

    /**
     * 创建默认分组API
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi defaultApi() {
        SwaggerProperties.Group groupProps = swaggerProperties.getGroup();
        
        return GroupedOpenApi.builder()
                .group(groupProps.getDefaultGroup())
                .packagesToScan(groupProps.getBasePackage())
                .build();
    }

    /**
     * 创建用户服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户服务")
                .packagesToScan("com.muyingmall.user")
                .pathsToMatch("/api/user/**")
                .build();
    }

    /**
     * 创建商品服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("商品服务")
                .packagesToScan("com.muyingmall.product")
                .pathsToMatch("/api/product/**")
                .build();
    }

    /**
     * 创建订单服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("订单服务")
                .packagesToScan("com.muyingmall.order")
                .pathsToMatch("/api/order/**")
                .build();
    }

    /**
     * 创建支付服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("支付服务")
                .packagesToScan("com.muyingmall.payment")
                .pathsToMatch("/api/payment/**")
                .build();
    }

    /**
     * 创建搜索服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi searchApi() {
        return GroupedOpenApi.builder()
                .group("搜索服务")
                .packagesToScan("com.muyingmall.search")
                .pathsToMatch("/api/search/**")
                .build();
    }

    /**
     * 创建物流服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi logisticsApi() {
        return GroupedOpenApi.builder()
                .group("物流服务")
                .packagesToScan("com.muyingmall.logistics")
                .pathsToMatch("/api/logistics/**")
                .build();
    }

    /**
     * 创建评论服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder()
                .group("评论服务")
                .packagesToScan("com.muyingmall.comment")
                .pathsToMatch("/api/comment/**")
                .build();
    }

    /**
     * 创建积分服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi pointsApi() {
        return GroupedOpenApi.builder()
                .group("积分服务")
                .packagesToScan("com.muyingmall.points")
                .pathsToMatch("/api/points/**")
                .build();
    }

    /**
     * 创建管理服务API分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.swagger.group", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理服务")
                .packagesToScan("com.muyingmall.admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}