package com.muyingmall.common.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Redis配置属性
 * 
 * <p>用于配置Redis相关的参数，包括缓存键前缀、默认过期时间、分布式锁配置等。</p>
 * 
 * <p>配置示例：</p>
 * <pre>{@code
 * muying:
 *   redis:
 *     key-prefix: "muying:"
 *     default-expire: PT1H  # 1小时
 *     lock:
 *       default-expire-time: 30  # 30秒
 *       retry-count: 3
 *       retry-interval: 100  # 100毫秒
 * }</pre>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "muying.redis")
public class RedisProperties {

    /**
     * 缓存键前缀
     */
    private String keyPrefix = "muying:";

    /**
     * 默认过期时间
     */
    private Duration defaultExpire = Duration.ofHours(1);

    /**
     * 是否启用键前缀
     */
    private boolean enableKeyPrefix = true;

    /**
     * 分布式锁配置
     */
    private Lock lock = new Lock();

    /**
     * 缓存配置
     */
    private Cache cache = new Cache();

    /**
     * 分布式锁配置类
     */
    @Data
    public static class Lock {
        
        /**
         * 默认锁过期时间（秒）
         */
        private int defaultExpireTime = 30;
        
        /**
         * 获取锁的重试次数
         */
        private int retryCount = 3;
        
        /**
         * 重试间隔时间（毫秒）
         */
        private long retryInterval = 100;
        
        /**
         * 锁键前缀
         */
        private String keyPrefix = "lock:";
    }

    /**
     * 缓存配置类
     */
    @Data
    public static class Cache {
        
        /**
         * 是否启用缓存空值
         */
        private boolean cacheNullValues = true;
        
        /**
         * 空值缓存时间（秒）
         */
        private long nullValueExpireTime = 300;
        
        /**
         * 是否启用缓存统计
         */
        private boolean enableStatistics = false;
    }
}