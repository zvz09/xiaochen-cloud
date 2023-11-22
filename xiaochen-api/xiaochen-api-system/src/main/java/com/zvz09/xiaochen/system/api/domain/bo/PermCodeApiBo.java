package com.zvz09.xiaochen.system.api.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lizili-YF0033
 */
@Data
public class PermCodeApiBo {

    private Long permCodeId;
    private Long apiId;

    @Schema(description = "权限字标识(一般为有含义的英文字符串)")
    private String permCode;

    @Schema(description = "显示名称")
    private String showName;

    private String serviceName;

    private String path;

    private String method;
}
