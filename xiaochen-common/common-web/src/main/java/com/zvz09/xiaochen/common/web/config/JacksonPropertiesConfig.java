package com.zvz09.xiaochen.common.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;


/**
 * @author lizili-YF0033
 */
@Configuration
public class JacksonPropertiesConfig {
    @Bean
    JacksonProperties jacksonProperties() {
        JacksonProperties properties = new JacksonProperties();
        properties.setDateFormat("yyyy-MM-dd HH:mm:ss");
        properties.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 格式化输出
        properties.getSerialization().put(SerializationFeature.INDENT_OUTPUT, false);
        // 忽略无法转换的对象
        properties.getSerialization().put(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 允许对象忽略json中不存在的属性
        properties.getDeserialization().put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return properties;
    }
}
