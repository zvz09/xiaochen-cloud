package com.zvz09.xiaochen.common.core.response;


import lombok.Getter;

/**
 * @author zvz09
 */

@Getter
public enum ApiCode {

    /**
     * 操作成功
     **/
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     **/
    FAIL(500, "操作失败"),

    INVALID_ARGUMENT_FORMAT(1001, "数据验证失败，不合法的参数格式，请核对！"),

    /**
     * token已过期
     */
    TOKEN_EXCEPTION(5001, "Token为空或已过期，请重新登录");

    private final int code;
    private final String msg;

    ApiCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiCode getApiCode(int code) {
        ApiCode[] apiCodes = ApiCode.values();
        for (ApiCode apiCode : apiCodes) {
            if (apiCode.getCode() == code) {
                return apiCode;
            }
        }
        return FAIL;
    }

}
