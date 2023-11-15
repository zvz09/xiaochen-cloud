package com.zvz09.xiaochen.flowable.domain.vo;

import lombok.Data;

/**
 * @author zvz09
 */
@Data
public class SysFormVo {

    /**
     * 表单名称
     */
    private String formName;
    /**
     * 表单模板 jsonString
     */
    private String formTemplate;
    /**
     * 表单填充数据 jsonString
     */
    private String models;
}
