/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.vo
 * @className com.zvz09.xiaochen.casbin.vo.CasbinVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CasbinVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/11 14:56
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CasbinVo")
public class CasbinVo {

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "请求方式")
    private String method;
}
 