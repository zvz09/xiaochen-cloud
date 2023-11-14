package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@Schema(name = "SysUser", description = "")
public class SysUser extends BaseEntity {

    @Schema(description = "用户UUID")
    private String uuid;

    @Schema(description = "用户登录名")
    private String username;

    @Schema(description = "用户登录密码")
    private String password;

    @Schema(description = "用户ni'ch")
    private String nickName;

    private String sideMode;

    private String headerImg;

    private String baseColor;

    private String activeColor;

    private Long authorityId;

    private String phone;

    private String email;

    private Long enable;
}
