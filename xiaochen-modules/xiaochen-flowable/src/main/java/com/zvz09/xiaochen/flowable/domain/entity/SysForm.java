package com.zvz09.xiaochen.flowable.domain.entity;

import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 流程表单对象 sys_task_form
 *
 * @author Tony
 * @date 2021-03-30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SysForm extends BaseEntity {
    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单内容
     */
    private String formContent;

    /**
     * 缩略图
     */
    private String thumbnail;
}
