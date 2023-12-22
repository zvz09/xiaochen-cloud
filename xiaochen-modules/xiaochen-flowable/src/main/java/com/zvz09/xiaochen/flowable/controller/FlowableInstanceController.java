package com.zvz09.xiaochen.flowable.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.flowable.service.IFlowableInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zvz09
 */
@Slf4j
@Tag(name = "工作流流程实例管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/instance")
public class FlowableInstanceController {

    private final IFlowableInstanceService flowableInstanceService;

    /**
     * 查询流程实例详情信息
     *
     * @param procInsId 流程实例ID
     * @param deployId  流程部署ID
     */
    @GetMapping("/{procInsId}/{deployId}/detail")
    @Operation(summary = "查询流程实例详情信息")
    public ApiResult<Map<String, Object>> detail(@PathVariable(value = "procInsId") String procInsId,
                                                 @PathVariable(value = "deployId") String deployId) {
        return ApiResult.success(flowableInstanceService.queryDetailProcess(procInsId, deployId));
    }
}
