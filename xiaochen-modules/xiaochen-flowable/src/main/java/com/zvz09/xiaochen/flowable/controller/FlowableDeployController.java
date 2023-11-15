package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/flowable/deploy")
public class FlowableDeployController {

    private final IFlowableDeployService flowableDeployService;
    private final ISysDeployFormService sysDeployFormService;

    /**
     * 查询流程部署列表
     */

    @GetMapping("/list")
    @Operation(summary = "查询流程部署列表")
    public ApiResult<Page<FlowableDeployVo>> list(ProcessQuery processQuery) {
        return ApiResult.success(flowableDeployService.queryPageList(processQuery));
    }

    /**
     * 查询流程部署版本列表
     */
    @GetMapping("/publishList")
    @Operation(summary = "查询流程部署版本列表")
    public ApiResult<Page<FlowableDeployVo>> publishList(@RequestParam String processKey, BasePage basePage) {
        return ApiResult.success(flowableDeployService.queryPublishList(processKey, basePage));
    }

    /**
     * 激活或挂起流程
     *
     * @param state        状态（active:激活 suspended:挂起）
     * @param definitionId 流程定义ID
     */
    @PutMapping(value = "/changeState")
    @Operation(summary = "激活或挂起流程")
    public ApiResult<Void> changeState(@RequestParam String state, @RequestParam String definitionId) {
        flowableDeployService.updateState(definitionId, state);
        return ApiResult.success();
    }

    /**
     * 读取xml文件
     *
     * @param definitionId 流程定义ID
     * @return
     */
    @GetMapping("/bpmnXml")
    @Operation(summary = "读取xml文件")
    public ApiResult<String> getBpmnXml(String definitionId) {
        return ApiResult.success(flowableDeployService.queryBpmnXmlById(definitionId));
    }

    /**
     * 删除流程部署
     *
     * @param deployIds 流程部署ids
     */
    @DeleteMapping()
    @Operation(summary = "删除流程部署")
    public ApiResult<String> remove(@RequestBody List<String> deployIds) {
        flowableDeployService.deleteByIds(deployIds);
        return ApiResult.success();
    }

    /**
     * 查询流程部署关联表单信息
     *
     * @param deployId 流程部署id
     */
    @GetMapping("/form")
    @Operation(summary = "查询流程部署关联表单信息")
    public ApiResult<String> deployFormInfo(String deployId) {
        SysDeployForm sysDeployForm = sysDeployFormService.selectDeployFormByDeployId(deployId);
        if (Objects.isNull(sysDeployForm)) {
            return ApiResult.fail("请先配置流程表单");
        }
        return ApiResult.success(sysDeployForm.getContent());
    }
}
