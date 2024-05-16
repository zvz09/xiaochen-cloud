package com.zvz09.xiaochen.data.desensitize.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FastJson 脱敏配置元注解
 * <p>
 * 标注在类上则标记该类执行序列化时会执行脱敏
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:58
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DesensitizeObject {

}
