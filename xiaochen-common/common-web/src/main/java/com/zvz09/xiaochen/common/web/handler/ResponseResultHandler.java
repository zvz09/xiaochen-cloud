package com.zvz09.xiaochen.common.web.handler;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.web.annotation.ResponseNotIntercept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;

/**
 * @author lizili-YF0033
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final List<PathPattern> notInterceptUriPatternList;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getDeclaringClass().isAnnotationPresent(ResponseNotIntercept.class)) {
            //若在类中加了@ResponseNotIntercept 则该类中的方法不用做统一的拦截
            return false;
        }
        if (returnType.getMethod().isAnnotationPresent(ResponseNotIntercept.class)) {
            //若方法上加了@ResponseNotIntercept 则该方法不用做统一的拦截
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (isNotInterceptUrI(request, response)) {
            return body;
        }
        if (body instanceof ApiResult) {
            // 提供一定的灵活度，如果body已经被包装了，就不进行包装
            return body;
        }
        if (body instanceof String) {
            //解决返回值为字符串时，不能正常包装
            return JacksonUtil.writeValueAsString(ApiResult.success(body));
        }
        return ApiResult.success(body);
    }

    private boolean isNotInterceptUrI(ServerHttpRequest request, ServerHttpResponse response) {

        if (MediaType.APPLICATION_JSON.equals(response.getHeaders().getContentType())) {
            return true;
        }
        if (notInterceptUriPatternList != null) {
            for (PathPattern pathPattern : notInterceptUriPatternList) {
                if (pathPattern.matches(PathContainer.parsePath(request.getURI().getPath()))) {
                    return true;
                }
            }
        }
        return false;
    }
}

