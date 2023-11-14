/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.config
 * @className com.zvz09.xiaochen.auth.config.WebMvcConfig
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.config;

import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.common.web.interceptor.ApiLogInterceptor;
import com.zvz09.xiaochen.common.web.interceptor.HeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WebMvcConfig
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/28 16:24
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiLogInterceptor apiLogInterceptor;
    private final HeaderInterceptor headerInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> apiLogExcludePath = new ArrayList<>();
        apiLogExcludePath.add("/operationRecord/**");
        apiLogExcludePath.addAll(Arrays.asList(Constants.EXCLUDE_PATH_PATTERNS));
        registry.addInterceptor(headerInterceptor).addPathPatterns("/**").excludePathPatterns(Constants.EXCLUDE_PATH_PATTERNS);
        registry.addInterceptor(apiLogInterceptor).addPathPatterns("/**").excludePathPatterns(apiLogExcludePath);
    }
}
 