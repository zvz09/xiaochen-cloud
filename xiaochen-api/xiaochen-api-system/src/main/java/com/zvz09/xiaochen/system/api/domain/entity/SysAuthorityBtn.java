package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Getter
@Setter
@TableName("sys_authority_btn")
@Schema(name = "SysAuthorityBtn", description = "")
public class SysAuthorityBtn implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long authorityId;

    @Schema(description = "菜单ID")
    private Long sysMenuId;

    @Schema(description = "菜单按钮ID")
    private Long sysBaseMenuBtnId;
}
