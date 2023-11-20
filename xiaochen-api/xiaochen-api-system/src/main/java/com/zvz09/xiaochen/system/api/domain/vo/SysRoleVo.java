/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.vo
 * @className com.zvz09.xiaochen.system.vo.SysRoleVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SysRoleVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/31 14:39
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysRoleVo")
public class SysRoleVo extends BaseVo {

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名")
    private String roleName;


    public SysRoleVo(SysRole sysRole) {
        super(sysRole.getId());
        this.roleCode = sysRole.getRoleCode();
        this.roleName = sysRole.getRoleName();
    }
}
 