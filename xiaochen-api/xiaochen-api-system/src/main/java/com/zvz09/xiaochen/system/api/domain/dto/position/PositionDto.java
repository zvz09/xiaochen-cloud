package com.zvz09.xiaochen.system.api.domain.dto.position;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位管理 DTO
 *
 * @author zvz09
 * @date 2023-10-10 13:50:31
 */
@Getter
@Setter
public class PositionDto extends BaseDto<SysPosition> {

    @Schema(description = "岗位编码")
    @NotBlank(message = "岗位编码不能为空")
    private String positionCode;
    @Schema(description = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    private String positionName;
    @Schema(description = "显示顺序")
    private Integer positionSort;
    @Schema(description = "状态0正常1停用")
    @NotBlank(message = "状态0正常1停用不能为空")
    private Boolean status;
    @Schema(description = "备注")
    private String remark;

    public SysPosition convertedToPo() {
        return SysPosition.builder().id(this.getId())
                .positionCode(this.positionCode)
                .positionName(this.positionName)
                .positionSort(this.positionSort)
                .status(this.status)
                .remark(this.remark)
                .build();
    }
}
