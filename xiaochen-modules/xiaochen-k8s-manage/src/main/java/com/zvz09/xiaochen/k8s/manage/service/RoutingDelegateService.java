package com.zvz09.xiaochen.k8s.manage.service;

import com.zvz09.xiaochen.common.core.response.ApiCode;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.k8s.manage.domain.vo.RouteInstance;
import com.zvz09.xiaochen.k8s.manage.filter.mapper.HttpRequestMapper;
import com.zvz09.xiaochen.k8s.manage.filter.mapper.HttpResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Administrator
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingDelegateService {

    private final HttpResponseMapper responseMapper;

    private final HttpRequestMapper requestMapper;

    private final RestTemplate restTemplate;

    /**
     * 根据相应策略转发请求到对应后端服务
     *
     * @param instance RouteInstance
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doForward(RouteInstance instance, HttpServletRequest request, HttpServletResponse response) {
        // 转发请求
        try {
            goForward(request, response, instance);
        } catch (Exception e) {
            // 连接超时、返回异常
            log.error("转发异常:",e);
            ApiResult<String> result = new ApiResult<>();
            result.setCode(ApiCode.FAIL.getCode());
            result.setMsg(ApiCode.FAIL.getMsg());

            renderString(response, result.toString());
        }

    }

    /**
     * 发送请求到对应后端服务
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param instance RouteInstance
     * @throws IOException
     */
    private void goForward(HttpServletRequest request, HttpServletResponse response, RouteInstance instance) throws IOException {
        RequestEntity<byte[]> requestEntity = requestMapper.map(request, instance);
        //用byte数组处理返回结果，因为返回结果可能是字符串也可能是数据流
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
        responseMapper.map(responseEntity, response);
    }

    /**
     * 写回字符串结果到客户端
     *
     * @param response
     * @param string
     */
    public void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            log.error("回字异常:",e);
        }
    }
}
