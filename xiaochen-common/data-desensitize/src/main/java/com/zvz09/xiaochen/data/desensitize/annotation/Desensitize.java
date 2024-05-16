package com.zvz09.xiaochen.data.desensitize.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zvz09.xiaochen.data.desensitize.filter.DesensitizeValueFilter;
import com.zvz09.xiaochen.data.desensitize.serializer.DesensitizeJsonSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FastJson / Jackson 脱敏配置元注解
 * <p>
 * Jackson 标注在字段上则标记该字段执行 http 序列化 (返回 VO)时脱敏 (http-message-converts 用的是 jackson)
 * <p>
 * FastJson 需要组合 {@link DesensitizeObject} 一起使用
 * JSON.toJSONString 时需要使用过滤器 {@link DesensitizeValueFilter}
 * - 全局日志打印 {@see LogPrinterInterceptor}
 * - 操作日志切面 {@see OperatorLogAspect}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizeJsonSerializer.class)
public @interface Desensitize {

    /**
     * 一般用于操作日志参数脱敏
     *
     * @return 是否直接设置为空
     */
    boolean toEmpty() default false;

    /**
     * @return 起始保留字符数
     */
    int keepStart() default 1;

    /**
     * @return 结束保留字符数
     */
    int keepEnd() default 1;

    /**
     * @return 脱敏字符
     */
    char replacer() default '*';

}
