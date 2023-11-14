package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 用户与岗位关联表 实体类
 *
 * @author Default Author
 * @date 2023-10-11 15:45:35
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_user_position")
@Schema(name = "SysUserPosition", description = "用户与岗位关联表")
public class SysUserPosition {

    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "岗位ID")
    private Long positionId;
}

