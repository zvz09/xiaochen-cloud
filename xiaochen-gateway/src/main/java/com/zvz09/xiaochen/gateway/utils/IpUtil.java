package com.zvz09.xiaochen.gateway.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Ip工具类。
 *
 */
@Slf4j
public class IpUtil {

    private static final String UNKNOWN = "unknown";


    /**
     * 通过Reactive的ServerHttpRequest对象获取Ip地址。
     *
     * @param request ServerHttpRequest对象。
     * @return 本次请求的Ip地址。
     */
    public static String getRemoteIpAddress(ServerHttpRequest request) {
        String ip = null;
        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeaders().getFirst("X-Forwarded-For");
        if (StrUtil.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeaders().getFirst("X-Real-IP");
        }
        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null) {
            ip = ipAddresses.split(",")[0];
        }
        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (StrUtil.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            InetSocketAddress address = request.getRemoteAddress();
            if (address == null) {
                return ip;
            }
            InetAddress a = address.getAddress();
            if (a == null) {
                return ip;
            }
            ip = a.getHostAddress();
        }
        return ip;
    }

    public static String getFirstLocalIpAddress() {
        String ip;
        try {
            List<String> ipList = getHostAddress();
            // default the first
            ip = (!ipList.isEmpty()) ? ipList.get(0) : "";
        } catch (Exception ex) {
            ip = "";
            log.error("Failed to call ", ex);
        }
        return ip;
    }

    private static List<String> getHostAddress() throws SocketException {
        List<String> ipList = new ArrayList<>(5);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> allAddress = ni.getInetAddresses();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                // skip the IPv6 addr
                // skip the IPv6 addr
                if (address.isLoopbackAddress() || address instanceof Inet6Address) {
                    continue;
                }
                String hostAddress = address.getHostAddress();
                ipList.add(hostAddress);
            }
        }
        return ipList;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private IpUtil() {
    }
}
