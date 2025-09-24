package com.muyingmall.common.redis.config;

import com.muyingmall.common.redis.lock.DistributedLock;
import com.muyingmall.common.redis.lock.impl.RedisDistributedLock;
import com.muyingmall.common.redis.properties.RedisProperties;
import com.muyingmall.common.redis.service.CacheService;
import com.muyingmall.common.redis.service.impl.RedisCacheService;
import com.muyingmall.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.PostConstruct;

/**
 * Redis自动配置类
 * 
 * <p>提供Redis模块的自动配置，包括缓存服务、分布式锁、工具类等组件的自动装配。</p>
 * 
 * <p>自动配置的组件：</p>
 * <ul>
 *   <li>RedisTemplate配置</li>
 *   <li>CacheService缓存服务</li>
 *   <li>DistributedLock分布式锁</li>
 *   <li>RedisUtils工具类</li>
 *   <li>RedisProperties配置属性</li>
 * </ul>
 * 
 * <p>配置条件：</p>
 * <ul>
 *   <li>类路径中存在RedisTemplate类</li>
 *   <li>相关Bean不存在时才会创建</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(RedisTemplate.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({RedisConfig.class})
public class RedisAutoConfiguration {

    /**
     * 配置缓存服务
     *
     * @param redisTemplate   Redis模板
     * @param redisProperties Redis配置属性
     * @return 缓存服务实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(RedisTemplate<String, Object> redisTemplate, 
                                   RedisProperties redisProperties) {
        log.info("初始化Redis缓存服务");
        return new RedisCacheService(redisTemplate, redisProperties);
    }

    /**
     * 配置分布式锁
     *
     * @param redisTemplate   Redis模板
     * @param redisProperties Redis配置属性
     * @return 分布式锁实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DistributedLock distributedLock(RedisTemplate<String, Object> redisTemplate, 
                                         RedisProperties redisProperties) {
        log.info("初始化Redis分布式锁");
        return new RedisDistributedLock(redisTemplate, redisProperties);
    }

    /**
     * 配置Redis工具类
     *
     * @param cacheService 缓存服务
     * @return Redis工具类实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisUtils redisUtils(CacheService cacheService) {
        log.info("初始化Redis工具类");
        RedisUtils redisUtils = new RedisUtils(cacheService);
        redisUtils.init(); // 初始化静态缓存服务
        return redisUtils;
    }

    /**
     * 配置完成后的初始化
     */
    @PostConstruct
    public void init() {
        log.info("Redis模块自动配置完成");
        log.info("可用组件: CacheService, DistributedLock, RedisUtils");
        log.info("配置属性前缀: muying.redis");
    }
}