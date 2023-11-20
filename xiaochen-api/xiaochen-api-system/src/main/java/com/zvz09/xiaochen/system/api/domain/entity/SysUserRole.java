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
@TableName("sys_user_role")
@Schema(name = "SysUserRole", description = "")
public class SysUserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sysUserId;

    private String sysRoleCode;
}
