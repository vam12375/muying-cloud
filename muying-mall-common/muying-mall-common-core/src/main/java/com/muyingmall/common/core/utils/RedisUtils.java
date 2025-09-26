package com.muyingmall.common.core.utils;

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
 * 
 * <p>提供Redis缓存操作的通用方法，包括基本的CRUD操作、分布式锁、集合操作等。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>基本缓存操作（设置、获取、删除）</li>
 *   <li>过期时间管理</li>
 *   <li>分布式锁操作</li>
 *   <li>Hash、Set、List数据结构操作</li>
 *   <li>原子递增递减操作</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @Autowired
 * private RedisUtils redisUtils;
 * 
 * // 基本操作
 * redisUtils.set("key", "value", 3600); // 设置缓存，过期时间1小时
 * Object value = redisUtils.get("key");  // 获取缓存
 * 
 * // 分布式锁
 * String lockKey = "lock:user:1001";
 * String requestId = UUID.randomUUID().toString();
 * if (redisUtils.getLock(lockKey, requestId, 10)) {
 *     try {
 *         // 执行业务逻辑
 *     } finally {
 *         redisUtils.releaseLock(lockKey, requestId);
 *     }
 * }
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>只有在Redis配置可用时才会生效</li>
 *   <li>分布式锁使用时请确保在finally块中释放锁</li>
 *   <li>所有操作都包含异常处理，失败时会记录日志</li>
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
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    // =============================基本操作=============================

    /**
     * 设置缓存
     *
     * @param key   缓存键，不能为null
     * @param value 缓存值
     * @return true表示设置成功，false表示设置失败
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
     * @param key   缓存键，不能为null
     * @param value 缓存值
     * @param time  过期时间（秒），必须大于0
     * @return true表示设置成功，false表示设置失败
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
     * @param key 缓存键
     * @return 缓存值，如果不存在则返回null
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存并指定类型
     *
     * @param key   缓存键
     * @param clazz 期望的返回类型
     * @param <T>   返回类型
     * @return 缓存值，如果不存在或类型不匹配则返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = get(key);
            if (value != null && clazz.isInstance(value)) {
                return (T) value;
            }
            return null;
        } catch (Exception e) {
            log.error("Redis获取缓存失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     * @return true表示删除成功，false表示删除失败
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
     * @param keys 缓存键集合
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
     * 删除缓存（兼容方法）
     *
     * @param key 缓存键
     * @return true表示删除成功，false表示删除失败
     */
    public boolean del(String key) {
        return delete(key);
    }

    /**
     * 根据模式删除缓存
     *
     * @param pattern 匹配模式，支持通配符*和?
     * @return 删除的数量
     */
    public Long delPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                return redisTemplate.delete(keys);
            }
            return 0L;
        } catch (Exception e) {
            log.error("Redis根据模式删除缓存失败: pattern={}, error={}", pattern, e.getMessage());
            return 0L;
        }
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键
     * @return true表示存在，false表示不存在
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
     * @param key  缓存键
     * @param time 过期时间（秒），必须大于0
     * @return true表示设置成功，false表示设置失败
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
     * @param key 缓存键
     * @return 过期时间（秒），返回-1表示永久有效，返回-2表示键不存在
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    // =============================原子操作=============================

    /**
     * 递增操作
     *
     * @param key   缓存键
     * @param delta 要增加的值，必须大于0
     * @return 递增后的值
     * @throws RuntimeException 如果delta小于等于0
     */
    public Long incr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增操作（默认增加1）
     *
     * @param key 缓存键
     * @return 递增后的值
     */
    public Long incr(String key) {
        return incr(key, 1);
    }

    /**
     * 递减操作
     *
     * @param key   缓存键
     * @param delta 要减少的值，必须大于0
     * @return 递减后的值
     * @throws RuntimeException 如果delta小于等于0
     */
    public Long decr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 递减操作（默认减少1）
     *
     * @param key 缓存键
     * @return 递减后的值
     */
    public Long decr(String key) {
        return decr(key, 1);
    }

    // =============================Hash操作=============================

    /**
     * 获取Hash中的值
     *
     * @param key  Hash键
     * @param item Hash项
     * @return Hash值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取Hash中的所有键值对
     *
     * @param key Hash键
     * @return Hash中的所有键值对
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 批量设置Hash值
     *
     * @param key Hash键
     * @param map 要设置的键值对
     * @return true表示设置成功，false表示设置失败
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
     * 批量设置Hash值并设置过期时间
     *
     * @param key  Hash键
     * @param map  要设置的键值对
     * @param time 过期时间（秒）
     * @return true表示设置成功，false表示设置失败
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
     * 设置Hash中的单个值
     *
     * @param key   Hash键
     * @param item  Hash项
     * @param value Hash值
     * @return true表示设置成功，false表示设置失败
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
     * 删除Hash中的值
     *
     * @param key  Hash键
     * @param item Hash项，可以是多个
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断Hash中是否存在指定项
     *
     * @param key  Hash键
     * @param item Hash项
     * @return true表示存在，false表示不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    // =============================Set操作=============================

    /**
     * 获取Set中的所有值
     *
     * @param key Set键
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
     * 向Set中添加值
     *
     * @param key    Set键
     * @param values 要添加的值，可以是多个
     * @return 成功添加的个数
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
     * 判断元素是否在Set集合中
     *
     * @param key    Set键
     * @param member 要判断的元素
     * @return true表示存在，false表示不存在
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
     *
     * @param key    Set键
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

    // =============================List操作=============================

    /**
     * 获取List中的内容
     *
     * @param key   List键
     * @param start 开始位置
     * @param end   结束位置
     * @return List内容
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
     * 向List右侧添加值
     *
     * @param key   List键
     * @param value 要添加的值
     * @return true表示添加成功，false表示添加失败
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
     * 向List右侧添加值并设置过期时间
     *
     * @param key   List键
     * @param value 要添加的值
     * @param time  过期时间（秒）
     * @return true表示添加成功，false表示添加失败
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
     * 批量向List右侧添加值
     *
     * @param key   List键
     * @param value 要添加的值列表
     * @return true表示添加成功，false表示添加失败
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

    // =============================分布式锁操作=============================

    /**
     * 获取分布式锁
     * 
     * <p>使用Redis的SETNX命令实现分布式锁，支持自动过期。</p>
     *
     * @param key        锁的键
     * @param value      锁的值（通常使用UUID或当前线程ID）
     * @param expireTime 过期时间（秒）
     * @return true表示成功获取锁，false表示获取锁失败
     */
    public boolean getLock(String key, String value, int expireTime) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
            boolean lockAcquired = result != null && result;
            if (lockAcquired) {
                log.debug("成功获取分布式锁: key={}, value={}, expireTime={}s", key, value, expireTime);
            } else {
                log.debug("获取分布式锁失败: key={}, value={}", key, value);
            }
            return lockAcquired;
        } catch (Exception e) {
            log.error("Redis获取分布式锁失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 释放分布式锁
     * 
     * <p>只有当锁的值与传入的值相同时才会释放锁，防止误释放其他线程的锁。</p>
     *
     * @param key   锁的键
     * @param value 锁的值（必须与获取锁时的值相同）
     * @return true表示成功释放锁，false表示释放锁失败
     */
    public boolean releaseLock(String key, String value) {
        try {
            Object currentValue = redisTemplate.opsForValue().get(key);
            if (value.equals(currentValue)) {
                redisTemplate.delete(key);
                log.debug("成功释放分布式锁: key={}, value={}", key, value);
                return true;
            } else {
                log.debug("释放分布式锁失败，值不匹配: key={}, expectedValue={}, actualValue={}", 
                         key, value, currentValue);
                return false;
            }
        } catch (Exception e) {
            log.error("Redis释放分布式锁失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 尝试获取分布式锁（带重试）
     * 
     * <p>在指定时间内重试获取分布式锁。</p>
     *
     * @param key         锁的键
     * @param value       锁的值
     * @param expireTime  锁的过期时间（秒）
     * @param retryTime   重试时间（毫秒）
     * @param retryCount  重试次数
     * @return true表示成功获取锁，false表示获取锁失败
     */
    public boolean tryLock(String key, String value, int expireTime, long retryTime, int retryCount) {
        for (int i = 0; i < retryCount; i++) {
            if (getLock(key, value, expireTime)) {
                return true;
            }
            
            if (i < retryCount - 1) {
                try {
                    Thread.sleep(retryTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("获取分布式锁时被中断: key={}", key);
                    return false;
                }
            }
        }
        
        log.debug("重试{}次后仍未获取到分布式锁: key={}", retryCount, key);
        return false;
    }
}