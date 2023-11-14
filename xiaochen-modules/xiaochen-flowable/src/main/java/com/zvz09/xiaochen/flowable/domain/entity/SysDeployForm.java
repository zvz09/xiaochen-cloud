package com.zvz09.xiaochen.flowable.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 流程实例关联表单对象 sys_instance_form
 *
 * @author Tony
 * @date 2021-03-30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SysDeployForm {

    /**
     * 表单主键
     */
    private Long formId;

    /**
     * 流程定义主键
     */
    private String deployId;

    /**
     * 表单Key
     */
    private String formKey;
    /**
     * 节点Key
     */
    private String nodeKey;
    /**
     * 表单名称
     */
    private String formName;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 表单内容
     */
    private String content;
}
