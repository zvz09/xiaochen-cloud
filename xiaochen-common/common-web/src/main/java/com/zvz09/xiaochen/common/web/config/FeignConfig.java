package com.zvz09.xiaochen.common.web.config;

import com.zvz09.xiaochen.common.core.constant.SecurityConstants;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zvz09.xiaochen.common.core.constant.CommonConstant.TRACE_ID;

/**
 * @author zvz09
 * @version 1.0
 * @date 2023-03-09 17:37
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Bean
    public Logger.Level level() {
        /*
            日志级别：
            NONE（不记录日志 (默认)）
            BASIC（只记录请求方法和URL以及响应状态代码和执行时间）
            HEADERS（记录请求和应答的头的基本信息）
            FULL（记录请求和响应的头信息，正文和元数据）
         */
        return Logger.Level.FULL;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (StringUtils.isNoneBlank(MDC.get(TRACE_ID))) {
            requestTemplate.header(TRACE_ID, MDC.get(TRACE_ID));
        }
        if (SecurityContextHolder.getUserId() != null) {
            requestTemplate.header(SecurityConstants.DETAILS_USER_ID, String.valueOf(SecurityContextHolder.getUserId()));
        }
        if (StringUtils.isNoneBlank(SecurityContextHolder.getUserName())) {
            requestTemplate.header(SecurityConstants.DETAILS_USERNAME, SecurityContextHolder.getUserName());
        }
        if (SecurityContextHolder.getRoleId() != null) {
            requestTemplate.header(SecurityConstants.DETAILS_AUTHORITY_ID, String.valueOf(SecurityContextHolder.getRoleId()));
        }
        if (StringUtils.isNoneBlank(SecurityContextHolder.getRoleCode())) {
            requestTemplate.header(SecurityConstants.DETAILS_AUTHORITY_CODE, SecurityContextHolder.getRoleCode());
        }
    }
}
