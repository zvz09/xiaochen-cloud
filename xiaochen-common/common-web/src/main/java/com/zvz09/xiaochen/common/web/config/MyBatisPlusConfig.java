/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.config
 * @className com.zvz09.xiaochen.common.web.config.MyBatisPlusConfig
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.config;

/**
 * MyBatisPlusConfig
 *
 * @description
 * @author zvz09
 * @date 2023/9/12 10:15
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataChangeRecorderInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 *
 * @author zvz09
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * MyBatisPlus拦截器（用于分页）
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //添加MySQL的分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //添加数据变更记录拦截器
        interceptor.addInnerInterceptor(new DataChangeRecorderInnerInterceptor());
        return interceptor;
    }
}

 