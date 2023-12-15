package com.zvz09.xiaochen.system.api.domain.dto.dept;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 部门管理 DTO
 *
 * @author zvz09
 * @date 2023-10-10 11:49:52
 */
@Getter
@Setter
public class DepartmentDto extends BaseDto<SysDepartment> {

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "负责人")
    @NotBlank(message = "负责人不能为空")
    private String leader;

    @Schema(description = "联系电话")
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "部门状态0正常1停用")
    @NotNull(message = "部门状态不能为空")
    private Boolean status;

    public SysDepartment convertedToPo() {
        return SysDepartment.builder().id(this.getId())
                .parentId(this.parentId)
                .deptName(this.deptName)
                .orderNum(this.orderNum)
                .leader(this.leader)
                .phone(this.phone)
                .email(this.email)
                .status(this.status)
                .build();
    }
}
