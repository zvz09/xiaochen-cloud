package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色与系统权限字对应关系表
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_perm_code")
@Schema(description = "角色与系统权限字对应关系表")
public class SysRolePermCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色Id")
    private Long roleId;

    @Schema(description = "权限字Id")
    private Long permCodeId;

}
