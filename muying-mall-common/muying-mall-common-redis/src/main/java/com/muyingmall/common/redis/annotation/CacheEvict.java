package com.muyingmall.common.redis.annotation;

import java.lang.annotation.*;

/**
 * 缓存清除注解
 * 
 * <p>用于标记需要清除缓存的方法，支持清除单个或多个缓存键。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @CacheEvict(key = "user:#{#userId}")
 * public void updateUser(Long userId, User user) {
 *     userRepository.update(user);
 * }
 * 
 * @CacheEvict(key = {"user:#{#user.id}", "user:profile:#{#user.id}"})
 * public void deleteUser(User user) {
 *     userRepository.delete(user);
 * }
 * 
 * @CacheEvict(keyPattern = "product:*", allEntries = true)
 * public void clearAllProductCache() {
 *     // 清除所有产品相关缓存
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
public @interface CacheEvict {

    /**
     * 要清除的缓存键，支持SpEL表达式
     * 
     * <p>可以指定单个键或多个键</p>
     *
     * @return 缓存键数组
     */
    String[] key() default {};

    /**
     * 缓存键模式，支持通配符
     * 
     * <p>例如："user:*" 会清除所有以"user:"开头的缓存</p>
     *
     * @return 缓存键模式
     */
    String keyPattern() default "";

    /**
     * 是否清除所有缓存
     *
     * @return 是否清除所有缓存，默认false
     */
    boolean allEntries() default false;

    /**
     * 清除条件，支持SpEL表达式
     * 
     * <p>只有当条件为true时才会清除缓存</p>
     *
     * @return 清除条件表达式
     */
    String condition() default "";

    /**
     * 是否在方法执行前清除缓存
     * 
     * <p>默认在方法执行后清除，设置为true则在方法执行前清除</p>
     *
     * @return 是否在方法执行前清除
     */
    boolean beforeInvocation() default false;
}