package com.zvz09.xiaochen.flowable.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "FlowableModelVo")
public class FlowableModelVo {
    /**
     * 模型ID
     */
    private String modelId;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型Key
     */
    private String modelKey;
    /**
     * 分类编码
     */
    private String category;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单ID
     */
    private Long formId;
    /**
     * 模型描述
     */
    private String description;
    /**
     * 创建时间
     */

    private Date createTime;
    /**
     * 流程xml
     */
    private String bpmnXml;
    /**
     * 表单内容
     */
    private String content;

}
