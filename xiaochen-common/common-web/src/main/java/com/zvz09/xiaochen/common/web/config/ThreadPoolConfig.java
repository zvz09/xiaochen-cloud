package com.zvz09.xiaochen.common.web.config;

import com.zvz09.xiaochen.common.web.wrapper.ThreadPoolExecutorMdcWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zvz09
 * @version 1.0
 * @date 2023-03-21 10:21
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * 消息处理线程池配置
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor poolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolExecutorMdcWrapper();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("task_");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
