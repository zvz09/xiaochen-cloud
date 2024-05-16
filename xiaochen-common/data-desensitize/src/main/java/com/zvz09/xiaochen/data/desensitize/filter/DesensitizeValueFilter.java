package com.zvz09.xiaochen.data.desensitize.filter;

import cn.hutool.core.annotation.AnnotationUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.zvz09.xiaochen.common.core.constant.Const;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.data.desensitize.annotation.Desensitize;
import com.zvz09.xiaochen.data.desensitize.annotation.DesensitizeObject;
import com.zvz09.xiaochen.data.desensitize.utils.Desensitizes;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * fastjson 脱敏序列化器
 * <p>
 * 用于全局日志打印
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 12:19
 */
public class DesensitizeValueFilter implements ValueFilter {

    private static final Map<String, Map<String, Desensitize>> DESENSITIZE_FIELDS = new HashMap<>();

    @Override
    public Object process(Object object, String name, Object value) {
        if (object == null || value == null) {
            return value;
        }
        Desensitize config = this.doDesensitizeField(object, name);
        // 无需脱敏
        if (config == null) {
            return value;
        }
        if (config.toEmpty()) {
            // 设置为空
            return Const.EMPTY;
        } else {
            // 脱敏
            return Desensitizes.mix(Desensitizes.object2String(value), config.keepStart(), config.keepEnd(), config.replacer());
        }
    }

    /**
     * 是否要执行脱敏
     *
     * @param object object
     * @param name   name
     * @return 是否执行
     */
    private Desensitize doDesensitizeField(Object object, String name) {
        // 查询缓存
        String className = object.getClass().toString();
        Map<String, Desensitize> fields = DESENSITIZE_FIELDS.get(className);
        if (fields == null) {
            // 查询脱敏配置
            fields = this.initClassDesensitize(object);
            DESENSITIZE_FIELDS.put(className, fields);
        }
        // 无需脱敏
        if (fields.isEmpty()) {
            return null;
        }
        return fields.get(name);
    }

    /**
     * 初始化脱敏配置
     *
     * @param object object
     * @return config
     */
    private Map<String, Desensitize> initClassDesensitize(Object object) {
        Class<?> dataClass = object.getClass();
        // 检查是否为脱敏对象
        DesensitizeObject has = AnnotationUtil.getAnnotation(dataClass, DesensitizeObject.class);
        if (has == null) {
            return new HashMap<>();
        }
        Map<String, Desensitize> config = new HashMap<>();
        // 获取对象字段
        List<Field> fields = getFields(dataClass);
        // 查询脱敏配置
        for (Field field : fields) {
            // 脱敏注解
            Desensitize desensitize = AnnotationUtil.getAnnotation(field, Desensitize.class);
            if (desensitize == null) {
                continue;
            }
            // json 注解
            JSONField jsonField = AnnotationUtil.getAnnotation(field, JSONField.class);
            String fieldName = field.getName();
            String jsonFieldName;
            if (jsonField != null && !StringUtils.isBlank(jsonFieldName = jsonField.name())) {
                fieldName = jsonFieldName;
            }
            config.put(fieldName, desensitize);
        }
        return config;
    }

    public static List<Field> getFields(Class<?> clazz) {
        if(clazz == null){
            throw new BusinessException("field class is null");
        }
        if (clazz.getSuperclass() != null) {
            List<Field> fieldList = (List) Stream.of(clazz.getDeclaredFields()).filter((field) -> {
                return !Modifier.isStatic(field.getModifiers());
            }).filter((field) -> {
                return !Modifier.isTransient(field.getModifiers());
            }).collect(Collectors.toList());
            Class<?> superClass = clazz.getSuperclass();
            Map<String, Field> fieldMap = (Map)fieldList.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
            getFields(superClass).stream().filter((field) -> {
                return !fieldMap.containsKey(field.getName());
            }).forEach(fieldList::add);
            return fieldList;
        } else {
            return new ArrayList();
        }
    }
}
