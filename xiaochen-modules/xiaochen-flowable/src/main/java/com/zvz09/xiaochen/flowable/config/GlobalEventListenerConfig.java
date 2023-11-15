package com.zvz09.xiaochen.flowable.config;

import com.zvz09.xiaochen.flowable.listener.GlobalEventListener;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author zvz09
 * 已废弃 会引起循环依赖问题  https://juejin.cn/post/6844904001285128199
 */
@Deprecated
//@Configuration
@RequiredArgsConstructor
public class GlobalEventListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final GlobalEventListener globalEventListener;
    private final RuntimeService runtimeService;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        // 流程正常结束
        runtimeService.addEventListener(globalEventListener);
    }
}
