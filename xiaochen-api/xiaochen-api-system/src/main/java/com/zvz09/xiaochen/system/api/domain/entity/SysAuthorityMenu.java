package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_authority_menu")
@Schema(name = "SysAuthorityMenu", description = "")
public class SysAuthorityMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Long sysBaseMenuId;

    @Schema(description = "角色ID")
    private Long sysAuthorityId;
}
