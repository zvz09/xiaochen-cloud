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
 * 岗位管理 实体类
 *
 * @author zvz09
 * @date 2023-10-10 13:50:31
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_position")
@Schema(name = "SysPosition", description = "岗位管理")
public class SysPosition extends BaseEntity {

    @Schema(description = "岗位编码")
    private String positionCode;
    @Schema(description = "岗位名称")
    private String positionName;
    @Schema(description = "显示顺序")
    private Integer positionSort;
    @Schema(description = "状态")
    private Boolean status;
    @Schema(description = "备注")
    private String remark;
}
