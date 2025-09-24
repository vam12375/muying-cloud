package com.muyingmall.common.utils;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 向后兼容类 - RedisUtil
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.redis.utils.RedisUtils} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class RedisUtil {
    
    private static com.muyingmall.common.redis.utils.RedisUtils delegate;
    
    /**
     * 设置委托实例
     * 
     * @param redisUtils Redis工具类实例
     */
    public static void setDelegate(com.muyingmall.common.redis.utils.RedisUtils redisUtils) {
        delegate = redisUtils;
    }
    
    /**
     * 设置缓存
     * 
     * @param key 键
     * @param value 值
     */
    public static void set(String key, Object value) {
        if (delegate != null) {
            delegate.set(key, value);
        }
    }
    
    /**
     * 设置缓存并指定过期时间
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     */
    public static void set(String key, Object value, Duration timeout) {
        if (delegate != null) {
            delegate.set(key, value, timeout);
        }
    }
    
    /**
     * 获取缓存
     * 
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return delegate != null ? delegate.get(key) : null;
    }
    
    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 是否删除成功
     */
    public static Boolean delete(String key) {
        return delegate != null ? delegate.delete(key) : false;
    }
    
    /**
     * 检查键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public static Boolean hasKey(String key) {
        return delegate != null ? delegate.exists(key) : false;
    }
    
    /**
     * 设置过期时间
     * 
     * @param key 键
     * @param timeout 过期时间
     * @return 是否设置成功
     */
    public static Boolean expire(String key, Duration timeout) {
        return delegate != null ? delegate.expire(key, timeout) : false;
    }
}