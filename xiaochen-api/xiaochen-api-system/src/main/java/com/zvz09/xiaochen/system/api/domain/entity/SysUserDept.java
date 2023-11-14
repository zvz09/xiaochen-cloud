package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 用户与部门关联 实体类
 *
 * @author zvz09
 * @date 2023-10-11 15:42:50
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_user_dept")
@Schema(name = "SysUserDept", description = "用户与部门关联")
public class SysUserDept {

    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "部门ID")
    private Long deptId;
}
