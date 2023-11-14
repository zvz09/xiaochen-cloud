package com.zvz09.xiaochen.flowable.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lizili-YF0033
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowableDeployVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程定义ID
     */
    @Schema(description = "流程定义ID")
    private String definitionId;

    /**
     * 流程名称
     */
    @Schema(description = "流程名称")
    private String processName;

    /**
     * 流程Key
     */
    @Schema(description = "流程Key")
    private String processKey;

    /**
     * 分类编码
     */
    @Schema(description = "分类编码")
    private String category;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 表单ID
     */
    @Schema(description = "表单ID")
    private Long formId;

    /**
     * 表单名称
     */
    @Schema(description = "表单名称")
    private String formName;

    /**
     * 部署ID
     */
    @Schema(description = "部署ID")
    private String deploymentId;

    /**
     * 流程定义状态: 1:激活 , 2:中止
     */
    @Schema(description = "流程定义状态: 1:激活 , 2:中止")
    private Boolean suspended;

    /**
     * 部署时间
     */
    @Schema(description = "部署时间")
    private Date deploymentTime;
}
