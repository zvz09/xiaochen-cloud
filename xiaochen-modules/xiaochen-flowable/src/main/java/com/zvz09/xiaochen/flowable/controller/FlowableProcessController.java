package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.log.annotation.BizNo;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableCopyQuery;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.dto.StartProcessDto;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableCopyVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDetailVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableTaskVo;
import com.zvz09.xiaochen.flowable.domain.vo.SysFormVo;
import com.zvz09.xiaochen.flowable.service.IFlowableCopyService;
import com.zvz09.xiaochen.flowable.service.IFlowableProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zvz09
 */
@Slf4j
@Tag(name = "工作流流程管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/process")
public class FlowableProcessController {

    private final IFlowableProcessService flowableProcessService;
    private final IFlowableCopyService flowableCopyService;

    /**
     * 查询可发起流程列表
     *
     * @param processQuery 查询参数
     */
    @PostMapping(value = "/page")
    @Operation(summary = "查询可发起流程列表")
    public ApiResult<Page<FlowableDeployVo>> startProcessList(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageStartProcessList(processQuery));
    }

    /**
     * 我发起的流程
     */
    @PostMapping(value = "/own/page")
    @Operation(summary = "我发起的流程")
    public ApiResult<Page<FlowableTaskVo>> ownProcessList(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageOwnProcessList(processQuery));
    }

    /**
     * 获取待办列表
     */
    @PostMapping(value = "/todo/page")
    @Operation(summary = "获取待办列表")
    public ApiResult<Page<FlowableTaskVo>> todoProcessList(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageTodoProcessList(processQuery));
    }

    /**
     * 获取待签列表
     *
     * @param processQuery 流程业务对象
     */
    @PostMapping(value = "/receipted/page")
    @Operation(summary = "获取待签列表")
    public ApiResult<Page<FlowableTaskVo>> receiptedProcessList(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageReceiptedProcessList(processQuery));
    }

    /**
     * 获取已办列表
     */
    @PostMapping(value = "/finished/page")
    @Operation(summary = "获取已办列表")
    public ApiResult<Page<FlowableTaskVo>> finishedProcessList(@RequestBody ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageFinishedProcessList(processQuery));
    }

    /**
     * 查询流程部署关联表单信息
     *
     * @param definitionId 流程定义id
     * @param deployId     流程部署id
     */
    @GetMapping("{definitionId}/{deployId}/form")
    @Operation(summary = "查询流程部署关联表单信息")
    public ApiResult<SysFormVo> getForm(@PathVariable(value = "definitionId") String definitionId,
                                        @PathVariable(value = "deployId") String deployId,
                                        @RequestParam(value = "procInsId", required = false) String procInsId) {
        return ApiResult.success(flowableProcessService.selectFormContent(definitionId, deployId, procInsId));
    }

    /**
     * 根据流程定义id启动流程实例
     *
     * @param startProcessDto
     */
    @PostMapping("/start")
    @Operation(summary = "根据流程定义id启动流程实例")
    @BizNo(spEl = "{#StartProcessDto.processDefId}")
    public ApiResult<Void> start(@RequestBody StartProcessDto startProcessDto) {
        flowableProcessService.startProcessByDefId(startProcessDto.getProcessDefId(), startProcessDto.getVariables());
        return ApiResult.success();

    }

    /**
     * 删除流程实例
     *
     * @param instanceIds 流程实例ID串
     */
    @DeleteMapping("/instance")
    @Operation(summary = "删除流程实例")
    @BizNo(spEl = "{#instanceIds}")
    public ApiResult<Void> delete(@RequestBody String[] instanceIds) {
        flowableProcessService.deleteProcessByIds(instanceIds);
        return ApiResult.success();
    }

    /**
     * 读取xml文件
     *
     * @param processDefId 流程定义ID
     */
    @GetMapping("{processDefId}/bpmnXml")
    @Operation(summary = "读取xml文件")
    public ApiResult<String> getBpmnXml(@PathVariable(value = "processDefId") String processDefId) {
        return ApiResult.success(flowableProcessService.queryBpmnXmlById(processDefId));
    }

    /**
     * 查询流程详情信息
     *
     * @param procInsId 流程实例ID
     * @param taskId    任务ID
     */
    @GetMapping("{procInsId}/{taskId}/detail")
    @Operation(summary = "查询流程详情信息")
    public ApiResult<FlowableDetailVo> detail(@PathVariable(value = "procInsId") String procInsId,
                                              @PathVariable(value = "taskId") String taskId) {
        return ApiResult.success(flowableProcessService.queryProcessDetail(procInsId, taskId));
    }

    /**
     * 获取抄送列表
     *
     * @param copyBo    流程抄送对象
     * @param pageQuery 分页参数
     */
    @PostMapping(value = "/copy/page")
    @Operation(summary = "获取抄送列表")
    public ApiResult<Page<FlowableCopyVo>> copyProcessList(@RequestBody FlowableCopyQuery flowableCopyQuery) {
        flowableCopyQuery.setUserId(SecurityContextHolder.getUserId());
        return ApiResult.success(flowableCopyService.selectPageList(flowableCopyQuery));
    }
}
