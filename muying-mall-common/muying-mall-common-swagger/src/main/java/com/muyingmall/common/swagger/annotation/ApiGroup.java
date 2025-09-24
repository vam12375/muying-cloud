package com.muyingmall.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * API分组注解
 * 用于标记Controller所属的API分组
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiGroup {

    /**
     * 分组名称
     */
    String value() default "";

    /**
     * 分组描述
     */
    String description() default "";

    /**
     * 分组排序
     */
    int order() default 0;
}