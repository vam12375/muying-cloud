package com.muyingmall.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis工具类 - 兼容性包装器
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.core.utils.RedisUtils}，请使用新的位置。
 * 此类仅为向后兼容而保留，将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 */
@Deprecated
@Slf4j
@Component
@ConditionalOnClass(RedisTemplate.class)
public class RedisUtil {

    @Autowired
    private com.muyingmall.common.core.utils.RedisUtils coreRedisUtils;

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#set(String, Object)}
     */
    @Deprecated
    public boolean set(String key, Object value) {
        return coreRedisUtils.set(key, value);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#set(String, Object, long)}
     */
    @Deprecated
    public boolean set(String key, Object value, long time) {
        return coreRedisUtils.set(key, value, time);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#get(String)}
     */
    @Deprecated
    public Object get(String key) {
        return coreRedisUtils.get(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#delete(String)}
     */
    @Deprecated
    public boolean delete(String key) {
        return coreRedisUtils.delete(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#delete(Collection)}
     */
    @Deprecated
    public Long delete(Collection<String> keys) {
        return coreRedisUtils.delete(keys);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hasKey(String)}
     */
    @Deprecated
    public boolean hasKey(String key) {
        return coreRedisUtils.hasKey(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#expire(String, long)}
     */
    @Deprecated
    public boolean expire(String key, long time) {
        return coreRedisUtils.expire(key, time);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#getExpire(String)}
     */
    @Deprecated
    public Long getExpire(String key) {
        return coreRedisUtils.getExpire(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#incr(String, long)}
     */
    @Deprecated
    public Long incr(String key, long delta) {
        return coreRedisUtils.incr(key, delta);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#decr(String, long)}
     */
    @Deprecated
    public Long decr(String key, long delta) {
        return coreRedisUtils.decr(key, delta);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hGet(String, String)}
     */
    @Deprecated
    public Object hGet(String key, String item) {
        return coreRedisUtils.hGet(key, item);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hGetAll(String)}
     */
    @Deprecated
    public Map<Object, Object> hGetAll(String key) {
        return coreRedisUtils.hGetAll(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hSetAll(String, Map)}
     */
    @Deprecated
    public boolean hSetAll(String key, Map<String, Object> map) {
        return coreRedisUtils.hSetAll(key, map);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hSetAll(String, Map, long)}
     */
    @Deprecated
    public boolean hSetAll(String key, Map<String, Object> map, long time) {
        return coreRedisUtils.hSetAll(key, map, time);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hSet(String, String, Object)}
     */
    @Deprecated
    public boolean hSet(String key, String item, Object value) {
        return coreRedisUtils.hSet(key, item, value);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hDel(String, Object...)}
     */
    @Deprecated
    public void hDel(String key, Object... item) {
        coreRedisUtils.hDel(key, item);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#hHasKey(String, String)}
     */
    @Deprecated
    public boolean hHasKey(String key, String item) {
        return coreRedisUtils.hHasKey(key, item);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#sGet(String)}
     */
    @Deprecated
    public Set<Object> sGet(String key) {
        return coreRedisUtils.sGet(key);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#sSet(String, Object...)}
     */
    @Deprecated
    public Long sSet(String key, Object... values) {
        return coreRedisUtils.sSet(key, values);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#lGet(String, long, long)}
     */
    @Deprecated
    public List<Object> lGet(String key, long start, long end) {
        return coreRedisUtils.lGet(key, start, end);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#lSet(String, Object)}
     */
    @Deprecated
    public boolean lSet(String key, Object value) {
        return coreRedisUtils.lSet(key, value);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#lSet(String, Object, long)}
     */
    @Deprecated
    public boolean lSet(String key, Object value, long time) {
        return coreRedisUtils.lSet(key, value, time);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#lSet(String, List)}
     */
    @Deprecated
    public boolean lSet(String key, List<Object> value) {
        return coreRedisUtils.lSet(key, value);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#getLock(String, String, int)}
     */
    @Deprecated
    public boolean getLock(String key, String value, int expireTime) {
        return coreRedisUtils.getLock(key, value, expireTime);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#releaseLock(String, String)}
     */
    @Deprecated
    public boolean releaseLock(String key, String value) {
        return coreRedisUtils.releaseLock(key, value);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#sIsMember(String, String)}
     */
    @Deprecated
    public Boolean sIsMember(String key, String member) {
        return coreRedisUtils.sIsMember(key, member);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.RedisUtils#sAdd(String, String...)}
     */
    @Deprecated
    public Long sAdd(String key, String... values) {
        return coreRedisUtils.sAdd(key, values);
    }
}