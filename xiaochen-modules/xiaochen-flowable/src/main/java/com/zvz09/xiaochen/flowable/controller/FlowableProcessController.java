package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableCopyDto;
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
    @GetMapping(value = "/list")
    @Operation(summary = "查询可发起流程列表")
    public ApiResult<Page<FlowableDeployVo>> startProcessList(ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageStartProcessList(processQuery));
    }

    /**
     * 我发起的流程
     */
    @GetMapping(value = "/ownList")
    @Operation(summary = "我发起的流程")
    public ApiResult<Page<FlowableTaskVo>> ownProcessList(ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageOwnProcessList(processQuery));
    }

    /**
     * 获取待办列表
     */
    @GetMapping(value = "/todoList")
    @Operation(summary = "获取待办列表")
    public ApiResult<Page<FlowableTaskVo>> todoProcessList(ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageTodoProcessList(processQuery));
    }

    /**
     * 获取待签列表
     *
     * @param processQuery 流程业务对象
     */
    @GetMapping(value = "/receiptedList")
    @Operation(summary = "获取待签列表")
    public ApiResult<Page<FlowableTaskVo>> receiptedProcessList(ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageReceiptedProcessList(processQuery));
    }

    /**
     * 获取已办列表
     */
    @GetMapping(value = "/finishedList")
    @Operation(summary = "获取已办列表")
    public ApiResult<Page<FlowableTaskVo>> finishedProcessList(ProcessQuery processQuery) {
        return ApiResult.success(flowableProcessService.selectPageFinishedProcessList(processQuery));
    }

    /**
     * 查询流程部署关联表单信息
     *
     * @param definitionId 流程定义id
     * @param deployId     流程部署id
     */
    @GetMapping("/getProcessForm")
    @Operation(summary = "查询流程部署关联表单信息")
    public ApiResult<SysFormVo> getForm(@RequestParam(value = "definitionId") String definitionId,
                                        @RequestParam(value = "deployId") String deployId,
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
    public ApiResult<Void> delete(@RequestBody String[] instanceIds) {
        flowableProcessService.deleteProcessByIds(instanceIds);
        return ApiResult.success();
    }

    /**
     * 读取xml文件
     *
     * @param processDefId 流程定义ID
     */
    @GetMapping("/bpmnXml")
    @Operation(summary = "读取xml文件")
    public ApiResult<String> getBpmnXml(String processDefId) {
        return ApiResult.success(flowableProcessService.queryBpmnXmlById(processDefId));
    }

    /**
     * 查询流程详情信息
     *
     * @param procInsId 流程实例ID
     * @param taskId    任务ID
     */
    @GetMapping("/detail")
    @Operation(summary = "查询流程详情信息")
    public ApiResult<FlowableDetailVo> detail(String procInsId, String taskId) {
        return ApiResult.success(flowableProcessService.queryProcessDetail(procInsId, taskId));
    }

    /**
     * 获取抄送列表
     *
     * @param copyBo    流程抄送对象
     * @param pageQuery 分页参数
     */
    @GetMapping(value = "/copyList")
    @Operation(summary = "获取抄送列表")
    public ApiResult<Page<FlowableCopyVo>> copyProcessList(FlowableCopyDto flowableCopyDto, BasePage pageQuery) {
        flowableCopyDto.setUserId(SecurityContextHolder.getUserId());
        return ApiResult.success(flowableCopyService.selectPageList(flowableCopyDto, pageQuery));
    }
}
