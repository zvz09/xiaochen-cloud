package com.zvz09.xiaochen.gateway.filter;

import com.zvz09.xiaochen.common.core.constant.LoginConstant;
import com.zvz09.xiaochen.common.core.constant.SecurityConstants;
import com.zvz09.xiaochen.common.core.util.Snowflake;
import com.zvz09.xiaochen.common.jwt.JwtUtils;
import com.zvz09.xiaochen.gateway.config.properties.IgnoreWhiteProperties;
import com.zvz09.xiaochen.gateway.utils.IpUtil;
import com.zvz09.xiaochen.gateway.utils.ServletUtils;
import com.zvz09.xiaochen.gateway.utils.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.zvz09.xiaochen.common.core.constant.CommonConstant.TRACE_ID;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 网关鉴权
 *
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final Enforcer enforcer;
    private final IgnoreWhiteProperties ignoreWhiteProperties;
    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        ServerHttpResponse response = exchange.getResponse();

        try {
            String traceId = Snowflake.getSnowflakeId();
            MDC.put(TRACE_ID, "g-"+traceId);
            response.getHeaders().add(TRACE_ID,traceId);
        } catch (Exception e) {
            log.error("logRequest error", e);
        }
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
        String remoteAddr = IpUtil.getRemoteIpAddress(request);
        Route route = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhiteProperties.getWhites())) {
            return chain.filter(exchange.mutate().request(mutate.build()).response(response).build());
        }

        // 操作对象
        String obj = requestUrl.getPath();
        // 获取请求方法
        String act = request.getMethod().name();
        log.info("用户ip:{},访问路径:{},请求类型：{},后端服务名:{}",remoteAddr, requestUrl.getPath(), act, route.getId());

        // 获取请求头中token
        String token = exchange.getRequest().getHeaders().getFirst(LoginConstant.TOKEN_NAME);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        if (token != null && Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(LoginConstant.JWT_BLACK_LIST, token))) {
            return unauthorizedResponse(exchange, "令牌已失效");
        }

        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        String roleId = JwtUtils.getRoleId(claims);
        String roleCode = JwtUtils.getRoleCode(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        if (!enforcer.enforce(roleCode, obj, act)) {
            return unauthorizedResponse(exchange, "用户权限不足");
        }

        addHeader(mutate, TRACE_ID, MDC.get(TRACE_ID));
        // 设置用户信息到请求
        addHeader(mutate,SecurityConstants.REMOTE_IP,remoteAddr);
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        addHeader(mutate, SecurityConstants.DETAILS_AUTHORITY_ID, roleId);
        addHeader(mutate, SecurityConstants.DETAILS_AUTHORITY_CODE, roleCode);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).response(response).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
    }


    @Override
    public int getOrder() {
        return 10001;  //这里要在 RouteToRequestUrlFilter 之后执行，才可以获得路由之后的路径
    }

}