package com.muyingmall.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 提供缓存操作的通用方法
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RedisTemplate.class)
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间（秒）
     * @return 是否成功
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否成功
     */
    public boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            log.error("Redis删除缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除缓存
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    public Long delete(Collection<String> keys) {
        try {
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            log.error("Redis批量删除缓存失败: error={}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis判断缓存是否存在失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key  键
     * @param time 过期时间（秒）
     * @return 是否成功
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
            }
            return false;
        } catch (Exception e) {
            log.error("Redis设置过期时间失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间（秒），返回0代表永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加的值
     * @return 递增后的值
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少的值
     * @return 递减后的值
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return 是否成功
     */
    public boolean hSetAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis HashSet失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * HashSet并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return 是否成功
     */
    public boolean hSetAll(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis HashSet失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return 是否成功
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Redis hSet失败: key={}, item={}, error={}", key, item, e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键
     * @param item 项 可以是多个
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键
     * @param item 项
     * @return 是否存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set集合
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis sGet失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Redis sSet失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return list
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis lGet失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis lSet失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis lSet失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis lSet失败: key={}, error={}", key, e.getMessage());
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
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis获取分布式锁失败: key={}, error={}", key, e.getMessage());
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
        try {
            Object currentValue = redisTemplate.opsForValue().get(key);
            if (value.equals(currentValue)) {
                redisTemplate.delete(key);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Redis释放分布式锁失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 判断元素是否在Set集合中
     * @param key 集合的键
     * @param member 要判断的元素
     * @return 是否存在
     */
    public Boolean sIsMember(String key, String member) {
        try {
            return redisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            log.error("Redis判断Set成员失败: key={}, member={}, error={}", key, member, e.getMessage());
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
        try {
            return redisTemplate.opsForSet().add(key, (Object[]) values);
        } catch (Exception e) {
            log.error("Redis向Set添加元素失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }
}