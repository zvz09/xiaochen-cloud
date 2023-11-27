package com.zvz09.xiaochen.flowable.config;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.util.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.cfg.IdGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * 流程id生成处理
 *
 * @author zvz09
 * @date 2022-12-26 10:24
 */
@Slf4j
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        engineConfiguration.setIdGenerator(new IdGenerator() {
            @Override
            public String getNextId() {
                try {
                    return Snowflake.getSnowflakeId(); //id生成方法
                } catch (Exception e) {
                    log.error("生成ID报错", e);
                    throw new BusinessException("生成ID报错");
                }
            }
        });
    }

}
