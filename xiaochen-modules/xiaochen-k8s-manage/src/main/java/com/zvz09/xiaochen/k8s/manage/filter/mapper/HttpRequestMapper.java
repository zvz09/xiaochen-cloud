package com.zvz09.xiaochen.k8s.manage.filter.mapper;

import com.zvz09.xiaochen.k8s.manage.domain.vo.RouteInstance;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;

import static java.util.Collections.list;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class HttpRequestMapper {

    public RequestEntity<byte[]> map(HttpServletRequest request, RouteInstance instance) throws IOException {
        byte[] body = extractBody(request);
        HttpHeaders headers = extractHeaders(request);
        headers.add("Authorization", "Bearer "+instance.getToken());
        HttpMethod method = extractMethod(request);
        URI uri = extractUri(request, instance);
        return new RequestEntity<>(body, headers, method, uri);
    }

    private URI extractUri(HttpServletRequest request, RouteInstance instance) throws UnsupportedEncodingException {
        //如果content path 不为空，移除content path
        String requestURI = StringUtils.isEmpty(request.getContextPath()) ? request.getRequestURI() :
                StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());

        //处理中文被自动编码问题
        String query = request.getQueryString() == null ? EMPTY : URLDecoder.decode(request.getQueryString(), "utf-8");

        // 需要重写path
        String prefix = StringUtils.substringBefore(instance.getPath(), "/**");
        requestURI = StringUtils.substringAfter(requestURI, prefix);
        URI redirectURL = UriComponentsBuilder.fromUriString(instance.getUrl() + requestURI).query(query).build().encode().toUri();
        log.info("real request url: {}", redirectURL.toString());
        return redirectURL;
    }

    private HttpMethod extractMethod(HttpServletRequest request) {
        return HttpMethod.valueOf(request.getMethod());
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            List<String> value = list(request.getHeaders(name));
            headers.put(name, value);
        }
        return headers;
    }

    private byte[] extractBody(HttpServletRequest request) throws IOException {
        return toByteArray(request.getInputStream());
    }
}

