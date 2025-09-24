package com.muyingmall.common.swagger.config;

import com.muyingmall.common.swagger.properties.SwaggerProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Swagger自动配置类
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springdoc.core.configuration.SpringDocConfiguration")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "muying.swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(SwaggerConfig.class)
public class SwaggerAutoConfiguration {

    /**
     * 自动配置构造函数
     */
    public SwaggerAutoConfiguration() {
        // 自动配置类，无需额外逻辑
    }
}