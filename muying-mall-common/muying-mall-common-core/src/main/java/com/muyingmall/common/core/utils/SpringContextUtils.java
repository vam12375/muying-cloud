package com.muyingmall.common.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文工具类
 * 
 * <p>用于在非Spring管理的类中获取Spring容器中的Bean实例。
 * 实现了ApplicationContextAware接口，在Spring容器启动时自动注入ApplicationContext。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>获取Spring应用上下文</li>
 *   <li>根据Bean名称获取Bean实例</li>
 *   <li>根据Bean类型获取Bean实例</li>
 *   <li>根据名称和类型获取Bean实例</li>
 *   <li>检查Bean是否存在</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // 根据类型获取Bean
 * UserService userService = SpringContextUtils.getBean(UserService.class);
 * 
 * // 根据名称获取Bean
 * Object userService = SpringContextUtils.getBean("userService");
 * 
 * // 根据名称和类型获取Bean
 * UserService userService = SpringContextUtils.getBean("userService", UserService.class);
 * 
 * // 检查Bean是否存在
 * boolean exists = SpringContextUtils.containsBean("userService");
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>该工具类必须在Spring容器中注册为Bean才能正常工作</li>
 *   <li>在Spring容器完全启动之前，ApplicationContext可能为null</li>
 *   <li>建议在使用前检查ApplicationContext是否已初始化</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * Spring应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置Spring应用上下文
     * 
     * <p>该方法由Spring容器自动调用，用于注入ApplicationContext实例。</p>
     *
     * @param applicationContext Spring应用上下文
     * @throws BeansException 如果设置过程中发生异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获取Spring应用上下文
     * 
     * <p>返回当前的ApplicationContext实例。</p>
     *
     * @return Spring应用上下文，如果未初始化则返回null
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 检查Spring应用上下文是否已初始化
     *
     * @return true表示已初始化，false表示未初始化
     */
    public static boolean isApplicationContextInitialized() {
        return applicationContext != null;
    }

    /**
     * 根据Bean名称获取Bean实例
     * 
     * <p>从Spring容器中获取指定名称的Bean实例。</p>
     *
     * @param name Bean名称
     * @return Bean实例，如果不存在则抛出异常
     * @throws IllegalStateException 如果ApplicationContext未初始化
     * @throws BeansException 如果获取Bean过程中发生异常
     */
    public static Object getBean(String name) {
        checkApplicationContext();
        return applicationContext.getBean(name);
    }

    /**
     * 根据Bean类型获取Bean实例
     * 
     * <p>从Spring容器中获取指定类型的Bean实例。</p>
     *
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例，如果不存在则抛出异常
     * @throws IllegalStateException 如果ApplicationContext未初始化
     * @throws BeansException 如果获取Bean过程中发生异常
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据Bean名称和类型获取Bean实例
     * 
     * <p>从Spring容器中获取指定名称和类型的Bean实例。</p>
     *
     * @param name  Bean名称
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例，如果不存在则抛出异常
     * @throws IllegalStateException 如果ApplicationContext未初始化
     * @throws BeansException 如果获取Bean过程中发生异常
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 检查容器中是否包含指定名称的Bean
     *
     * @param name Bean名称
     * @return true表示包含，false表示不包含
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static boolean containsBean(String name) {
        checkApplicationContext();
        return applicationContext.containsBean(name);
    }

    /**
     * 检查指定名称的Bean是否为单例
     *
     * @param name Bean名称
     * @return true表示是单例，false表示不是单例
     * @throws IllegalStateException 如果ApplicationContext未初始化
     * @throws BeansException 如果Bean不存在
     */
    public static boolean isSingleton(String name) {
        checkApplicationContext();
        return applicationContext.isSingleton(name);
    }

    /**
     * 检查指定名称的Bean是否为原型
     *
     * @param name Bean名称
     * @return true表示是原型，false表示不是原型
     * @throws IllegalStateException 如果ApplicationContext未初始化
     * @throws BeansException 如果Bean不存在
     */
    public static boolean isPrototype(String name) {
        checkApplicationContext();
        return applicationContext.isPrototype(name);
    }

    /**
     * 获取指定名称Bean的类型
     *
     * @param name Bean名称
     * @return Bean的类型，如果Bean不存在则返回null
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static Class<?> getType(String name) {
        checkApplicationContext();
        return applicationContext.getType(name);
    }

    /**
     * 获取指定名称Bean的所有别名
     *
     * @param name Bean名称
     * @return Bean的别名数组
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static String[] getAliases(String name) {
        checkApplicationContext();
        return applicationContext.getAliases(name);
    }

    /**
     * 根据类型获取所有匹配的Bean名称
     *
     * @param type Bean类型
     * @return 匹配的Bean名称数组
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        checkApplicationContext();
        return applicationContext.getBeanNamesForType(type);
    }

    /**
     * 根据类型获取所有匹配的Bean实例
     *
     * @param type Bean类型
     * @param <T>  Bean类型泛型
     * @return 匹配的Bean实例Map，key为Bean名称，value为Bean实例
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static <T> java.util.Map<String, T> getBeansOfType(Class<T> type) {
        checkApplicationContext();
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 尝试获取Bean实例，如果不存在则返回null
     * 
     * <p>与getBean方法不同，该方法在Bean不存在时不会抛出异常，而是返回null。</p>
     *
     * @param name Bean名称
     * @return Bean实例，如果不存在则返回null
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static Object getBeanOrNull(String name) {
        checkApplicationContext();
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 尝试根据类型获取Bean实例，如果不存在则返回null
     * 
     * <p>与getBean方法不同，该方法在Bean不存在时不会抛出异常，而是返回null。</p>
     *
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例，如果不存在则返回null
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static <T> T getBeanOrNull(Class<T> clazz) {
        checkApplicationContext();
        try {
            return applicationContext.getBean(clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 尝试根据名称和类型获取Bean实例，如果不存在则返回null
     * 
     * <p>与getBean方法不同，该方法在Bean不存在时不会抛出异常，而是返回null。</p>
     *
     * @param name  Bean名称
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例，如果不存在则返回null
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    public static <T> T getBeanOrNull(String name, Class<T> clazz) {
        checkApplicationContext();
        try {
            return applicationContext.getBean(name, clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 检查ApplicationContext是否已初始化
     * 
     * <p>如果未初始化，抛出IllegalStateException异常。</p>
     *
     * @throws IllegalStateException 如果ApplicationContext未初始化
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext未初始化，请确保SpringContextUtils已在Spring容器中注册为Bean");
        }
    }
}