package com.zvz09.xiaochen.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lizili-YF0033
 */

@EnableCaching
@EnableFeignClients(basePackages = "com.zvz09.xiaochen")
@EnableDiscoveryClient
@ComponentScan("com.zvz09.xiaochen")
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("认证服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
