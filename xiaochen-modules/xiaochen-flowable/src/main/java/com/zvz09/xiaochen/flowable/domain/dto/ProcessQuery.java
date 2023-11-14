package com.zvz09.xiaochen.flowable.domain.dto;

import com.zvz09.xiaochen.common.core.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProcessQuery extends BasePage {
    /**
     * 流程标识
     */
    private String processKey;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程分类
     */
    private String category;

    /**
     * 状态
     */
    private String state;

    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();
}
