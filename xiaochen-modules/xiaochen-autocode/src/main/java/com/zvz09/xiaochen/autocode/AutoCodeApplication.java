package com.zvz09.xiaochen.autocode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 网关启动程序
 *
 * @author zvz09
 */
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.zvz09.xiaochen")
@EnableAsync(proxyTargetClass = true)
@EnableFeignClients(basePackages = "com.zvz09.xiaochen")
@MapperScan(basePackages = "com.zvz09.xiaochen.*.mapper")
public class AutoCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoCodeApplication.class, args);
        System.out.println("代码生成服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
