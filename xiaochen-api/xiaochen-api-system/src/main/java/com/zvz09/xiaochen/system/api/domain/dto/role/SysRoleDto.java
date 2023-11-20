/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto
 * @className com.zvz09.xiaochen.system.dto.CreateRoleBody
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.role;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CreateRoleBody
 *
 * @author zvz09
 * @version 1.0
 * @description 创建角色请求体
 * @date 2023/8/31 15:04
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysRoleDto")
public class SysRoleDto extends BaseDto {

    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 转化为po对象
     *
     * @return SysApi
     */
    public SysRole convertedToPo() {
        return SysRole.builder().id(this.getId())
                .roleCode(this.roleCode)
                .roleName(this.roleName)
                .build();
    }
}
 