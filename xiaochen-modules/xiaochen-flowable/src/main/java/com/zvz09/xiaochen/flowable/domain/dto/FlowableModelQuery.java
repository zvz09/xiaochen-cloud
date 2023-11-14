package com.zvz09.xiaochen.flowable.domain.dto;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
public class FlowableModelQuery extends BasePage {
    /**
     * 模型名称
     */
    @Schema(description = "模型名称")
    private String modelName;
    /**
     * 模型Key
     */
    @Schema(description = "模型Key")
    private String modelKey;
    /**
     * 流程分类
     */
    @Schema(description = "流程分类")
    private String category;

}
