package com.zvz09.xiaochen.k8s.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author zvz09
 */

@EnableCaching
@EnableScheduling
@EnableDiscoveryClient
@ComponentScan("com.zvz09.xiaochen")
@EnableAsync(proxyTargetClass = true)
@EnableFeignClients(basePackages = "com.zvz09.xiaochen")
@MapperScan(basePackages = "com.zvz09.xiaochen.**.mapper")
@SpringBootApplication
public class K8sManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(K8sManageApplication.class, args);
        System.out.println("k8s管理服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
