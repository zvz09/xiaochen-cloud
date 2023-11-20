/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.vo
 * @className com.zvz09.xiaochen.auth.vo.SysUserVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import com.zvz09.xiaochen.system.api.domain.entity.SysPosition;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * SysUserVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/30 15:04
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysUserVo", description = "")
public class SysUserVo extends BaseVo {

    @Schema(description = "用户登录名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickName;

    private String sideMode;

    private String headerImg;

    private String baseColor;

    private String activeColor;

    private List<SysRoleVo> authorities;

    private String phone;

    private String email;

    private Long enable;

    private List<SysDepartment> sysDepartments;
    private List<SysPosition> sysPositions;

    public SysUserVo(Long id, String nickName) {
        super(id);
        this.nickName = nickName;
    }

    public SysUserVo(@NotNull SysUser sysUser) {
        super(sysUser.getId());
        this.username = sysUser.getUsername();
        this.nickName = sysUser.getNickName();
        this.sideMode = sysUser.getSideMode();
        this.headerImg = sysUser.getHeaderImg();
        this.baseColor = sysUser.getBaseColor();
        this.activeColor = sysUser.getActiveColor();
        this.phone = sysUser.getPhone();
        this.email = sysUser.getEmail();
        this.enable = sysUser.getEnable();
    }

    public SysUserVo(@NotNull SysUser sysUser, List<SysRole> sysAuthorities) {
        super(sysUser.getId());
        this.username = sysUser.getUsername();
        this.nickName = sysUser.getNickName();
        this.sideMode = sysUser.getSideMode();
        this.headerImg = sysUser.getHeaderImg();
        this.baseColor = sysUser.getBaseColor();
        this.activeColor = sysUser.getActiveColor();
        this.phone = sysUser.getPhone();
        this.email = sysUser.getEmail();
        this.enable = sysUser.getEnable();


        if (sysAuthorities == null) {
            this.authorities = null;
        } else {
            List<SysRoleVo> authorities = new ArrayList<>();
            sysAuthorities.forEach(role -> {
                authorities.add(new SysRoleVo(role));
            });
            this.authorities = authorities;
        }
    }
}
 