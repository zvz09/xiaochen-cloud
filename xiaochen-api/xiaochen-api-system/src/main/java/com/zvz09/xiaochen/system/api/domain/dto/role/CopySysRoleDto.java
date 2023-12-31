/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto
 * @className com.zvz09.xiaochen.system.dto.CreateRoleBody
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CreateRoleBody
 *
 * @author zvz09
 * @version 1.0
 * @description 拷贝角色请求体
 * @date 2023/8/31 15:04
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CopySysRoleDto")
public class CopySysRoleDto {

    @Valid
    @Schema(description = "角色信息")
    @NotNull(message = "角色信息不能为空")
    private SysRoleDto role;

    @Schema(description = "旧角色ID")
    @NotNull(message = "旧角色ID不能为空")
    private Long oldRoleId;

}
 