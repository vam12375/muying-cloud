package com.muyingmall.common.redis.lock.impl;

import com.muyingmall.common.redis.lock.DistributedLock;
import com.muyingmall.common.redis.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁实现
 * 
 * <p>基于Redis的SET命令实现分布式锁，支持锁的自动过期和安全释放。</p>
 * 
 * <p>特性：</p>
 * <ul>
 *   <li>使用Lua脚本保证操作的原子性</li>
 *   <li>支持锁的自动过期，防止死锁</li>
 *   <li>安全的锁释放，只有锁的持有者才能释放</li>
 *   <li>支持重试机制</li>
 *   <li>支持锁续期</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RedisTemplate.class)
public class RedisDistributedLock implements DistributedLock {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    /**
     * 释放锁的Lua脚本
     * 只有当锁的值与传入的值相同时才会删除锁
     */
    private static final String UNLOCK_SCRIPT = 
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";

    /**
     * 续期锁的Lua脚本
     * 只有当锁的值与传入的值相同时才会续期
     */
    private static final String RENEW_SCRIPT = 
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('expire', KEYS[1], ARGV[2]) " +
            "else " +
            "    return 0 " +
            "end";

    /**
     * 构建锁的完整键名
     *
     * @param lockKey 锁的键
     * @return 完整的锁键名
     */
    private String buildLockKey(String lockKey) {
        String lockPrefix = redisProperties.getLock().getKeyPrefix();
        if (redisProperties.isEnableKeyPrefix()) {
            return redisProperties.getKeyPrefix() + lockPrefix + lockKey;
        }
        return lockPrefix + lockKey;
    }

    @Override
    public boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            Boolean result = redisTemplate.opsForValue().setIfAbsent(fullLockKey, requestId, expireTime, timeUnit);
            boolean lockAcquired = result != null && result;
            
            if (lockAcquired) {
                log.debug("成功获取分布式锁: lockKey={}, requestId={}, expireTime={} {}", 
                         lockKey, requestId, expireTime, timeUnit);
            } else {
                log.debug("获取分布式锁失败: lockKey={}, requestId={}", lockKey, requestId);
            }
            
            return lockAcquired;
        } catch (Exception e) {
            log.error("获取分布式锁异常: lockKey={}, requestId={}, error={}", lockKey, requestId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean tryLock(String lockKey, String requestId) {
        int defaultExpireTime = redisProperties.getLock().getDefaultExpireTime();
        return tryLock(lockKey, requestId, defaultExpireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit, 
                          int retryCount, long retryInterval) {
        for (int i = 0; i < retryCount; i++) {
            if (tryLock(lockKey, requestId, expireTime, timeUnit)) {
                return true;
            }
            
            if (i < retryCount - 1) {
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("获取分布式锁时被中断: lockKey={}, requestId={}", lockKey, requestId);
                    return false;
                }
            }
        }
        
        log.debug("重试{}次后仍未获取到分布式锁: lockKey={}, requestId={}", retryCount, lockKey, requestId);
        return false;
    }

    @Override
    public boolean lock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit, long waitTime) 
            throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTimeMillis = timeUnit.toMillis(waitTime);
        long retryInterval = redisProperties.getLock().getRetryInterval();
        
        while (System.currentTimeMillis() - startTime < waitTimeMillis) {
            if (tryLock(lockKey, requestId, expireTime, timeUnit)) {
                return true;
            }
            
            Thread.sleep(retryInterval);
        }
        
        log.debug("等待{}ms后仍未获取到分布式锁: lockKey={}, requestId={}", waitTimeMillis, lockKey, requestId);
        return false;
    }

    @Override
    public boolean unlock(String lockKey, String requestId) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
            Long result = redisTemplate.execute(script, Collections.singletonList(fullLockKey), requestId);
            
            boolean unlocked = result != null && result > 0;
            if (unlocked) {
                log.debug("成功释放分布式锁: lockKey={}, requestId={}", lockKey, requestId);
            } else {
                log.debug("释放分布式锁失败，锁不存在或requestId不匹配: lockKey={}, requestId={}", lockKey, requestId);
            }
            
            return unlocked;
        } catch (Exception e) {
            log.error("释放分布式锁异常: lockKey={}, requestId={}, error={}", lockKey, requestId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean forceUnlock(String lockKey) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            Boolean result = redisTemplate.delete(fullLockKey);
            boolean unlocked = result != null && result;
            
            if (unlocked) {
                log.warn("强制释放分布式锁: lockKey={}", lockKey);
            } else {
                log.debug("强制释放分布式锁失败，锁不存在: lockKey={}", lockKey);
            }
            
            return unlocked;
        } catch (Exception e) {
            log.error("强制释放分布式锁异常: lockKey={}, error={}", lockKey, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isLocked(String lockKey) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            Boolean result = redisTemplate.hasKey(fullLockKey);
            return result != null && result;
        } catch (Exception e) {
            log.error("检查锁是否存在异常: lockKey={}, error={}", lockKey, e.getMessage());
            return false;
        }
    }

    @Override
    public String getLockHolder(String lockKey) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            Object holder = redisTemplate.opsForValue().get(fullLockKey);
            return holder != null ? holder.toString() : null;
        } catch (Exception e) {
            log.error("获取锁持有者异常: lockKey={}, error={}", lockKey, e.getMessage());
            return null;
        }
    }

    @Override
    public boolean renewLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit) {
        try {
            String fullLockKey = buildLockKey(lockKey);
            long expireSeconds = timeUnit.toSeconds(expireTime);
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(RENEW_SCRIPT, Long.class);
            Long result = redisTemplate.execute(script, Collections.singletonList(fullLockKey), 
                                              requestId, String.valueOf(expireSeconds));
            
            boolean renewed = result != null && result > 0;
            if (renewed) {
                log.debug("成功续期分布式锁: lockKey={}, requestId={}, expireTime={} {}", 
                         lockKey, requestId, expireTime, timeUnit);
            } else {
                log.debug("续期分布式锁失败，锁不存在或requestId不匹配: lockKey={}, requestId={}", lockKey, requestId);
            }
            
            return renewed;
        } catch (Exception e) {
            log.error("续期分布式锁异常: lockKey={}, requestId={}, error={}", lockKey, requestId, e.getMessage());
            return false;
        }
    }
}