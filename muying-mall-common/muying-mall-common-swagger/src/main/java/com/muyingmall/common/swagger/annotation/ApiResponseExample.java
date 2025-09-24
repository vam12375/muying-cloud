package com.muyingmall.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * API响应示例注解
 * 用于为API接口提供响应示例
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ApiResponseExamples.class)
public @interface ApiResponseExample {

    /**
     * HTTP状态码
     */
    int code() default 200;

    /**
     * 响应描述
     */
    String description() default "";

    /**
     * 响应示例内容
     */
    String example() default "";

    /**
     * 响应示例媒体类型
     */
    String mediaType() default "application/json";
}