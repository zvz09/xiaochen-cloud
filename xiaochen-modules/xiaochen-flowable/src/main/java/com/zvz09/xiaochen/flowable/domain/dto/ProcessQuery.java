package com.zvz09.xiaochen.flowable.domain.dto;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProcessQuery extends BasePage {

    @Schema(description = "流程标识")
    private String processKey;


    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程分类")
    private String category;


    @Schema(description = "状态")
    private String state;


    @Schema(description = "请求参数")
    private Map<String, Object> params = new HashMap<>();
}
