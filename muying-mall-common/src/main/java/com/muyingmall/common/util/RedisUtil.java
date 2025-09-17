package com.muyingmall.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Redis工具类
 * 只有在启用Redis时才生效
 */
@Component
@Slf4j
@ConditionalOnClass(RedisTemplate.class)
@ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = true)
public class RedisUtil {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置键值对
     */
    public void set(String key, Object value) {
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            log.warn("RedisTemplate未配置，无法执行set操作");
        }
    }

    /**
     * 设置键值对并指定过期时间
     * @param key 键
     * @param value 值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } else {
            log.warn("RedisTemplate未配置，无法执行set操作");
        }
    }

    /**
     * 获取值
     */
    public Object get(String key) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().get(key);
        } else {
            log.warn("RedisTemplate未配置，无法执行get操作");
            return null;
        }
    }

    /**
     * 删除键
     */
    public void delete(String key) {
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        } else {
            log.warn("RedisTemplate未配置，无法执行delete操作");
        }
    }

    /**
     * 判断键是否存在
     */
    public boolean hasKey(String key) {
        if (redisTemplate != null) {
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } else {
            log.warn("RedisTemplate未配置，无法执行hasKey操作");
            return false;
        }
    }

    /**
     * 获取分布式锁
     * @param key 锁的键
     * @param value 锁的值（通常使用UUID或当前线程ID）
     * @param expireTime 过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean getLock(String key, String value, int expireTime) {
        if (redisTemplate != null) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
            return result != null && result;
        } else {
            log.warn("RedisTemplate未配置，无法执行getLock操作");
            return false;
        }
    }

    /**
     * 释放分布式锁
     * @param key 锁的键
     * @param value 锁的值（必须与获取锁时的值相同）
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String key, String value) {
        if (redisTemplate != null) {
            Object currentValue = redisTemplate.opsForValue().get(key);
            if (value.equals(currentValue)) {
                redisTemplate.delete(key);
                return true;
            }
            return false;
        } else {
            log.warn("RedisTemplate未配置，无法执行releaseLock操作");
            return false;
        }
    }

    /**
     * 向Set集合添加元素
     * @param key 集合的键
     * @param values 要添加的元素
     * @return 成功添加的元素个数
     */
    public Long sAdd(String key, String... values) {
        if (redisTemplate != null) {
            return redisTemplate.opsForSet().add(key, (Object[]) values);
        } else {
            log.warn("RedisTemplate未配置，无法执行sAdd操作");
            return 0L;
        }
    }

    /**
     * 判断元素是否在Set集合中
     * @param key 集合的键
     * @param member 要判断的元素
     * @return 是否存在
     */
    public Boolean sIsMember(String key, String member) {
        if (redisTemplate != null) {
            return redisTemplate.opsForSet().isMember(key, member);
        } else {
            log.warn("RedisTemplate未配置，无法执行sIsMember操作");
            return false;
        }
    }

    /**
     * del方法的别名，保持兼容性
     */
    public void del(String key) {
        delete(key);
    }

    /**
     * 批量删除键
     */
    public void del(String[] keys) {
        if (redisTemplate != null && keys != null && keys.length > 0) {
            for (String key : keys) {
                redisTemplate.delete(key);
            }
        } else {
            log.warn("RedisTemplate未配置或keys为空，无法执行批量delete操作");
        }
    }

    /**
     * 根据模式查找键
     * @param pattern 模式，如 "user:*"
     * @return 匹配的键集合
     */
    public Set<String> keys(String pattern) {
        if (redisTemplate != null) {
            return redisTemplate.keys(pattern);
        } else {
            log.warn("RedisTemplate未配置，无法执行keys操作");
            return null;
        }
    }

    /**
     * 根据模式删除键
     * @param pattern 模式，如 "user:*"
     */
    public void delPattern(String pattern) {
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } else {
            log.warn("RedisTemplate未配置，无法执行delPattern操作");
        }
    }

    // ==================== Hash 操作 ====================

    /**
     * 获取Hash中的所有键值对
     * @param key Hash的键
     * @return 所有键值对的Map
     */
    public Map<Object, Object> hGetAll(String key) {
        if (redisTemplate != null) {
            return redisTemplate.opsForHash().entries(key);
        } else {
            log.warn("RedisTemplate未配置，无法执行hGetAll操作");
            return null;
        }
    }

    /**
     * 批量设置Hash中的键值对并设置过期时间
     * @param key Hash的键
     * @param map 要设置的键值对
     * @param timeout 过期时间（秒）
     */
    public void hPutAll(String key, Map<String, Object> map, long timeout) {
        if (redisTemplate != null) {
            redisTemplate.opsForHash().putAll(key, map);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } else {
            log.warn("RedisTemplate未配置，无法执行hPutAll操作");
        }
    }

    // ==================== List 操作 ====================

    /**
     * 获取List中指定范围的元素
     * @param key List的键
     * @param start 开始位置
     * @param end 结束位置
     * @return 指定范围的元素列表
     */
    public List<Object> lRange(String key, long start, long end) {
        if (redisTemplate != null) {
            return redisTemplate.opsForList().range(key, start, end);
        } else {
            log.warn("RedisTemplate未配置，无法执行lRange操作");
            return null;
        }
    }

    /**
     * 从右侧批量插入元素到List
     * @param key List的键
     * @param values 要插入的元素列表
     * @return 插入后List的长度
     */
    public Long lRightPushAll(String key, Collection<?> values) {
        if (redisTemplate != null) {
            return redisTemplate.opsForList().rightPushAll(key, values.toArray());
        } else {
            log.warn("RedisTemplate未配置，无法执行lRightPushAll操作");
            return 0L;
        }
    }

    /**
     * 设置键的过期时间
     * @param key 键
     * @param timeout 过期时间（秒）
     */
    public void expire(String key, long timeout) {
        if (redisTemplate != null) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } else {
            log.warn("RedisTemplate未配置，无法执行expire操作");
        }
    }

    // ==================== Sorted Set 操作 ====================

    /**
     * 获取有序集合中指定范围的元素（分数从高到低）
     * @param key 有序集合的键
     * @param start 开始位置
     * @param end 结束位置
     * @return 指定范围的元素集合
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        if (redisTemplate != null) {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } else {
            log.warn("RedisTemplate未配置，无法执行zReverseRange操作");
            return null;
        }
    }

    /**
     * 向有序集合添加元素
     * @param key 有序集合的键
     * @param value 要添加的元素
     * @param score 分数
     * @return 是否添加成功
     */
    public Boolean zAdd(String key, Object value, double score) {
        if (redisTemplate != null) {
            return redisTemplate.opsForZSet().add(key, value, score);
        } else {
            log.warn("RedisTemplate未配置，无法执行zAdd操作");
            return false;
        }
    }
}
