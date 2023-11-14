/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.handlerInterceptor
 * @className com.zvz09.xiaochen.common.web.interceptor.ApiLogInterceptor
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.interceptor;

import com.zvz09.xiaochen.common.web.config.properties.ApiLogProperties;
import com.zvz09.xiaochen.common.web.util.Snowflake;
import com.zvz09.xiaochen.common.web.wrapper.BodyCachingHttpServletRequestWrapper;
import com.zvz09.xiaochen.common.web.wrapper.BodyCachingHttpServletResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * ApiLogInterceptor
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 17:38
 */
@Slf4j
@Component("apiLogInterceptor")
@RequiredArgsConstructor
public class ApiLogInterceptor implements HandlerInterceptor {

    private final ApiLogProperties apiLogProperties;

    private static final String TRACE_ID = "traceId";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (request.getHeader(TRACE_ID) != null) {
                MDC.put(TRACE_ID, request.getHeader(TRACE_ID));
            } else {
                MDC.put(TRACE_ID, Snowflake.getSnowflakeId());
            }
            logRequest(request, handler);
        } catch (Exception e) {
            log.error("logRequest error", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            logResponse(request, response, handler);
            MDC.remove(TRACE_ID);
        } catch (Exception e) {
            log.error("logResponse error", e);
        }
    }

    private void logRequest(HttpServletRequest request, Object handler) throws IOException {

        String url = getUrl(request);
        if (url == null) {
            return;
        }
        String body = "";
        if ((request instanceof BodyCachingHttpServletRequestWrapper requestWrapper)) {
            body = new String(requestWrapper.getBody());
        }
        log.info("接收到来自【{}】请求,方式【{}】，路径【{}】，请求体【{}】", request.getRemoteAddr(), request.getMethod(), url, body);
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = getUrl(request);
        if (url == null) {
            return;
        }
        String body = "";
        if ((response instanceof BodyCachingHttpServletResponseWrapper responseWrapper)) {
            if (response.getContentType()!=null && !response.getContentType().contains("application/octet-stream")) {
                body = new String(responseWrapper.getBody());
            }
        }
        log.info("响应来自【{}】请求,方式【{}】，路径【{}】，响应体【{}】", request.getRemoteAddr(), request.getMethod(), url, body);
    }

    private String getUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (apiLogProperties.getExcludeUrl() != null && !apiLogProperties.getExcludeUrl().isEmpty()) {
            if (apiLogProperties.getExcludeUrl().contains(url)) {
                return null;
            }
        }
        String queryParams = request.getQueryString();

        if (queryParams != null) {
            try {
                url = url + "?" + URLDecoder.decode(queryParams, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("拼接queryString error", e);
            }
        }
        return url;
    }
}
 