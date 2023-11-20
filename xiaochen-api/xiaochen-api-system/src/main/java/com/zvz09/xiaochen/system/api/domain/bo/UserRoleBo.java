package com.zvz09.xiaochen.system.api.domain.bo;

import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户角色信息聚合
 *
 * @author lizili-YF0033
 */
@Data
public class UserRoleBo {

    @Schema(description = "用户Id")
    private Long userId;

    @Schema(description = "角色Id")
    private Long roleId;

    @Schema(description = "用户登录名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickName;

    private String sideMode;

    private String headerImg;

    private String baseColor;

    private String activeColor;

    private String phone;

    private String email;

    private Long enable;

    private String roleCode;

    private String roleName;

    public SysUser convertedToUser() {
        return SysUser.builder()
                .id(this.userId)
                .username(this.username)
                .nickName(this.nickName)
                .sideMode(this.sideMode)
                .headerImg(this.headerImg)
                .baseColor(this.baseColor)
                .activeColor(this.activeColor)
                .phone(this.phone)
                .email(this.email)
                .enable(this.enable)
                .build();
    }

    public SysRole convertedToRole() {
        return SysRole.builder()
                .id(this.roleId)
                .roleCode(this.roleCode)
                .roleName(this.roleName)
                .build();
    }
}
