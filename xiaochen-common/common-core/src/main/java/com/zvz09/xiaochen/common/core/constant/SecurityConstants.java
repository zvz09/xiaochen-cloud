package com.zvz09.xiaochen.common.core.constant;

/**
 * 权限相关通用常量
 *
 * @author zvz09
 */
public class SecurityConstants {
    /**
     * 用户ID字段
     */
    public static final String REMOTE_IP = "X-Real-IP";
    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 当前角色id
     */
    public static final String DETAILS_AUTHORITY_ID = "role_id";

    /**
     * 当前角色code
     */
    public static final String DETAILS_AUTHORITY_CODE = "role_code";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /**
     * 请求来源
     */
    public static final String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    public static final String INNER = "inner";

    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";

    /**
     * 角色权限
     */
    public static final String ROLE_PERMISSION = "role_permission";
}
