package com.zvz09.xiaochen.log.server;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author zvz09
 */
@EnableCaching
@EnableDiscoveryClient
@ComponentScan("com.zvz09.xiaochen")
@EnableAsync(proxyTargetClass = true)
@EsMapperScan("com.zvz09.xiaochen.log.server.mapper")
@EnableFeignClients(basePackages = "com.zvz09.xiaochen")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LogServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogServerApplication.class, args);
        System.out.println("日志查询服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
