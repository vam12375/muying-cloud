package com.muyingmall.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis分布式锁工具类
 * 只有在启用Redis时才生效
 */
@Component
@Slf4j
@ConditionalOnClass(RedisTemplate.class)
@ConditionalOnProperty(name = "spring.data.redis.host", matchIfMissing = false)
public class RedisLockUtil {

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 尝试获取分布式锁
     */
    public boolean tryLock(String lockKey, String lockValue, long expireTime) {
        if (redisTemplate != null) {
            try {
                Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue,
                    java.time.Duration.ofMillis(expireTime));
                return result != null && result;
            } catch (Exception e) {
                log.error("获取Redis锁失败", e);
                return false;
            }
        } else {
            log.warn("RedisTemplate未配置，无法执行tryLock操作");
            return false;
        }
    }

    /**
     * 释放分布式锁
     */
    public void releaseLock(String lockKey, String lockValue) {
        if (redisTemplate != null) {
            try {
                String currentValue = redisTemplate.opsForValue().get(lockKey);
                if (lockValue.equals(currentValue)) {
                    redisTemplate.delete(lockKey);
                }
            } catch (Exception e) {
                log.error("释放Redis锁失败", e);
            }
        } else {
            log.warn("RedisTemplate未配置，无法执行releaseLock操作");
        }
    }
}