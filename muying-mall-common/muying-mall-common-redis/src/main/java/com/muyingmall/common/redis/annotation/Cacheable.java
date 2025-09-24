package com.muyingmall.common.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存注解
 * 
 * <p>用于标记需要缓存的方法，支持自动缓存方法的返回值。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @Cacheable(key = "user:#{#userId}", expire = 3600)
 * public User getUserById(Long userId) {
 *     return userRepository.findById(userId);
 * }
 * 
 * @Cacheable(key = "product:list:#{#categoryId}", expire = 30, timeUnit = TimeUnit.MINUTES)
 * public List<Product> getProductsByCategory(Long categoryId) {
 *     return productRepository.findByCategoryId(categoryId);
 * }
 * }</pre>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

    /**
     * 缓存键，支持SpEL表达式
     * 
     * <p>SpEL表达式示例：</p>
     * <ul>
     *   <li>"user:#{#userId}" - 使用方法参数</li>
     *   <li>"product:#{#product.id}" - 使用对象属性</li>
     *   <li>"list:#{#page}:#{#size}" - 使用多个参数</li>
     * </ul>
     *
     * @return 缓存键
     */
    String key();

    /**
     * 缓存过期时间
     *
     * @return 过期时间，默认3600秒（1小时）
     */
    long expire() default 3600;

    /**
     * 时间单位
     *
     * @return 时间单位，默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 缓存条件，支持SpEL表达式
     * 
     * <p>只有当条件为true时才会缓存</p>
     *
     * @return 缓存条件表达式
     */
    String condition() default "";

    /**
     * 排除条件，支持SpEL表达式
     * 
     * <p>当条件为true时不会缓存</p>
     *
     * @return 排除条件表达式
     */
    String unless() default "";

    /**
     * 是否缓存空值
     *
     * @return 是否缓存空值，默认false
     */
    boolean cacheNullValues() default false;
}