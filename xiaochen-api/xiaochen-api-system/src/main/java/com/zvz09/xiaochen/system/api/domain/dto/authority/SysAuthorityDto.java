/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto
 * @className com.zvz09.xiaochen.system.dto.CreateAuthorityBody
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.authority;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CreateAuthorityBody
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
@Schema(name = "SysAuthorityDto")
public class SysAuthorityDto extends BaseDto {

    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String authorityCode;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String authorityName;

    @Schema(description = "父角色ID")
    @NotNull(message = "父角色ID不能为空")
    private Long parentId;

    /**
     * 转化为po对象
     *
     * @return SysApi
     */
    public SysAuthority convertedToPo() {
        return SysAuthority.builder().id(this.getId())
                .authorityCode(this.authorityCode)
                .authorityName(this.authorityName)
                .parentId(this.parentId).build();
    }
}
 