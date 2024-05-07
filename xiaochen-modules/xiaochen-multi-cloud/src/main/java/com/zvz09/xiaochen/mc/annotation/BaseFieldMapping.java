package com.zvz09.xiaochen.mc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zvz09
 * 结合 swagger.v3 的Operation注解一起使用，描述统一swagger.v3的注解里取。本注解只取操作对象的唯一id。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BaseFieldMapping {

    String value() default "";

    String outValue() default "";

}
