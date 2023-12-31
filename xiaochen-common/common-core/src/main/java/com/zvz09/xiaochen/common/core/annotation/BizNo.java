package com.zvz09.xiaochen.common.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zvz09
 * 结合 swagger.v3 的Operation注解一起使用，描述统一swagger.v3的注解里取。本注解只取操作对象的唯一id。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizNo {
    /**
     *  操作日志绑定的业务对象标识  SpEL 表达式
     */
    String spEl() default "";
}
