package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.flowable.common.constant.ProcessConstants;
import com.zvz09.xiaochen.flowable.common.constant.TaskConstants;
import com.zvz09.xiaochen.flowable.common.enums.FlowComment;
import com.zvz09.xiaochen.flowable.common.enums.ProcessStatus;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableTaskDto;
import com.zvz09.xiaochen.flowable.flow.CustomProcessDiagramGenerator;
import com.zvz09.xiaochen.flowable.service.IFlowableCopyService;
import com.zvz09.xiaochen.flowable.service.IFlowableTaskService;
import com.zvz09.xiaochen.flowable.utils.FlowableUtils;
import com.zvz09.xiaochen.flowable.utils.ModelUtils;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.bpmn.model.Process;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author lizili-YF0033
 */
@Service
@RequiredArgsConstructor
public class FlowableTaskServiceImpl implements IFlowableTaskService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;

    private final RemoteUserService remoteUserService;

    private final IFlowableCopyService flowableCopyService;

    @Override
    public void startFirstTask(ProcessInstance processInstance, Map<String, Object> variables) {
        // 若第一个用户任务为发起人，则自动完成任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        if (CollUtil.isNotEmpty(tasks)) {
            String userIdStr = (String) variables.get(TaskConstants.PROCESS_INITIATOR);
            for (Task task : tasks) {
                if (StrUtil.equals(task.getAssignee(), userIdStr)) {
                    taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowComment.NORMAL.getType(), SecurityContextHolder.getUserName() + "发起流程申请");
                    taskService.complete(task.getId(), variables);
                }
            }
        }
    }

    @Override
    public void stopProcess(FlowableTaskDto dto) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(dto.getProcInsId()).list();
        if (CollectionUtils.isEmpty(task)) {
            throw new BusinessException("流程未启动或已执行完成，取消申请失败");
        }

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(dto.getProcInsId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (Objects.nonNull(bpmnModel)) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (CollectionUtils.isNotEmpty(endNodes)) {
                Authentication.setAuthenticatedUserId(String.valueOf(SecurityContextHolder.getUserId()));
                // 获取当前流程最后一个节点
                String endId = endNodes.get(0).getId();
                List<Execution> executions = runtimeService.createExecutionQuery()
                        .parentId(processInstance.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                // 变更流程为已结束状态
                runtimeService.createChangeActivityStateBuilder()
                        .moveExecutionsToSingleActivityId(executionIds, endId).changeState();
            }
        }
    }

    @Override
    public void revokeProcess(FlowableTaskDto dto) {
        String procInsId = dto.getProcInsId();
        String taskId = dto.getTaskId();
        // 校验流程是否结束
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(procInsId)
                .active()
                .singleResult();
        if (ObjectUtil.isNull(processInstance)) {
            throw new BusinessException("流程已结束或已挂起，无法执行撤回操作");
        }
        // 获取待撤回任务实例
        HistoricTaskInstance currTaskIns = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .taskAssignee(String.valueOf(SecurityContextHolder.getUserId()))
                .singleResult();
        if (ObjectUtil.isNull(currTaskIns)) {
            throw new BusinessException("当前任务不存在，无法执行撤回操作");
        }
        // 获取 bpmn 模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currTaskIns.getProcessDefinitionId());
        UserTask currUserTask = ModelUtils.getUserTaskByKey(bpmnModel, currTaskIns.getTaskDefinitionKey());
        // 查找下一级用户任务列表
        List<UserTask> nextUserTaskList = ModelUtils.findNextUserTasks(currUserTask);
        List<String> nextUserTaskKeys = nextUserTaskList.stream().map(UserTask::getId).collect(Collectors.toList());

        // 获取当前节点之后已完成的流程历史节点
        List<HistoricTaskInstance> finishedTaskInsList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInsId)
                .taskCreatedAfter(currTaskIns.getEndTime())
                .finished()
                .list();
        for (HistoricTaskInstance finishedTaskInstance : finishedTaskInsList) {
            // 检查已完成流程历史节点是否存在下一级中
            if (CollUtil.contains(nextUserTaskKeys, finishedTaskInstance.getTaskDefinitionKey())) {
                throw new BusinessException("下一流程已处理，无法执行撤回操作");
            }
        }
        // 获取所有激活的任务节点，找到需要撤回的任务
        List<Task> activateTaskList = taskService.createTaskQuery().processInstanceId(procInsId).list();
        List<String> revokeExecutionIds = new ArrayList<>();
        for (Task task : activateTaskList) {
            // 检查激活的任务节点是否存在下一级中，如果存在，则加入到需要撤回的节点
            if (CollUtil.contains(nextUserTaskKeys, task.getTaskDefinitionKey())) {
                // 添加撤回审批信息
                taskService.setAssignee(task.getId(), String.valueOf(SecurityContextHolder.getUserId()));
                taskService.addComment(task.getId(), task.getProcessInstanceId(), FlowComment.REVOKE.getType(), SecurityContextHolder.getUserName() + "撤回流程审批");
                revokeExecutionIds.add(task.getExecutionId());
            }
        }
        try {
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(procInsId)
                    .moveExecutionsToSingleActivityId(revokeExecutionIds, currTaskIns.getTaskDefinitionKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("执行撤回操作失败");
        }
    }

    @Override
    public Map<String, Object> getProcessVariables(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return historicTaskInstance.getProcessVariables();
        }
        return taskService.getVariables(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(FlowableTaskDto dto) {
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new BusinessException("任务不存在");
        }
        // 获取 bpmn 模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(dto.getTaskId(), dto.getProcInsId(), FlowComment.DELEGATE.getType(), dto.getComment());
            taskService.resolveTask(dto.getTaskId());
        } else {
            taskService.addComment(dto.getTaskId(), dto.getProcInsId(), FlowComment.NORMAL.getType(), dto.getComment());
            taskService.setAssignee(dto.getTaskId(), String.valueOf(SecurityContextHolder.getUserId()));
            if (ObjectUtil.isNotEmpty(dto.getVariables())) {
                // 获取模型信息
                String localScopeValue = ModelUtils.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), ProcessConstants.PROCESS_FORM_LOCAL_SCOPE);
                boolean localScope = Convert.toBool(localScopeValue, false);
                taskService.complete(dto.getTaskId(), dto.getVariables(), localScope);
            } else {
                taskService.complete(dto.getTaskId());
            }
        }
        // 设置任务节点名称
        dto.setTaskName(task.getName());
        // 处理下一级审批人
        if (dto.getNextUserIds() != null && !dto.getNextUserIds().isEmpty()) {
            this.assignNextUsers(bpmnModel, dto.getProcInsId(), dto.getNextUserIds());
        }
        // 处理抄送用户
        if (!flowableCopyService.makeCopy(dto)) {
            throw new BusinessException("抄送任务失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskReject(FlowableTaskDto dto) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (ObjectUtil.isNull(task)) {
            throw new BusinessException("获取任务信息异常！");
        }
        if (task.isSuspended()) {
            throw new BusinessException("任务处于挂起状态");
        }
        // 获取流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(dto.getProcInsId())
                .singleResult();
        if (processInstance == null) {
            throw new BusinessException("流程实例不存在，请确认！");
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        // 添加审批意见
        taskService.addComment(dto.getTaskId(), dto.getProcInsId(), FlowComment.REJECT.getType(), dto.getComment());
        // 设置流程状态为已终结
        runtimeService.setVariable(processInstance.getId(), ProcessConstants.PROCESS_STATUS_KEY, ProcessStatus.TERMINATED.getStatus());
        // 获取所有节点信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        EndEvent endEvent = ModelUtils.getEndEvent(bpmnModel);
        // 终止流程
        List<Execution> executions = runtimeService.createExecutionQuery().parentId(task.getProcessInstanceId()).list();
        List<String> executionIds = executions.stream().map(Execution::getId).collect(Collectors.toList());
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveExecutionsToSingleActivityId(executionIds, endEvent.getId())
                .changeState();
        // 处理抄送用户
        if (!flowableCopyService.makeCopy(dto)) {
            throw new BusinessException("抄送任务失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskReturn(FlowableTaskDto dto) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (ObjectUtil.isNull(task)) {
            throw new BusinessException("获取任务信息异常！");
        }
        if (task.isSuspended()) {
            throw new BusinessException("任务处于挂起状态");
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取流程模型信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        // 获取当前任务节点元素
        FlowElement source = ModelUtils.getFlowElementById(bpmnModel, task.getTaskDefinitionKey());
        // 获取跳转的节点元素
        FlowElement target = ModelUtils.getFlowElementById(bpmnModel, dto.getTargetKey());
        // 从当前节点向前扫描，判断当前节点与目标节点是否属于串行，若目标节点是在并行网关上或非同一路线上，不可跳转
        boolean isSequential = ModelUtils.isSequentialReachable(source, target, new HashSet<>());
        if (!isSequential) {
            throw new BusinessException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }

        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowComment.REBACK.getType(), dto.getComment());
        }

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, dto.getTargetKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("无法取消或开始活动");
        }
        // 设置任务节点名称
        dto.setTaskName(task.getName());
        // 处理抄送用户
        if (!flowableCopyService.makeCopy(dto)) {
            throw new BusinessException("抄送任务失败");
        }
    }

    @Override
    public List<FlowElement> findReturnTaskList(FlowableTaskDto dto) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取流程模型信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        // 查询历史节点实例
        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .activityType(BpmnXMLConstants.ELEMENT_TASK_USER)
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        List<String> activityIdList = activityInstanceList.stream()
                .map(HistoricActivityInstance::getActivityId)
                .filter(activityId -> !StringUtils.equals(activityId, task.getTaskDefinitionKey()))
                .distinct()
                .collect(Collectors.toList());
        // 获取当前任务节点元素
        FlowElement source = ModelUtils.getFlowElementById(bpmnModel, task.getTaskDefinitionKey());
        List<FlowElement> elementList = new ArrayList<>();
        for (String activityId : activityIdList) {
            FlowElement target = ModelUtils.getFlowElementById(bpmnModel, activityId);
            boolean isSequential = ModelUtils.isSequentialReachable(source, target, new HashSet<>());
            if (isSequential) {
                elementList.add(target);
            }
        }
        return elementList;
    }

    @Override
    public void deleteTask(FlowableTaskDto dto) {
        taskService.deleteTask(dto.getTaskId(), dto.getComment());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receipted(FlowableTaskDto dto) {
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new BusinessException("任务不存在");
        }
        taskService.claim(dto.getTaskId(), String.valueOf(SecurityContextHolder.getUserId()));
    }

    @Override
    public void unReceipted(FlowableTaskDto dto) {
        taskService.unclaim(dto.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(FlowableTaskDto dto) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new BusinessException("获取任务失败！");
        }
        StringBuilder commentBuilder = new StringBuilder(SecurityContextHolder.getUserName())
                .append("->");
        SysUser user = remoteUserService.getById(Long.parseLong(dto.getUserId()));
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getNickName());
        } else {
            commentBuilder.append(dto.getUserId());
        }
        if (StringUtils.isNotBlank(dto.getComment())) {
            commentBuilder.append(": ").append(dto.getComment());
        }
        // 添加审批意见
        taskService.addComment(dto.getTaskId(), task.getProcessInstanceId(), FlowComment.DELEGATE.getType(), commentBuilder.toString());
        // 设置办理人为当前登录人
        taskService.setOwner(dto.getTaskId(), String.valueOf(SecurityContextHolder.getUserId()));
        // 执行委派
        taskService.delegateTask(dto.getTaskId(), dto.getUserId());
        // 设置任务节点名称
        dto.setTaskName(task.getName());
        // 处理抄送用户
        if (!flowableCopyService.makeCopy(dto)) {
            throw new BusinessException("抄送任务失败");
        }
    }

    @Override
    public void transferTask(FlowableTaskDto dto) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new BusinessException("获取任务失败！");
        }
        StringBuilder commentBuilder = new StringBuilder(SecurityContextHolder.getUserName())
                .append("->");
        SysUser user = remoteUserService.getById(Long.parseLong(dto.getUserId()));
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getNickName());
        } else {
            commentBuilder.append(dto.getUserId());
        }
        if (StringUtils.isNotBlank(dto.getComment())) {
            commentBuilder.append(": ").append(dto.getComment());
        }
        // 添加审批意见
        taskService.addComment(dto.getTaskId(), task.getProcessInstanceId(), FlowComment.TRANSFER.getType(), commentBuilder.toString());
        // 设置拥有者为当前登录人
        taskService.setOwner(dto.getTaskId(), String.valueOf(SecurityContextHolder.getUserId()));
        // 转办任务
        taskService.setAssignee(dto.getTaskId(), dto.getUserId());
        // 设置任务节点名称
        dto.setTaskName(task.getName());
        // 处理抄送用户
        if (!flowableCopyService.makeCopy(dto)) {
            throw new BusinessException("抄送任务失败");
        }
    }

    @Override
    public InputStream diagram(String processId) {
        String processDefinitionId;
        // 获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
            if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                //高亮线
                highLightedFlows.add(tempActivity.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(tempActivity.getActivityId());
            }
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);

    }

    /**
     * 指派下一任务审批人
     *
     * @param bpmnModel    bpmn模型
     * @param processInsId 流程实例id
     * @param userIds      用户ids
     */
    private void assignNextUsers(BpmnModel bpmnModel, String processInsId, List<Long> userIds) {
        // 获取所有节点信息
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId(processInsId)
                .list();
        if (list.isEmpty()) {
            return;
        }

        Queue<Long> assignIds = new LinkedList(userIds);
        if (list.size() == assignIds.size()) {
            for (Task task : list) {
                taskService.setAssignee(task.getId(), String.valueOf(assignIds.poll()));
            }
            return;
        }
        // 优先处理非多实例任务
        Iterator<Task> iterator = list.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (!ModelUtils.isMultiInstance(bpmnModel, task.getTaskDefinitionKey())) {
                if (!assignIds.isEmpty()) {
                    taskService.setAssignee(task.getId(), String.valueOf(assignIds.poll()));
                }
                iterator.remove();
            }
        }
        // 若存在多实例任务，则进行动态加减签
        if (CollUtil.isNotEmpty(list)) {
            if (assignIds.isEmpty()) {
                // 动态减签
                for (Task task : list) {
                    runtimeService.deleteMultiInstanceExecution(task.getExecutionId(), true);
                }
            } else {
                // 动态加签
                for (Long assignId : assignIds) {
                    Map<String, Object> assignVariables = Collections.singletonMap(BpmnXMLConstants.ATTRIBUTE_TASK_USER_ASSIGNEE, assignId);
                    runtimeService.addMultiInstanceExecution(list.get(0).getTaskDefinitionKey(), list.get(0).getProcessInstanceId(), assignVariables);
                }
            }
        }
    }
}
