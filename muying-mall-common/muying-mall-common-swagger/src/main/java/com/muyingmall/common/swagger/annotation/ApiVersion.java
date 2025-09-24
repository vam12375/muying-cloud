package com.muyingmall.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * API版本注解
 * 用于标记API的版本信息
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    /**
     * API版本号
     */
    String value() default "1.0";

    /**
     * 版本描述
     */
    String description() default "";

    /**
     * 是否已废弃
     */
    boolean deprecated() default false;
}