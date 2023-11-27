package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.flowable.common.constant.ProcessConstants;
import com.zvz09.xiaochen.flowable.common.constant.TaskConstants;
import com.zvz09.xiaochen.flowable.common.enums.ProcessStatus;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDetailVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableProcNodeVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableTaskVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableViewerVo;
import com.zvz09.xiaochen.flowable.domain.vo.SysFormVo;
import com.zvz09.xiaochen.flowable.service.IFlowableProcessService;
import com.zvz09.xiaochen.flowable.service.IFlowableTaskService;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import com.zvz09.xiaochen.flowable.service.ISysFormService;
import com.zvz09.xiaochen.flowable.utils.FlowableUtils;
import com.zvz09.xiaochen.flowable.utils.ModelUtils;
import com.zvz09.xiaochen.flowable.utils.ProcessUtils;
import com.zvz09.xiaochen.system.api.RemoteDepartmentService;
import com.zvz09.xiaochen.system.api.RemoteRoleService;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableProcessServiceImpl implements IFlowableProcessService {


    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final TaskService taskService;
    private final IdentityService identityService;
    private final RuntimeService runtimeService;

    private final RemoteUserService remoteUserService;
    private final RemoteRoleService remoteRoleService;
    private final RemoteDepartmentService remoteDepartmentService;

    private final IFlowableTaskService flowableTaskService;
    private final ISysDeployFormService sysDeployFormService;
    private final ISysFormService sysFormService;

    @Override
    public Page<FlowableDeployVo> selectPageStartProcessList(ProcessQuery processQuery) {
        Page<FlowableDeployVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .active()
                .orderByProcessDefinitionKey()
                .asc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(processDefinitionQuery, processQuery);
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return page;
        }
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));

        List<FlowableDeployVo> definitionVoList = new ArrayList<>();
        for (ProcessDefinition processDefinition : definitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            FlowableDeployVo vo = new FlowableDeployVo();
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessKey(processDefinition.getKey());
            vo.setProcessName(processDefinition.getName());
            vo.setVersion(processDefinition.getVersion());
            vo.setDeploymentId(deploymentId);
            vo.setSuspended(processDefinition.isSuspended());
            vo.setCategory(deployment.getCategory());
            // 流程部署时间
            vo.setDeploymentTime(deployment.getDeploymentTime());
            definitionVoList.add(vo);
        }
        page.setRecords(definitionVoList);
        page.setTotal(pageTotal);
        return page;
    }

    @Override
    public Page<FlowableTaskVo> selectPageOwnProcessList(ProcessQuery processQuery) {
        Page<FlowableTaskVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(SecurityContextHolder.getUserId()))
                .orderByProcessInstanceStartTime()
                .desc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(historicProcessInstanceQuery, processQuery);
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
                .listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));
        page.setTotal(historicProcessInstanceQuery.count());
        List<FlowableTaskVo> taskVoList = new ArrayList<>();
        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            FlowableTaskVo taskVo = new FlowableTaskVo();
            // 获取流程状态
            HistoricVariableInstance processStatusVariable = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisIns.getId())
                    .variableName(ProcessConstants.PROCESS_STATUS_KEY)
                    .singleResult();
            String processStatus = null;
            if (ObjectUtil.isNotNull(processStatusVariable)) {
                processStatus = Convert.toStr(processStatusVariable.getValue());
            }
            // 兼容旧流程
            if (processStatus == null) {
                processStatus = ObjectUtil.isNull(hisIns.getEndTime()) ? ProcessStatus.RUNNING.getStatus() : ProcessStatus.COMPLETED.getStatus();
            }
            taskVo.setProcessStatus(processStatus);
            taskVo.setCreateTime(hisIns.getStartTime());
            taskVo.setFinishTime(hisIns.getEndTime());
            taskVo.setProcInsId(hisIns.getId());

            // 计算耗时
            if (Objects.nonNull(hisIns.getEndTime())) {
                taskVo.setDuration(DateUtils.getDatePoor(hisIns.getEndTime(), hisIns.getStartTime()));
            } else {
                taskVo.setDuration(DateUtils.getDatePoor(DateUtils.getNowDate(), hisIns.getStartTime()));
            }
            // 流程部署实例信息
            Deployment deployment = repositoryService.createDeploymentQuery()
                    .deploymentId(hisIns.getDeploymentId()).singleResult();
            taskVo.setDeployId(hisIns.getDeploymentId());
            taskVo.setProcDefId(hisIns.getProcessDefinitionId());
            taskVo.setProcDefName(hisIns.getProcessDefinitionName());
            taskVo.setProcDefVersion(hisIns.getProcessDefinitionVersion());
            taskVo.setCategory(deployment.getCategory());
            // 当前所处流程
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(hisIns.getId()).includeIdentityLinks().list();
            if (CollUtil.isNotEmpty(taskList)) {
                taskVo.setTaskName(taskList.stream().map(Task::getName).filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
            }
            taskVoList.add(taskVo);
        }
        page.setRecords(taskVoList);
        return page;
    }

    @Override
    public Page<FlowableTaskVo> selectPageTodoProcessList(ProcessQuery processQuery) {
        Page<FlowableTaskVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskAssignee(String.valueOf(SecurityContextHolder.getUserId()))
                .orderByTaskCreateTime().desc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(taskQuery, processQuery);
        page.setTotal(taskQuery.count());
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<Task> taskList = taskQuery.listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));
        List<FlowableTaskVo> flowList = new ArrayList<>();
        for (Task task : taskList) {
            FlowableTaskVo flowTask = new FlowableTaskVo();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setClaimTime(task.getClaimTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            Long userId = Long.parseLong(historicProcessInstance.getStartUserId());
            SysUser sysUser = remoteUserService.getById(userId);
            flowTask.setStartUserId(userId);
            if (sysUser != null) {
                flowTask.setStartUserName(sysUser.getNickName());
            }

            // 流程变量
            flowTask.setProcVars(task.getProcessVariables());

            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return page;
    }

    @Override
    public Page<FlowableTaskVo> selectPageReceiptedProcessList(ProcessQuery processQuery) {
        Page<FlowableTaskVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .or()
                .taskCandidateUser(String.valueOf(SecurityContextHolder.getUserId()))
                .taskCandidateGroupIn(this.getCandidateGroup())
                .endOr()
                .orderByTaskCreateTime().desc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(taskQuery, processQuery);
        page.setTotal(taskQuery.count());
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<Task> taskList = taskQuery.listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));
        List<FlowableTaskVo> flowList = new ArrayList<>();
        for (Task task : taskList) {
            FlowableTaskVo flowTask = new FlowableTaskVo();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            Long userId = Long.parseLong(historicProcessInstance.getStartUserId());
            flowTask.setStartUserId(userId);
            SysUser sysUser = remoteUserService.getById(userId);
            if (sysUser != null) {
                flowTask.setStartUserName(sysUser.getNickName());
            }

            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return page;
    }

    @Override
    public Page<FlowableTaskVo> selectPageFinishedProcessList(ProcessQuery processQuery) {
        Page<FlowableTaskVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskAssignee(String.valueOf(SecurityContextHolder.getUserId()))
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(taskInstanceQuery, processQuery);
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));
        List<FlowableTaskVo> hisTaskList = new ArrayList<>();
        for (HistoricTaskInstance histTask : historicTaskInstanceList) {
            FlowableTaskVo flowTask = new FlowableTaskVo();
            // 当前流程信息
            flowTask.setTaskId(histTask.getId());
            // 审批人员信息
            flowTask.setCreateTime(histTask.getCreateTime());
            flowTask.setFinishTime(histTask.getEndTime());
            flowTask.setDuration(DateUtil.formatBetween(histTask.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            flowTask.setProcDefId(histTask.getProcessDefinitionId());
            flowTask.setTaskDefKey(histTask.getTaskDefinitionKey());
            flowTask.setTaskName(histTask.getName());

            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(histTask.getProcessDefinitionId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(histTask.getProcessInstanceId());
            flowTask.setHisProcInsId(histTask.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(histTask.getProcessInstanceId())
                    .singleResult();
            Long userId = Long.parseLong(historicProcessInstance.getStartUserId());
            flowTask.setStartUserId(userId);
            SysUser sysUser = remoteUserService.getById(userId);
            if (sysUser != null) {
                flowTask.setStartUserName(sysUser.getNickName());
            }

            // 流程变量
            flowTask.setProcVars(histTask.getProcessVariables());

            hisTaskList.add(flowTask);
        }
        page.setTotal(taskInstanceQuery.count());
        page.setRecords(hisTaskList);

        return page;
    }

    @Override
    public SysFormVo selectFormContent(String definitionId, String deployId, String procInsId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        if (ObjectUtil.isNull(bpmnModel)) {
            throw new BusinessException("获取流程设计失败！");
        }
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String customizationFormKey = startEvent.getAttributeValue("http://flowable.org/bpmn", "customizationFormKey");
        SysDeployForm deployForm = sysDeployFormService.getOne(new LambdaQueryWrapper<SysDeployForm>()
                .eq(SysDeployForm::getDeployId, deployId)
                .eq(SysDeployForm::getFormKey, customizationFormKey)
                .eq(SysDeployForm::getNodeKey, startEvent.getId()));

        if (ObjectUtil.isNull(deployForm)) {
            throw new BusinessException("获取流程表单失败！");
        }
        SysFormVo sysFormVo = new SysFormVo();
        sysFormVo.setFormName(deployForm.getFormName());
        sysFormVo.setFormTemplate(deployForm.getContent());
        if (ObjectUtil.isNotEmpty(procInsId)) {
            // 获取流程实例
            HistoricProcessInstance historicProcIns = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(procInsId)
                    .includeProcessVariables()
                    .singleResult();
            //填充表单信息
            sysFormVo.setModels(JacksonUtil.writeValueAsString(historicProcIns.getProcessVariables()));

        }
        return sysFormVo;
    }

    @Override
    public void startProcessByDefId(String processDefId, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefId).singleResult();
            startProcess(processDefinition, variables);
        } catch (Exception e) {
            log.error("流程启动错误", e);
            throw new BusinessException("流程启动错误");
        }
    }

    @Override
    public void deleteProcessByIds(String[] instanceIds) {
        List<String> ids = Arrays.asList(instanceIds);
        // 校验流程是否结束
        long activeInsCount = runtimeService.createProcessInstanceQuery()
                .processInstanceIds(new HashSet<>(ids)).active().count();
        if (activeInsCount > 0) {
            throw new BusinessException("不允许删除进行中的流程实例");
        }
        // 删除历史流程实例
        historyService.bulkDeleteHistoricProcessInstances(ids);
    }

    @Override
    public String queryBpmnXmlById(String processDefId) {
        InputStream inputStream = repositoryService.getProcessModel(processDefId);
        try {
            return IoUtil.readUtf8(inputStream);
        } catch (IORuntimeException exception) {
            throw new RuntimeException("加载xml文件异常");
        }
    }

    @Override
    public FlowableDetailVo queryProcessDetail(String procInsId, String taskId) {
        FlowableDetailVo detailVo = new FlowableDetailVo();
        // 获取流程实例
        HistoricProcessInstance historicProcIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInsId)
                .includeProcessVariables()
                .singleResult();
        if (StringUtils.isNotBlank(taskId)) {
            HistoricTaskInstance taskIns = historyService.createHistoricTaskInstanceQuery()
                    .taskId(taskId)
                    .includeIdentityLinks()
                    .includeProcessVariables()
                    .includeTaskLocalVariables()
                    .singleResult();
            if (taskIns == null) {
                throw new BusinessException("没有可办理的任务！");
            }
            detailVo.setTaskFormData(currTaskFormData(historicProcIns.getDeploymentId(), taskIns));
        }
        // 获取Bpmn模型信息
        InputStream inputStream = repositoryService.getProcessModel(historicProcIns.getProcessDefinitionId());
        String bpmnXmlStr = StrUtil.utf8Str(IoUtil.readBytes(inputStream, false));
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bpmnXmlStr);
        detailVo.setBpmnXml(bpmnXmlStr);
        detailVo.setHistoryProcNodeList(historyProcNodeList(historicProcIns));
        detailVo.setProcessFormList(processFormList(bpmnModel, historicProcIns));
        detailVo.setFlowViewer(getFlowViewer(bpmnModel, procInsId));
        return detailVo;
    }

    /**
     * 启动流程实例
     */
    private void startProcess(ProcessDefinition procDef, Map<String, Object> variables) {
        if (ObjectUtil.isNotNull(procDef) && procDef.isSuspended()) {
            throw new BusinessException("流程已被挂起，请先激活流程");
        }
        // 设置流程发起人Id到流程中
        String userIdStr = String.valueOf(SecurityContextHolder.getUserId());
        identityService.setAuthenticatedUserId(userIdStr);
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, userIdStr);
        // 设置流程状态为进行中
        variables.put(ProcessConstants.PROCESS_STATUS_KEY, ProcessStatus.RUNNING.getStatus());
        // 发起流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDef.getId(), variables);
        // 第一个用户任务为发起人，则自动完成任务
        flowableTaskService.startFirstTask(processInstance, variables);
    }

    /**
     * 获取当前任务流程表单信息
     */
    private SysFormVo currTaskFormData(String deployId, HistoricTaskInstance taskIns) {
        SysDeployForm deployForm = sysDeployFormService.getOne(new LambdaQueryWrapper<SysDeployForm>()
                .eq(SysDeployForm::getDeployId, deployId)
                .eq(SysDeployForm::getNodeKey, taskIns.getTaskDefinitionKey()));
        if (ObjectUtil.isNotEmpty(deployForm)) {
            SysFormVo sysFormVo = new SysFormVo();
            sysFormVo.setFormName(deployForm.getFormName());
            sysFormVo.setFormTemplate(deployForm.getContent());
            //填充表单信息
            sysFormVo.setModels(JacksonUtil.writeValueAsString(taskIns.getProcessVariables()));
            return sysFormVo;
        }
        return null;
    }

    /**
     * 获取历史任务信息列表
     */
    private List<FlowableProcNodeVo> historyProcNodeList(HistoricProcessInstance historicProcIns) {
        String procInsId = historicProcIns.getId();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .activityTypes(CollUtil.newHashSet(BpmnXMLConstants.ELEMENT_EVENT_START, BpmnXMLConstants.ELEMENT_EVENT_END, BpmnXMLConstants.ELEMENT_TASK_USER))
                .orderByHistoricActivityInstanceStartTime().desc()
                .orderByHistoricActivityInstanceEndTime().desc()
                .list();

        List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);

        List<FlowableProcNodeVo> elementVoList = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            FlowableProcNodeVo elementVo = new FlowableProcNodeVo();
            elementVo.setProcDefId(activityInstance.getProcessDefinitionId());
            elementVo.setActivityId(activityInstance.getActivityId());
            elementVo.setActivityName(activityInstance.getActivityName());
            elementVo.setActivityType(activityInstance.getActivityType());
            elementVo.setCreateTime(activityInstance.getStartTime());
            elementVo.setEndTime(activityInstance.getEndTime());
            if (ObjectUtil.isNotNull(activityInstance.getDurationInMillis())) {
                elementVo.setDuration(DateUtil.formatBetween(activityInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            }

            if (BpmnXMLConstants.ELEMENT_EVENT_START.equals(activityInstance.getActivityType())) {
                if (ObjectUtil.isNotNull(historicProcIns)) {
                    Long userId = Long.parseLong(historicProcIns.getStartUserId());
                    SysUser user = remoteUserService.getById(userId);
                    if (user != null) {
                        elementVo.setAssigneeId(user.getId());
                        elementVo.setAssigneeName(user.getNickName());
                    }
                }
            } else if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())) {
                if (StringUtils.isNotBlank(activityInstance.getAssignee())) {
                    SysUser user = remoteUserService.getById(Long.parseLong(activityInstance.getAssignee()));
                    elementVo.setAssigneeId(user.getId());
                    elementVo.setAssigneeName(user.getNickName());
                }
                // 展示审批人员
                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(activityInstance.getTaskId());
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            SysUser user = remoteUserService.getById(Long.parseLong(identityLink.getUserId()));
                            stringBuilder.append(user.getNickName()).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(TaskConstants.ROLE_GROUP_PREFIX)) {
                                String roleCode = StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.ROLE_GROUP_PREFIX);
                                SysRole role = remoteRoleService.getByRoleCode(roleCode);
                                stringBuilder.append(role.getRoleName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstants.DEPT_GROUP_PREFIX)) {
                                Long deptId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.DEPT_GROUP_PREFIX));
                                SysDepartment dept = remoteDepartmentService.getById(deptId);
                                stringBuilder.append(dept.getDeptName()).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    elementVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(commentList)) {
                    List<Comment> comments = new ArrayList<>();
                    for (Comment comment : commentList) {

                        if (comment.getTaskId().equals(activityInstance.getTaskId())) {
                            comments.add(comment);
                        }
                    }
                    elementVo.setCommentList(comments);
                }
            }
            elementVoList.add(elementVo);
        }
        return elementVoList;
    }

    /**
     * 获取历史流程表单信息
     */
    private List<SysFormVo> processFormList(BpmnModel bpmnModel, HistoricProcessInstance historicProcIns) {
        List<SysFormVo> procFormList = new ArrayList<>();

        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(historicProcIns.getId()).finished()
                .activityTypes(CollUtil.newHashSet(BpmnXMLConstants.ELEMENT_EVENT_START, BpmnXMLConstants.ELEMENT_TASK_USER))
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        List<String> processFormKeys = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : activityInstanceList) {
            // 获取当前节点流程元素信息
            FlowElement flowElement = ModelUtils.getFlowElementById(bpmnModel, activityInstance.getActivityId());
            // 获取当前节点表单Key
            String formKey = ModelUtils.getFormKey(flowElement);
            if (formKey == null) {
                continue;
            }
            boolean localScope = Convert.toBool(ModelUtils.getElementAttributeValue(flowElement, ProcessConstants.PROCESS_FORM_LOCAL_SCOPE), false);
            Map<String, Object> variables;
            if (localScope) {
                // 查询任务节点参数，并转换成Map
                variables = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(historicProcIns.getId())
                        .taskId(activityInstance.getTaskId())
                        .list()
                        .stream()
                        .collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue));
            } else {
                if (processFormKeys.contains(formKey)) {
                    continue;
                }
                variables = historicProcIns.getProcessVariables();
                processFormKeys.add(formKey);
            }
            // 非节点表单此处查询结果可能有多条，只获取第一条信息
            List<SysDeployForm> formInfoList = sysDeployFormService.list(new LambdaQueryWrapper<SysDeployForm>()
                    .eq(SysDeployForm::getDeployId, historicProcIns.getDeploymentId())
                    .eq(SysDeployForm::getFormKey, formKey)
                    .eq(localScope, SysDeployForm::getNodeKey, flowElement.getId()));

            //@update by Brath：避免空集合导致的NULL空指针
            SysDeployForm deployForm = formInfoList.stream().findFirst().orElse(null);

            if (ObjectUtil.isNotNull(deployForm)) {
                // 旧数据 formInfo.getFormName() 为 null
                String formName = Optional.ofNullable(deployForm.getFormName()).orElse(StringUtils.EMPTY);
                String title = localScope ? formName.concat("(" + flowElement.getName() + ")") : formName;
                SysFormVo sysFormVo = new SysFormVo();
                sysFormVo.setFormName(deployForm.getFormName());
                sysFormVo.setFormTemplate(deployForm.getContent());
                sysFormVo.setModels(JacksonUtil.writeValueAsString(variables));
                procFormList.add(sysFormVo);
            }
        }
        return procFormList;
    }

    /**
     * 获取流程执行过程
     *
     * @param procInsId
     * @return
     */
    private FlowableViewerVo getFlowViewer(BpmnModel bpmnModel, String procInsId) {
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return new FlowableViewerVo();
        }
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream()
                .filter(item -> ObjectUtil.isNotNull(item.getEndTime())).collect(Collectors.toList());
        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });
        // 查询所有未结束的节点
        Set<String> unfinishedTaskSet = allActivityInstanceList.stream()
                .filter(item -> ObjectUtil.isNull(item.getEndTime()))
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toSet());
        // DFS 查询未通过的元素集合
        Set<String> rejectedSet = FlowableUtils.dfsFindRejects(bpmnModel, unfinishedTaskSet, finishedSequenceFlowSet, finishedTaskSet);
        return new FlowableViewerVo(finishedTaskSet, finishedSequenceFlowSet, unfinishedTaskSet, rejectedSet);
    }

    /**
     * 获取用户组信息
     *
     * @return candidateGroup
     */
    private List<String> getCandidateGroup() {
        List<String> list = new ArrayList<>();
        SysUserVo user = remoteUserService.getUserInfo();
        if (ObjectUtil.isNotNull(user)) {
            if (ObjectUtil.isNotEmpty(user.getRoleVos())) {
                user.getRoleVos().forEach(roleVo -> list.add(TaskConstants.ROLE_GROUP_PREFIX + roleVo.getRoleCode()));
            }
        }
        return list;
    }
}
