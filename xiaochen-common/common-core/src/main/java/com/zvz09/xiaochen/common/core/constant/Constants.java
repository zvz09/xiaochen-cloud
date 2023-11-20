package com.zvz09.xiaochen.common.core.constant;

/**
 * 通用常量信息
 *
 * @author zvz09
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";


    public static final String[] EXCLUDE_PATH_PATTERNS = {"/favicon.ico", "/error",
            "/doc.html", "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**",
            "/swagger-ui.html/**", "/swagger-ui/**"};

    public static final String FEIGN_PATH_PREFIX = "/feign";

    public static final String SUPER_ADMIN = "888";
}
