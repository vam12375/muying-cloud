package com.muyingmall.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文工具类 - 兼容性包装器
 * 
 * @deprecated 该类已迁移到 {@link com.muyingmall.common.core.utils.SpringContextUtils}，请使用新的位置。
 * 此类仅为向后兼容而保留，将在未来版本中移除。
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 */
@Deprecated
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SpringContextUtils#getApplicationContext()}
     */
    @Deprecated
    public static ApplicationContext getApplicationContext() {
        return com.muyingmall.common.core.utils.SpringContextUtils.getApplicationContext();
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SpringContextUtils#getBean(String)}
     */
    @Deprecated
    public static Object getBean(String name) {
        return com.muyingmall.common.core.utils.SpringContextUtils.getBean(name);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SpringContextUtils#getBean(Class)}
     */
    @Deprecated
    public static <T> T getBean(Class<T> clazz) {
        return com.muyingmall.common.core.utils.SpringContextUtils.getBean(clazz);
    }

    /**
     * @deprecated 使用 {@link com.muyingmall.common.core.utils.SpringContextUtils#getBean(String, Class)}
     */
    @Deprecated
    public static <T> T getBean(String name, Class<T> clazz) {
        return com.muyingmall.common.core.utils.SpringContextUtils.getBean(name, clazz);
    }
}