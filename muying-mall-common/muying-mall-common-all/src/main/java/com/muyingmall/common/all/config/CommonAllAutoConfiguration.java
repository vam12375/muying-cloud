package com.muyingmall.common.all.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.muyingmall.common.redis.config.RedisAutoConfiguration;
import com.muyingmall.common.swagger.config.SwaggerAutoConfiguration;
import com.muyingmall.common.security.config.SecurityAutoConfiguration;

/**
 * Common All 模块自动配置类
 * 
 * <p>聚合所有功能模块的自动配置，确保所有功能可用</p>
 * 
 * @author 青柠檬
 * @since 2025-09-24
 */
@AutoConfiguration
@ComponentScan(basePackages = {
    "com.muyingmall.common.core",
    "com.muyingmall.common.redis", 
    "com.muyingmall.common.swagger",
    "com.muyingmall.common.security"
})
@Import({
    RedisAutoConfiguration.class,
    SwaggerAutoConfiguration.class,
    SecurityAutoConfiguration.class,
    BackwardCompatibilityConfiguration.class
})
public class CommonAllAutoConfiguration {
    
    /**
     * 构造函数
     */
    public CommonAllAutoConfiguration() {
        // 自动配置类初始化
    }
}