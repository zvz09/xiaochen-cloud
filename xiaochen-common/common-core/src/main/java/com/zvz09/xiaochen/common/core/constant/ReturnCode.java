/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.annotation
 * @className com.zvz09.xiaochen.common.core.constant.ReturnCode
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.core.constant;

/**
 * ReturnCode
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/25 16:35
 */
public enum ReturnCode {
    /**
     * 未提供token
     */
    NOT_TOKEN("10001", "未提供token"),
    /**
     * token无效
     */
    INVALID_TOKEN("10002", "token无效"),
    /**
     * token已过期
     */
    TOKEN_TIMEOUT("10003", "token已过期"),
    /**
     * token已被顶下线
     */
    BE_REPLACED("10004", "token已被顶下线"),
    /**
     * token已被踢下线
     */
    KICK_OUT("10005", "token已被踢下线"),
    /**
     * 当前会话未登录
     */
    NOT_NOT_TOKEN("10006", "当前会话未登录"),
    /**
     * 应用名已存在
     */
    APPNAME_IS_EXIST("20001", "应用名已存在"),
    /**
     * 应用不存在
     */
    APPNAME_IS_NOT_EXIST("20002", "应用不存在"),
    /**
     * user is not exist
     */
    USER_IS_NOT_EXIST("11003", "user is not exist"),
    /**
     * success
     */
    SUCCESS("00000", "success"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR("50000", "系统错误"),
    /**
     * 参数错误
     */
    PARAM_ERROR("44444", "参数错误");

    private final String code;
    private final String message;

    ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
 