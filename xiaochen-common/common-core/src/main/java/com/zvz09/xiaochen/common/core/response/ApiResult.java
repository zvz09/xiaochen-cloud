package com.zvz09.xiaochen.common.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * REST API 返回结果
 *
 * @author zvz09
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@Schema(description = "响应结果")
public class ApiResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7594052194764993562L;

    @Schema(description = "响应编码 200：成功，500：失败")
    private int code;

    @Schema(description = "响应结果 true：成功，false：失败")
    private boolean success;

    @Schema(description = "响应消息")
    private String msg;

    @Schema(description = "响应结果数据")
    private T data;

    public ApiResult() {
    }

    public static ApiResult<String> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T data) {
        return result(ApiCode.SUCCESS, data);
    }

    public static ApiResult<String> fail(String message) {
        return fail(ApiCode.FAIL, message);
    }

    public static ApiResult<String> fail(ApiCode apiCode) {
        return fail(apiCode, null);
    }

    public static ApiResult<String> fail(ApiCode apiCode, String message) {
        if (ApiCode.SUCCESS == apiCode) {
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode, StringUtils.isBlank(message) ? apiCode.getMsg() : message, null);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, T data) {
        return result(apiCode, null, data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, String message, T data) {
        if (apiCode == null) {
            throw new RuntimeException("结果状态码不能为空");
        }
        boolean success = false;
        int code = apiCode.getCode();
        if (ApiCode.SUCCESS.getCode() == code) {
            success = true;
        }
        String outMessage;
        if (StringUtils.isBlank(message)) {
            outMessage = apiCode.getMsg();
        } else {
            outMessage = message;
        }
        return ApiResult.<T>builder()
                .code(code)
                .msg(outMessage)
                .data(data)
                .success(success)
                .build();
    }

}
