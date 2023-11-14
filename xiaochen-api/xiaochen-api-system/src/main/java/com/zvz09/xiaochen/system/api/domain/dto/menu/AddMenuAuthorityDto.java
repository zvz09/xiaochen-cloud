/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.menu
 * @className com.zvz09.xiaochen.system.dto.menu.AddMenuAuthorityInfo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * AddMenuAuthorityInfo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/5 17:41
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AddMenuAuthorityDto")
public class AddMenuAuthorityDto {

    @Schema(description = "菜单集合Id")
    @NotEmpty(message = "菜单集合不能为空")
    private List<Long> menuIds;

    @Schema(description = "角色Id")
    @NotNull(message = "角色不能为空")
    private Long authorityId;
}
 