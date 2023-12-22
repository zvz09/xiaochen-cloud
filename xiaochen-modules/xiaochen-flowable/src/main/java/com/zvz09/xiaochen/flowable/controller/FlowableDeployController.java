package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.annotation.BizNo;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;
import com.zvz09.xiaochen.flowable.service.IFlowableDeployService;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author zvz09
 */
@Slf4j
@Tag(name = "流程部署")
@RestController
@RequiredArgsConstructor
@RequestMapping("/deploy")
public class FlowableDeployController {

    private final IFlowableDeployService flowableDeployService;
    private final ISysDeployFormService sysDeployFormService;

    /**
     * 查询流程部署列表
     */

    @PostMapping("/page")
    @Operation(summary = "查询流程部署列表")
    @BizNo(spEl = "{#processQuery.processName}")
    public ApiResult<Page<FlowableDeployVo>> list(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableDeployService.queryPageList(processQuery));
    }

    /**
     * 查询流程部署版本列表
     */
    @PostMapping("/{processKey}/page")
    @Operation(summary = "查询流程部署版本列表")
    public ApiResult<Page<FlowableDeployVo>> publishList(@PathVariable(value = "processKey") String processKey, @RequestBody BasePage basePage) {
        return ApiResult.success(flowableDeployService.queryPublishList(processKey, basePage));
    }

    /**
     * 激活或挂起流程
     *
     * @param state        状态（active:激活 suspended:挂起）
     * @param definitionId 流程定义ID
     */
    @PutMapping(value = "/{definitionId}/{state}")
    @Operation(summary = "激活或挂起流程")
    @BizNo(spEl = "{#definitionId}")
    public ApiResult<String> changeState(@PathVariable(value = "definitionId") String definitionId,
                                       @PathVariable(value = "state") String state) {
        flowableDeployService.updateState(definitionId, state);
        return ApiResult.success();
    }

    /**
     * 读取xml文件
     *
     * @param definitionId 流程定义ID
     * @return
     */
    @GetMapping("/{definitionId}/bpmnXml")
    @Operation(summary = "读取xml文件")
    public ApiResult<String> getBpmnXml(@PathVariable(value = "definitionId") String definitionId) {
        return ApiResult.success(flowableDeployService.queryBpmnXmlById(definitionId));
    }

    /**
     * 删除流程部署
     *
     * @param deployIds 流程部署ids
     */
    @DeleteMapping()
    @Operation(summary = "删除流程部署")
    @BizNo(spEl = "{#deployIds}")
    public ApiResult<String> remove(@RequestBody List<String> deployIds) {
        flowableDeployService.deleteByIds(deployIds);
        return ApiResult.success();
    }

    /**
     * 查询流程部署关联表单信息
     *
     * @param deployId 流程部署id
     */
    @GetMapping("/{deployId}/form")
    @Operation(summary = "查询流程部署关联表单信息")
    @BizNo(spEl = "{#deployId}")
    public ApiResult<String> deployFormInfo(@PathVariable(value = "deployId") String deployId) {
        SysDeployForm sysDeployForm = sysDeployFormService.selectDeployFormByDeployId(deployId);
        if (Objects.isNull(sysDeployForm)) {
            return ApiResult.fail("请先配置流程表单");
        }
        return ApiResult.success(sysDeployForm.getContent());
    }
}
