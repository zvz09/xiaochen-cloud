package com.zvz09.xiaochen.flowable.domain.dto;

import com.zvz09.xiaochen.common.core.page.BasePage;
import lombok.Data;

/**
 * @author zvz09
 */
@Data
public class FlowableCopyQuery extends BasePage {

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 发起人名称
     */
    private String originatorName;
}
