/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.vo
 * @className com.zvz09.xiaochen.system.vo.SysAuthorityVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SysAuthorityVo
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
@Schema(name = "SysAuthorityVo")
public class SysAuthorityVo extends BaseVo {

    @Schema(description = "角色编码")
    private String authorityCode;

    @Schema(description = "角色名")
    private String authorityName;

    @Schema(description = "默认菜单")
    private String defaultRouter;

    @Schema(description = "子角色")
    private List<SysAuthorityVo> children;

    public SysAuthorityVo(SysAuthority sysAuthority) {
        super(sysAuthority.getId());
        this.authorityCode = sysAuthority.getAuthorityCode();
        this.authorityName = sysAuthority.getAuthorityName();
        this.defaultRouter = sysAuthority.getDefaultRouter();
    }
}
 