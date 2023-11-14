package com.zvz09.xiaochen.flowable.config;

import com.zvz09.xiaochen.flowable.listener.GlobalEventListener;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author lizili-YF0033
 */
@Configuration
@AllArgsConstructor
public class GlobalEventListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final GlobalEventListener globalEventListener;
    private final RuntimeService runtimeService;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        // 流程正常结束
        runtimeService.addEventListener(globalEventListener);
    }
}
