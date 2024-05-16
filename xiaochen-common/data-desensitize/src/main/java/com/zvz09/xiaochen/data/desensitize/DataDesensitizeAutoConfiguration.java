package com.zvz09.xiaochen.data.desensitize;

import com.zvz09.xiaochen.data.desensitize.filter.DesensitizeValueFilter;
import com.zvz09.xiaochen.data.desensitize.serializer.DesensitizeJsonSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@AutoConfiguration
public class DataDesensitizeAutoConfiguration {
    /**
     * 用于参数打印
     * - 全局日志打印
     * - 操作日志参数
     *
     * @return fastjson 序列化脱敏过滤器
     */
    @Primary
    @Bean
    public DesensitizeValueFilter desensitizeValueFilter() {
        return new DesensitizeValueFilter();
    }

    /**
     * 用于 HTTP 响应序列化
     *
     * @return jackson 序列化脱敏序列化
     */
    @Bean
    @ConditionalOnBean(MappingJackson2HttpMessageConverter.class)
    public DesensitizeJsonSerializer desensitizeJsonSerializer() {
        return new DesensitizeJsonSerializer();
    }

}
