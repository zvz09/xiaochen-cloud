package com.zvz09.xiaochen.message;

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
 * @author ruoyi
 */
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.zvz09.xiaochen")
@EnableAsync(proxyTargetClass = true)
@MapperScan(basePackages = "com.zvz09.xiaochen.*.mapper")
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
        System.out.println("消息中心服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
