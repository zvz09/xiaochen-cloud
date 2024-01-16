package com.zvz09.xiaochen.log.server;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
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
@EnableFeignClients(basePackages = "com.zvz09.xiaochen")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class LogServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogServerApplication.class, args);
        System.out.println("日志查询服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
