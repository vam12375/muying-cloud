package com.muyingmall.common.redis.service.impl;

import com.muyingmall.common.redis.properties.RedisProperties;
import com.muyingmall.common.redis.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务实现类
 * 
 * <p>基于RedisTemplate实现的缓存服务，提供完整的Redis操作功能。</p>
 * 
 * <p>特性：</p>
 * <ul>
 *   <li>支持键前缀配置</li>
 *   <li>完整的异常处理</li>
 *   <li>详细的操作日志</li>
 *   <li>支持多种数据结构操作</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnClass(RedisTemplate.class)
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    /**
     * 构建完整的缓存键（包含前缀）
     *
     * @param key 原始键
     * @return 完整的缓存键
     */
    private String buildKey(String key) {
        if (redisProperties.isEnableKeyPrefix()) {
            return redisProperties.getKeyPrefix() + key;
        }
        return key;
    }

    // =============================基本操作=============================

    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(buildKey(key), value);
            log.debug("设置缓存成功: key={}", key);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(buildKey(key), value, timeout, timeUnit);
            log.debug("设置缓存成功: key={}, timeout={} {}", key, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value, Duration duration) {
        return set(key, value, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(buildKey(key));
            log.debug("获取缓存: key={}, found={}", key, value != null);
            return value;
        } catch (Exception e) {
            log.error("获取缓存失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = get(key);
            if (value != null && clazz.isInstance(value)) {
                return (T) value;
            }
            return null;
        } catch (Exception e) {
            log.error("获取缓存失败: key={}, clazz={}, error={}", key, clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(buildKey(key));
            boolean deleted = result != null && result;
            log.debug("删除缓存: key={}, deleted={}", key, deleted);
            return deleted;
        } catch (Exception e) {
            log.error("删除缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public Long delete(Collection<String> keys) {
        try {
            Collection<String> fullKeys = keys.stream()
                    .map(this::buildKey)
                    .toList();
            Long count = redisTemplate.delete(fullKeys);
            log.debug("批量删除缓存: count={}", count);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("批量删除缓存失败: error={}", e.getMessage());
            return 0L;
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(buildKey(key));
            return result != null && result;
        } catch (Exception e) {
            log.error("判断缓存是否存在失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        try {
            Boolean result = redisTemplate.expire(buildKey(key), timeout, timeUnit);
            boolean expired = result != null && result;
            log.debug("设置过期时间: key={}, timeout={} {}, success={}", key, timeout, timeUnit, expired);
            return expired;
        } catch (Exception e) {
            log.error("设置过期时间失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean expire(String key, Duration duration) {
        return expire(key, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return redisTemplate.getExpire(buildKey(key), timeUnit);
        } catch (Exception e) {
            log.error("获取过期时间失败: key={}, error={}", key, e.getMessage());
            return -2L; // 键不存在
        }
    }

    // =============================原子操作=============================

    @Override
    public Long increment(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(buildKey(key), delta);
            log.debug("递增操作: key={}, delta={}, result={}", key, delta, result);
            return result;
        } catch (Exception e) {
            log.error("递增操作失败: key={}, delta={}, error={}", key, delta, e.getMessage());
            return null;
        }
    }

    @Override
    public Long increment(String key) {
        return increment(key, 1L);
    }

    @Override
    public Long decrement(String key, long delta) {
        return increment(key, -delta);
    }

    @Override
    public Long decrement(String key) {
        return increment(key, -1L);
    }

    // =============================Hash操作=============================

    @Override
    public Object hGet(String key, String hashKey) {
        try {
            Object value = redisTemplate.opsForHash().get(buildKey(key), hashKey);
            log.debug("获取Hash值: key={}, hashKey={}, found={}", key, hashKey, value != null);
            return value;
        } catch (Exception e) {
            log.error("获取Hash值失败: key={}, hashKey={}, error={}", key, hashKey, e.getMessage());
            return null;
        }
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        try {
            Map<Object, Object> result = redisTemplate.opsForHash().entries(buildKey(key));
            log.debug("获取Hash所有值: key={}, size={}", key, result.size());
            return result;
        } catch (Exception e) {
            log.error("获取Hash所有值失败: key={}, error={}", key, e.getMessage());
            return Map.of();
        }
    }

    @Override
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(buildKey(key), hashKey, value);
            log.debug("设置Hash值成功: key={}, hashKey={}", key, hashKey);
            return true;
        } catch (Exception e) {
            log.error("设置Hash值失败: key={}, hashKey={}, error={}", key, hashKey, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hSetAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(buildKey(key), map);
            log.debug("批量设置Hash值成功: key={}, size={}", key, map.size());
            return true;
        } catch (Exception e) {
            log.error("批量设置Hash值失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public Long hDelete(String key, Object... hashKeys) {
        try {
            Long count = redisTemplate.opsForHash().delete(buildKey(key), hashKeys);
            log.debug("删除Hash字段: key={}, count={}", key, count);
            return count;
        } catch (Exception e) {
            log.error("删除Hash字段失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    @Override
    public boolean hExists(String key, String hashKey) {
        try {
            Boolean result = redisTemplate.opsForHash().hasKey(buildKey(key), hashKey);
            return result != null && result;
        } catch (Exception e) {
            log.error("判断Hash字段是否存在失败: key={}, hashKey={}, error={}", key, hashKey, e.getMessage());
            return false;
        }
    }

    // =============================Set操作=============================

    @Override
    public Set<Object> sMembers(String key) {
        try {
            Set<Object> result = redisTemplate.opsForSet().members(buildKey(key));
            log.debug("获取Set所有成员: key={}, size={}", key, result != null ? result.size() : 0);
            return result;
        } catch (Exception e) {
            log.error("获取Set所有成员失败: key={}, error={}", key, e.getMessage());
            return Set.of();
        }
    }

    @Override
    public Long sAdd(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(buildKey(key), values);
            log.debug("向Set添加元素: key={}, count={}", key, count);
            return count;
        } catch (Exception e) {
            log.error("向Set添加元素失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    @Override
    public boolean sIsMember(String key, Object value) {
        try {
            Boolean result = redisTemplate.opsForSet().isMember(buildKey(key), value);
            return result != null && result;
        } catch (Exception e) {
            log.error("判断Set成员是否存在失败: key={}, value={}, error={}", key, value, e.getMessage());
            return false;
        }
    }

    @Override
    public Long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(buildKey(key), values);
            log.debug("从Set移除元素: key={}, count={}", key, count);
            return count;
        } catch (Exception e) {
            log.error("从Set移除元素失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    // =============================List操作=============================

    @Override
    public List<Object> lRange(String key, long start, long end) {
        try {
            List<Object> result = redisTemplate.opsForList().range(buildKey(key), start, end);
            log.debug("获取List范围内容: key={}, start={}, end={}, size={}", 
                     key, start, end, result != null ? result.size() : 0);
            return result;
        } catch (Exception e) {
            log.error("获取List范围内容失败: key={}, start={}, end={}, error={}", key, start, end, e.getMessage());
            return List.of();
        }
    }

    @Override
    public Long lRightPush(String key, Object value) {
        try {
            Long size = redisTemplate.opsForList().rightPush(buildKey(key), value);
            log.debug("向List右侧添加元素: key={}, size={}", key, size);
            return size;
        } catch (Exception e) {
            log.error("向List右侧添加元素失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    @Override
    public Long lLeftPush(String key, Object value) {
        try {
            Long size = redisTemplate.opsForList().leftPush(buildKey(key), value);
            log.debug("向List左侧添加元素: key={}, size={}", key, size);
            return size;
        } catch (Exception e) {
            log.error("向List左侧添加元素失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    @Override
    public Object lRightPop(String key) {
        try {
            Object value = redisTemplate.opsForList().rightPop(buildKey(key));
            log.debug("从List右侧弹出元素: key={}, value={}", key, value);
            return value;
        } catch (Exception e) {
            log.error("从List右侧弹出元素失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public Object lLeftPop(String key) {
        try {
            Object value = redisTemplate.opsForList().leftPop(buildKey(key));
            log.debug("从List左侧弹出元素: key={}, value={}", key, value);
            return value;
        } catch (Exception e) {
            log.error("从List左侧弹出元素失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public Long lSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(buildKey(key));
            log.debug("获取List大小: key={}, size={}", key, size);
            return size != null ? size : 0L;
        } catch (Exception e) {
            log.error("获取List大小失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }
}