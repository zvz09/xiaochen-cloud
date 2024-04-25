package com.zvz09.xiaochen.k8s.manage.filter;

import com.zvz09.xiaochen.k8s.manage.domain.vo.RouteInstance;
import com.zvz09.xiaochen.k8s.manage.service.RoutingDelegateService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.zvz09.xiaochen.k8s.manage.constant.Constant.PROXY_PREFIX;

/**
 * @author Administrator
 */
@Slf4j
@WebFilter(urlPatterns = PROXY_PREFIX)
@Component
@RequiredArgsConstructor
public class ForwardRoutingFilter extends OncePerRequestFilter implements Ordered {

    private final ProxyRoutes proxyRoutes;
    private final RoutingDelegateService routingDelegateService;
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("ForwardRoutingFilter doFilterInternal,request uri: {}", request.getRequestURI());
        String currentURL = StringUtils.isEmpty(request.getContextPath()) ? request.getRequestURI() :
                StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
        AntPathMatcher matcher = new AntPathMatcher();
        RouteInstance instance = proxyRoutes.getRoutes().stream().filter(i -> matcher.match(i.getPath(), currentURL)).findFirst().orElse(new RouteInstance());

        if (instance.getUrl() == null) {
            //转发的uri为空，不进行代理转发，交由过滤器链后续过滤器处理
            filterChain.doFilter(request, response);
        } else {
            // 创建一个service 去处理代理转发逻辑
            routingDelegateService.doForward(instance, request, response);
            return;
        }

    }
}
