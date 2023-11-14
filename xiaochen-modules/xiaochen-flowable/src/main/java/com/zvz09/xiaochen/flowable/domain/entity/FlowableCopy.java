package com.zvz09.xiaochen.flowable.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 流程抄送 实体类
 *
 * @author zvz09
 * @date 2023-10-24 15:52:06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("flowable_copy")
@Schema(name = "FlowableCopy", description = "流程抄送")
public class FlowableCopy extends BaseEntity {

    @Schema(description = "抄送标题")
    private String title;
    @Schema(description = "流程主键")
    private String processId;
    @Schema(description = "流程名称")
    private String processName;
    @Schema(description = "流程分类主键")
    private String categoryId;
    @Schema(description = "部署主键")
    private String deploymentId;
    @Schema(description = "流程实例主键")
    private String instanceId;
    @Schema(description = "任务主键")
    private String taskId;
    @Schema(description = "用户主键")
    private Long userId;
    @Schema(description = "发起人主键")
    private Long originatorId;
    @Schema(description = "发起人名称")
    private String originatorName;
}
