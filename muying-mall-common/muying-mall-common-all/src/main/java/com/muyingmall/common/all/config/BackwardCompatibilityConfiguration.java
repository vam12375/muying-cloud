package com.muyingmall.common.all.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.muyingmall.common.redis.utils.RedisUtils;
import com.muyingmall.common.utils.RedisUtil;

/**
 * 向后兼容配置类
 * 
 * <p>提供向后兼容的Bean配置和初始化</p>
 * 
 * @author 青柠檬
 * @since 2025-09-24
 */
@Configuration
public class BackwardCompatibilityConfiguration {
    
    /**
     * 初始化向后兼容的RedisUtil委托
     * 
     * @param redisUtils Redis工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisUtilInitializer redisUtilInitializer(RedisUtils redisUtils) {
        return new RedisUtilInitializer(redisUtils);
    }
    
    /**
     * RedisUtil初始化器
     */
    public static class RedisUtilInitializer {
        
        /**
         * 构造函数
         * 
         * @param redisUtils Redis工具类
         */
        public RedisUtilInitializer(RedisUtils redisUtils) {
            // 设置RedisUtil的委托实例
            RedisUtil.setDelegate(redisUtils);
        }
    }
}