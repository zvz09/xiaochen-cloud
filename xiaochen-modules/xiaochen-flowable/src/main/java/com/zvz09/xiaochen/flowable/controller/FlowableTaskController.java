package com.zvz09.xiaochen.flowable.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zvz09.xiaochen.common.core.annotation.BizNo;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableTaskDto;
import com.zvz09.xiaochen.flowable.service.IFlowableTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author zvz09
 */
@Slf4j
@Tag(name = "工作流任务管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class FlowableTaskController {

    private final IFlowableTaskService flowableTaskService;

    /**
     * 取消流程
     */
    @PostMapping(value = "/stopProcess")
    @Operation(summary = "取消流程")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> stopProcess(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.stopProcess(dto);
        return ApiResult.success();
    }

    /**
     * 撤回流程
     */
    @PostMapping(value = "/revokeProcess")
    @Operation(summary = "撤回流程")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> revokeProcess(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.revokeProcess(dto);
        return ApiResult.success();
    }

    /**
     * 获取流程变量
     *
     * @param taskId 流程任务Id
     */
    @GetMapping(value = "/{taskId}/processVariables")
    @Operation(summary = "获取流程变量")
    public ApiResult<Map<String, Object>> processVariables(@PathVariable(value = "taskId") String taskId) {
        return ApiResult.success(flowableTaskService.getProcessVariables(taskId));
    }

    /**
     * 审批流程
     */
    @PostMapping(value = "/complete")
    @Operation(summary = "审批流程")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> complete(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.complete(dto);
        return ApiResult.success();
    }

    /**
     * 拒绝任务
     */
    @PostMapping(value = "/reject")
    @Operation(summary = "拒绝任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> taskReject(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.taskReject(dto);
        return ApiResult.success();
    }

    /**
     * 退回任务
     */
    @PostMapping(value = "/return")
    @Operation(summary = "退回任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> taskReturn(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.taskReturn(dto);
        return ApiResult.success();
    }

    /**
     * 获取所有可回退的节点
     */
    @PostMapping(value = "/returnList")
    @Operation(summary = "获取所有可回退的节点")
    public ApiResult<List<FlowElement>> findReturnTaskList(@RequestBody FlowableTaskDto dto) {
        return ApiResult.success(flowableTaskService.findReturnTaskList(dto));
    }

    /**
     * 删除任务
     */
    @DeleteMapping(value = "/delete")
    @Operation(summary = "删除任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> delete(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.deleteTask(dto);
        return ApiResult.success();
    }

    /**
     * 认领/签收任务
     */
    @PostMapping(value = "/receipted")
    @Operation(summary = "签收任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> receipted(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.receipted(dto);
        return ApiResult.success();
    }

    /**
     * 取消认领/签收任务
     */
    @PostMapping(value = "/unReceipted")
    @Operation(summary = "取消签收任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> unReceipted(@RequestBody FlowableTaskDto dto) {
        flowableTaskService.unReceipted(dto);
        return ApiResult.success();
    }

    /**
     * 委派任务
     */
    @PostMapping(value = "/delegate")
    @Operation(summary = "委派任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> delegate(@RequestBody FlowableTaskDto dto) {
        if (ObjectUtil.hasNull(dto.getTaskId(), dto.getUserId())) {
            return ApiResult.fail("参数错误！");
        }
        flowableTaskService.delegateTask(dto);
        return ApiResult.success();
    }

    /**
     * 转办任务
     */
    @PostMapping(value = "/transfer")
    @Operation(summary = "转办任务")
    @BizNo(spEl = "{#dto.taskId}")
    public ApiResult<String> transfer(@RequestBody FlowableTaskDto dto) {
        if (ObjectUtil.hasNull(dto.getTaskId(), dto.getUserId())) {
            return ApiResult.fail("参数错误！");
        }
        flowableTaskService.transferTask(dto);
        return ApiResult.success();
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @PostMapping("/{processId}/diagram")
    @Operation(summary = "生成流程图")
    public void genProcessDiagram(HttpServletResponse response,
                                  @PathVariable(value = "processId") String processId) {
        InputStream inputStream = flowableTaskService.diagram(processId);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            log.error("生成流程图失败", e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                log.error("生成流程图失败", e);
            }
        }
    }
}
