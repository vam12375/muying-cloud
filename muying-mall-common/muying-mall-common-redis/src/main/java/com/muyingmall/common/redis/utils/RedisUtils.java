package com.muyingmall.common.redis.utils;

import com.muyingmall.common.redis.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类（Redis模块版本）
 * 
 * <p>基于CacheService的Redis工具类，提供更简洁的静态方法调用方式。</p>
 * 
 * <p>这是Redis模块的官方工具类，推荐在需要Redis功能的地方使用此类。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // 基本操作
 * RedisUtils.set("user:1001", userInfo, Duration.ofHours(1));
 * UserInfo user = RedisUtils.get("user:1001", UserInfo.class);
 * 
 * // Hash操作
 * RedisUtils.hSet("user:profile:1001", "name", "张三");
 * String name = (String) RedisUtils.hGet("user:profile:1001", "name");
 * 
 * // 原子操作
 * Long count = RedisUtils.increment("page:view:count");
 * }</pre>
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

    private final CacheService cacheService;
    
    private static CacheService staticCacheService;

    /**
     * 初始化静态缓存服务
     */
    public void init() {
        staticCacheService = this.cacheService;
    }

    // =============================基本操作=============================

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return 是否设置成功
     */
    public static boolean set(String key, Object value) {
        return staticCacheService.set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public static boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        return staticCacheService.set(key, value, timeout, timeUnit);
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public static boolean set(String key, Object value, Duration duration) {
        return staticCacheService.set(key, value, duration);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public static Object get(String key) {
        return staticCacheService.get(key);
    }

    /**
     * 获取缓存并指定类型
     *
     * @param key   缓存键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 缓存值
     */
    public static <T> T get(String key, Class<T> clazz) {
        return staticCacheService.get(key, clazz);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        return staticCacheService.delete(key);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存键集合
     * @return 删除的数量
     */
    public static Long delete(Collection<String> keys) {
        return staticCacheService.delete(keys);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    public static boolean exists(String key) {
        return staticCacheService.exists(key);
    }

    /**
     * 设置过期时间
     *
     * @param key      缓存键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public static boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return staticCacheService.expire(key, timeout, timeUnit);
    }

    /**
     * 设置过期时间
     *
     * @param key      缓存键
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public static boolean expire(String key, Duration duration) {
        return staticCacheService.expire(key, duration);
    }

    /**
     * 获取过期时间
     *
     * @param key      缓存键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    public static Long getExpire(String key, TimeUnit timeUnit) {
        return staticCacheService.getExpire(key, timeUnit);
    }

    // =============================原子操作=============================

    /**
     * 递增操作
     *
     * @param key   缓存键
     * @param delta 增量
     * @return 递增后的值
     */
    public static Long increment(String key, long delta) {
        return staticCacheService.increment(key, delta);
    }

    /**
     * 递增操作（默认增量为1）
     *
     * @param key 缓存键
     * @return 递增后的值
     */
    public static Long increment(String key) {
        return staticCacheService.increment(key);
    }

    /**
     * 递减操作
     *
     * @param key   缓存键
     * @param delta 减量
     * @return 递减后的值
     */
    public static Long decrement(String key, long delta) {
        return staticCacheService.decrement(key, delta);
    }

    /**
     * 递减操作（默认减量为1）
     *
     * @param key 缓存键
     * @return 递减后的值
     */
    public static Long decrement(String key) {
        return staticCacheService.decrement(key);
    }

    // =============================Hash操作=============================

    /**
     * 获取Hash中的值
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @return Hash值
     */
    public static Object hGet(String key, String hashKey) {
        return staticCacheService.hGet(key, hashKey);
    }

    /**
     * 获取Hash中的所有键值对
     *
     * @param key Hash键
     * @return Hash中的所有键值对
     */
    public static Map<Object, Object> hGetAll(String key) {
        return staticCacheService.hGetAll(key);
    }

    /**
     * 设置Hash中的值
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @param value   Hash值
     * @return 是否设置成功
     */
    public static boolean hSet(String key, String hashKey, Object value) {
        return staticCacheService.hSet(key, hashKey, value);
    }

    /**
     * 批量设置Hash值
     *
     * @param key Hash键
     * @param map 要设置的键值对
     * @return 是否设置成功
     */
    public static boolean hSetAll(String key, Map<String, Object> map) {
        return staticCacheService.hSetAll(key, map);
    }

    /**
     * 删除Hash中的字段
     *
     * @param key      Hash键
     * @param hashKeys Hash字段
     * @return 删除的字段数量
     */
    public static Long hDelete(String key, Object... hashKeys) {
        return staticCacheService.hDelete(key, hashKeys);
    }

    /**
     * 判断Hash中是否存在指定字段
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @return 是否存在
     */
    public static boolean hExists(String key, String hashKey) {
        return staticCacheService.hExists(key, hashKey);
    }

    // =============================Set操作=============================

    /**
     * 获取Set中的所有值
     *
     * @param key Set键
     * @return Set集合
     */
    public static Set<Object> sMembers(String key) {
        return staticCacheService.sMembers(key);
    }

    /**
     * 向Set中添加值
     *
     * @param key    Set键
     * @param values 要添加的值
     * @return 成功添加的个数
     */
    public static Long sAdd(String key, Object... values) {
        return staticCacheService.sAdd(key, values);
    }

    /**
     * 判断元素是否在Set中
     *
     * @param key   Set键
     * @param value 要判断的元素
     * @return 是否存在
     */
    public static boolean sIsMember(String key, Object value) {
        return staticCacheService.sIsMember(key, value);
    }

    /**
     * 从Set中移除元素
     *
     * @param key    Set键
     * @param values 要移除的元素
     * @return 移除的元素个数
     */
    public static Long sRemove(String key, Object... values) {
        return staticCacheService.sRemove(key, values);
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
    public static List<Object> lRange(String key, long start, long end) {
        return staticCacheService.lRange(key, start, end);
    }

    /**
     * 向List右侧添加值
     *
     * @param key   List键
     * @param value 要添加的值
     * @return List的长度
     */
    public static Long lRightPush(String key, Object value) {
        return staticCacheService.lRightPush(key, value);
    }

    /**
     * 向List左侧添加值
     *
     * @param key   List键
     * @param value 要添加的值
     * @return List的长度
     */
    public static Long lLeftPush(String key, Object value) {
        return staticCacheService.lLeftPush(key, value);
    }

    /**
     * 从List右侧弹出值
     *
     * @param key List键
     * @return 弹出的值
     */
    public static Object lRightPop(String key) {
        return staticCacheService.lRightPop(key);
    }

    /**
     * 从List左侧弹出值
     *
     * @param key List键
     * @return 弹出的值
     */
    public static Object lLeftPop(String key) {
        return staticCacheService.lLeftPop(key);
    }

    /**
     * 获取List的长度
     *
     * @param key List键
     * @return List的长度
     */
    public static Long lSize(String key) {
        return staticCacheService.lSize(key);
    }
}