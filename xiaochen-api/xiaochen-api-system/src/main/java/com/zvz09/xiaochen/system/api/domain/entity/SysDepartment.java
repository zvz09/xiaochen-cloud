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
 * 部门管理 实体类
 *
 * @author zvz09
 * @date 2023-10-10 11:49:52
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_department")
@Schema(name = "SysDepartment", description = "部门管理")
public class SysDepartment extends BaseEntity {

    @Schema(description = "父部门id")
    private Long parentId;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "显示顺序")
    private Integer orderNum;
    @Schema(description = "负责人")
    private String leader;
    @Schema(description = "联系电话")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "部门状态0正常1停用")
    private Boolean status;
}
