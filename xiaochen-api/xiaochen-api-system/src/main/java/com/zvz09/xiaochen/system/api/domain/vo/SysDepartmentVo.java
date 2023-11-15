package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author zvz09
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysDepartmentVo")
public class SysDepartmentVo extends BaseVo {
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

    @Schema(description = "子部門")
    private List<SysDepartmentVo> children;

    public SysDepartmentVo(SysDepartment sysDepartment) {
        super(sysDepartment.getId());
        this.parentId = sysDepartment.getParentId();
        this.deptName = sysDepartment.getDeptName();
        this.orderNum = sysDepartment.getOrderNum();
        this.leader = sysDepartment.getLeader();
        this.phone = sysDepartment.getPhone();
        this.email = sysDepartment.getEmail();
        this.status = sysDepartment.getStatus();
    }
}
