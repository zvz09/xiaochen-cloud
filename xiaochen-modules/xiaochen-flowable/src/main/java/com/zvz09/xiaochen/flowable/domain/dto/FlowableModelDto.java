package com.zvz09.xiaochen.flowable.domain.dto;

import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlowableModelDto {
    /**
     * 模型主键
     */
    @NotNull(message = "模型主键不能为空", groups = {UpdateValidation.class})
    private String modelId;
    /**
     * 模型名称
     */
    @NotNull(message = "模型名称不能为空")
    private String modelName;
    /**
     * 模型Key
     */
    @NotNull(message = "模型Key不能为空")
    private String modelKey;
    /**
     * 流程分类
     */
    @NotBlank(message = "流程分类不能为空")
    private String category;
    /**
     * 描述
     */
    private String description;
    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单主键
     */
    private Long formId;
    /**
     * 流程xml
     */
    private String bpmnXml;
    /**
     * 是否保存为新版本
     */
    private Boolean newVersion;

}
