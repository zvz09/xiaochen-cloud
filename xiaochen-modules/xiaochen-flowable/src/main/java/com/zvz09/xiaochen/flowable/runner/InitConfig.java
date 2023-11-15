package com.zvz09.xiaochen.flowable.runner;

import com.zvz09.xiaochen.flowable.listener.GlobalEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 * flowable 添加全局监听
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitConfig implements ApplicationRunner {

    private final GlobalEventListener globalEventListener;
    private final RuntimeService runtimeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 流程正常结束
        runtimeService.addEventListener(globalEventListener);
    }
}
