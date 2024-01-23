package com.zvz09.xiaochen.k8s.manage.runner;

import com.zvz09.xiaochen.k8s.manage.domain.entity.Cluster;
import com.zvz09.xiaochen.k8s.manage.filter.ProxyRoutes;
import com.zvz09.xiaochen.k8s.manage.service.IClusterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncRoute implements ApplicationRunner {

    private final ProxyRoutes proxyRoutes;
    private final IClusterService clusterService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        proxyRoutes.clear();
        List<Cluster> clusters = clusterService.list();
        if(clusters != null){
           proxyRoutes.addRoute(clusters);
        }

    }
}
