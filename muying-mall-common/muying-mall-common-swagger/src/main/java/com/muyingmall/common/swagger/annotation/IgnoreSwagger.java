package com.muyingmall.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * 忽略Swagger文档注解
 * 用于标记不需要在API文档中显示的接口或参数
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSwagger {

    /**
     * 忽略原因
     */
    String value() default "内部接口，不对外开放";
}