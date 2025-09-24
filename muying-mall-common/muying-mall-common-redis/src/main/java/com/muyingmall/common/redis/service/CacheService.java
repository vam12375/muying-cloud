package com.muyingmall.common.redis.service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务接口
 * 
 * <p>定义了缓存操作的标准接口，提供统一的缓存操作方法。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>基本的缓存CRUD操作</li>
 *   <li>过期时间管理</li>
 *   <li>批量操作</li>
 *   <li>数据结构操作（Hash、Set、List）</li>
 *   <li>原子操作（递增、递减）</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
public interface CacheService {

    // =============================基本操作=============================

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return 是否设置成功
     */
    boolean set(String key, Object value);

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    boolean set(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param duration 过期时间
     * @return 是否设置成功
     */
    boolean set(String key, Object value, Duration duration);

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存值
     */
    Object get(String key);

    /**
     * 获取缓存并指定类型
     *
     * @param key   缓存键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 缓存值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     * @return 是否删除成功
     */
    boolean delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys 缓存键集合
     * @return 删除的数量
     */
    Long delete(Collection<String> keys);

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 设置过期时间
     *
     * @param key      缓存键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    boolean expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 设置过期时间
     *
     * @param key      缓存键
     * @param duration 过期时间
     * @return 是否设置成功
     */
    boolean expire(String key, Duration duration);

    /**
     * 获取过期时间
     *
     * @param key      缓存键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    Long getExpire(String key, TimeUnit timeUnit);

    // =============================原子操作=============================

    /**
     * 递增操作
     *
     * @param key   缓存键
     * @param delta 增量
     * @return 递增后的值
     */
    Long increment(String key, long delta);

    /**
     * 递增操作（默认增量为1）
     *
     * @param key 缓存键
     * @return 递增后的值
     */
    Long increment(String key);

    /**
     * 递减操作
     *
     * @param key   缓存键
     * @param delta 减量
     * @return 递减后的值
     */
    Long decrement(String key, long delta);

    /**
     * 递减操作（默认减量为1）
     *
     * @param key 缓存键
     * @return 递减后的值
     */
    Long decrement(String key);

    // =============================Hash操作=============================

    /**
     * 获取Hash中的值
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @return Hash值
     */
    Object hGet(String key, String hashKey);

    /**
     * 获取Hash中的所有键值对
     *
     * @param key Hash键
     * @return Hash中的所有键值对
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 设置Hash中的值
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @param value   Hash值
     * @return 是否设置成功
     */
    boolean hSet(String key, String hashKey, Object value);

    /**
     * 批量设置Hash值
     *
     * @param key Hash键
     * @param map 要设置的键值对
     * @return 是否设置成功
     */
    boolean hSetAll(String key, Map<String, Object> map);

    /**
     * 删除Hash中的字段
     *
     * @param key      Hash键
     * @param hashKeys Hash字段
     * @return 删除的字段数量
     */
    Long hDelete(String key, Object... hashKeys);

    /**
     * 判断Hash中是否存在指定字段
     *
     * @param key     Hash键
     * @param hashKey Hash字段
     * @return 是否存在
     */
    boolean hExists(String key, String hashKey);

    // =============================Set操作=============================

    /**
     * 获取Set中的所有值
     *
     * @param key Set键
     * @return Set集合
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set中添加值
     *
     * @param key    Set键
     * @param values 要添加的值
     * @return 成功添加的个数
     */
    Long sAdd(String key, Object... values);

    /**
     * 判断元素是否在Set中
     *
     * @param key   Set键
     * @param value 要判断的元素
     * @return 是否存在
     */
    boolean sIsMember(String key, Object value);

    /**
     * 从Set中移除元素
     *
     * @param key    Set键
     * @param values 要移除的元素
     * @return 移除的元素个数
     */
    Long sRemove(String key, Object... values);

    // =============================List操作=============================

    /**
     * 获取List中的内容
     *
     * @param key   List键
     * @param start 开始位置
     * @param end   结束位置
     * @return List内容
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 向List右侧添加值
     *
     * @param key   List键
     * @param value 要添加的值
     * @return List的长度
     */
    Long lRightPush(String key, Object value);

    /**
     * 向List左侧添加值
     *
     * @param key   List键
     * @param value 要添加的值
     * @return List的长度
     */
    Long lLeftPush(String key, Object value);

    /**
     * 从List右侧弹出值
     *
     * @param key List键
     * @return 弹出的值
     */
    Object lRightPop(String key);

    /**
     * 从List左侧弹出值
     *
     * @param key List键
     * @return 弹出的值
     */
    Object lLeftPop(String key);

    /**
     * 获取List的长度
     *
     * @param key List键
     * @return List的长度
     */
    Long lSize(String key);
}