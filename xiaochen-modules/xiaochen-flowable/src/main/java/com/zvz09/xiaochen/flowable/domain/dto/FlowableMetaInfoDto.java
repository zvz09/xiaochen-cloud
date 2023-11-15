package com.zvz09.xiaochen.flowable.domain.dto;

import lombok.Data;

/**
 * @author zvz09
 */
@Data
public class FlowableMetaInfoDto {

    /**
     * 创建者（username）
     */
    private String createUser;

    /**
     * 流程描述
     */
    private String description;
    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单编号
     */
    private Long formId;

}
