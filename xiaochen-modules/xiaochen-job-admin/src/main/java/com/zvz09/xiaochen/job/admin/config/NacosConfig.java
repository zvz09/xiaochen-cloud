package com.zvz09.xiaochen.job.admin.config;

import com.alibaba.nacos.common.notify.NotifyCenter;
import com.zvz09.xiaochen.job.admin.listener.InstancesChangeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosConfig implements ApplicationRunner{

    private final InstancesChangeListener instancesChangeListener;

    private final DiscoveryClient discoveryClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("注册自定义nacos 监听");
        NotifyCenter.registerSubscriber(instancesChangeListener);
    }
}
