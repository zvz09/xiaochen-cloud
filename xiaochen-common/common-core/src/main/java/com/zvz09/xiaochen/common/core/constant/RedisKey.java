package com.zvz09.xiaochen.common.core.constant;

/**
 * @author geekidea
 * @date 2022/7/9
 **/
public interface RedisKey {

    /**
     * 项目redis前缀
     */
    String PROJECT = "xc:";

    /**
     * 登录token
     */
    String LOGIN_TOKEN = PROJECT + "login:token:%s";

    /**
     * 登录用户ID
     */
    String LOGIN_USER_ID = PROJECT + "login:user-id:%s";

    /**
     * 登录用户token
     */
    String LOGIN_USER = PROJECT + "login:user:%s:%s";

}
