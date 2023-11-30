package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelDto;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelQuery;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableModelVo;
import com.zvz09.xiaochen.flowable.domain.vo.ListenerVo;
import com.zvz09.xiaochen.flowable.service.IFlowableModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zvz09
 */
@Slf4j
@Tag(name = "模型定义")
@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/model")
public class FlowableModelController {

    private final IFlowableModelService flowableModelService;

    /**
     * 查询流程模型列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询流程模型列表")
    public ApiResult<Page<FlowableModelVo>> page(@RequestBody FlowableModelQuery flowableModelQuery) {
        return ApiResult.success(flowableModelService.list(flowableModelQuery));
    }

    /**
     * 查询流程模型列表
     */
    @PostMapping("/{modelKey}/historyList")
    @Operation(summary = "查询流程模型列表")
    public ApiResult<Page<FlowableModelVo>> historyList(@PathVariable(value = "modelKey") String modelKey, @RequestBody BasePage basePage) {
        return ApiResult.success(flowableModelService.historyList(modelKey, basePage));
    }

    /**
     * 获取流程模型详细信息
     *
     * @param modelId 模型主键
     */
    @GetMapping(value = "/{modelId}")
    @Operation(summary = "获取流程模型详细信息")
    public ApiResult<FlowableModelVo> getInfo(@PathVariable(value = "modelId") String modelId) {
        return ApiResult.success(flowableModelService.getModel(modelId));
    }

    /**
     * 获取流程表单详细信息
     *
     * @param modelId 模型主键
     */
    @GetMapping(value = "{modelId}/getBpmnXml")
    @Operation(summary = "获取流程表单详细信息")
    public ApiResult<String> getBpmnXml(@PathVariable(value = "modelId") String modelId) {
        return ApiResult.success(flowableModelService.queryBpmnXmlById(modelId));
    }

    /**
     * 新增流程模型
     */
    @PostMapping("/add")
    @Operation(summary = "新增流程模型")
    public ApiResult<Void> add(@RequestBody FlowableModelDto flowableModelDto) {
        flowableModelService.insertModel(flowableModelDto);
        return ApiResult.success();
    }

    /**
     * 修改流程模型
     */
    @PutMapping("/edit")
    @Operation(summary = "修改流程模型")
    public ApiResult<Void> edit(@Validated(UpdateValidation.class) @RequestBody FlowableModelDto flowableModelDto) {
        flowableModelService.updateModel(flowableModelDto);
        return ApiResult.success();
    }

    /**
     * 保存流程模型
     */
    @PostMapping("/save")
    @Operation(summary = "保存流程模型")
    public ApiResult<String> save(@RequestBody FlowableModelDto flowableModelDto) {
        flowableModelService.saveModel(flowableModelDto);
        return ApiResult.success();
    }

    /**
     * 设为最新流程模型
     *
     * @param modelId
     * @return
     */
    @PostMapping("{modelId}/latest")
    @Operation(summary = "设为最新流程模型")
    public ApiResult<?> latest(@PathVariable(value = "modelId") String modelId) {
        flowableModelService.latestModel(modelId);
        return ApiResult.success();
    }

    /**
     * 删除流程模型
     *
     * @param modelIds 流程模型主键串
     */
    @DeleteMapping("/remove")
    @Operation(summary = "删除流程模型")
    public ApiResult<String> remove(@RequestBody List<String> modelIds) {
        flowableModelService.deleteByIds(modelIds);
        return ApiResult.success();
    }

    /**
     * 部署流程模型
     *
     * @param modelId 流程模型主键
     */
    @PostMapping("{modelId}/deploy")
    @Operation(summary = "部署流程模型")
    public ApiResult<Boolean> deployModel(@PathVariable(value = "modelId") String modelId) {
        return ApiResult.success(flowableModelService.deployModel(modelId));
    }


    /**
     * 列出自定义任务侦听器
     */
    @GetMapping("/list/taskListener")
    @Operation(summary = "列出自定义任务侦听器")
    public ApiResult<List<ListenerVo>> listTaskListener() {
        return ApiResult.success(flowableModelService.listTaskListener());
    }

    /**
     * 列出自定义执行侦听器
     */
    @GetMapping("/list/executionListener")
    @Operation(summary = "列出自定义执行侦听器")
    public ApiResult<List<ListenerVo>> listExecutionListener() {
        return ApiResult.success(flowableModelService.listExecutionListener());
    }
}
