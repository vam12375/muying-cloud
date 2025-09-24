package com.muyingmall.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * API响应示例容器注解
 * 用于支持多个响应示例
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseExamples {

    /**
     * 响应示例数组
     */
    ApiResponseExample[] value();
}