package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.flowable.common.constant.TaskConstants;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableTaskVo;
import com.zvz09.xiaochen.flowable.service.IFlowableInstanceService;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import com.zvz09.xiaochen.system.api.RemoteAuthorityService;
import com.zvz09.xiaochen.system.api.RemoteDepartmentService;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lizili-YF0033
 */
@Service
@RequiredArgsConstructor
public class FlowableInstanceServiceImpl implements IFlowableInstanceService {

    private final HistoryService historyService;
    private final TaskService taskService;

    private final RemoteUserService remoteUserService;
    private final RemoteAuthorityService remoteAuthorityService;
    private final RemoteDepartmentService remoteDepartmentService;

    private final ISysDeployFormService sysDeployFormService;

    @Override
    public Map<String, Object> queryDetailProcess(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(procInsId)) {
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(procInsId)
                    .orderByHistoricTaskInstanceStartTime().desc()
                    .list();
            List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
            List<FlowableTaskVo> taskVoList = new ArrayList<>(taskInstanceList.size());
            taskInstanceList.forEach(taskInstance -> {
                FlowableTaskVo taskVo = new FlowableTaskVo();
                taskVo.setProcDefId(taskInstance.getProcessDefinitionId());
                taskVo.setTaskId(taskInstance.getId());
                taskVo.setTaskDefKey(taskInstance.getTaskDefinitionKey());
                taskVo.setTaskName(taskInstance.getName());
                taskVo.setCreateTime(taskInstance.getCreateTime());
                taskVo.setClaimTime(taskInstance.getClaimTime());
                taskVo.setFinishTime(taskInstance.getEndTime());
                if (StringUtils.isNotBlank(taskInstance.getAssignee())) {
                    SysUser user = remoteUserService.getById(Long.parseLong(taskInstance.getAssignee()));
                    taskVo.setAssigneeId(user.getId());
                    taskVo.setAssigneeName(user.getNickName());
                    // taskVo.setDeptName(user.getDept().getDeptName());
                }
                // 展示审批人员
                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(taskInstance.getId());
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            SysUser user = remoteUserService.getById(Long.parseLong(identityLink.getUserId()));
                            stringBuilder.append(user.getNickName()).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(TaskConstants.ROLE_GROUP_PREFIX)) {
                                String authorityCode = StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.ROLE_GROUP_PREFIX);
                                SysAuthority role = remoteAuthorityService.getByAuthorityCode(authorityCode);
                                stringBuilder.append(role.getAuthorityName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstants.DEPT_GROUP_PREFIX)) {
                                Long deptId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.DEPT_GROUP_PREFIX));
                                SysDepartment dept = remoteDepartmentService.getById(deptId);
                                stringBuilder.append(dept.getDeptName()).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    taskVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                if (ObjectUtil.isNotNull(taskInstance.getDurationInMillis())) {
                    taskVo.setDuration(DateUtil.formatBetween(taskInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(commentList)) {
                    List<Comment> comments = new ArrayList<>();
                    // commentList.stream().filter(comment -> taskInstance.getId().equals(comment.getTaskId())).collect(Collectors.toList());
                    for (Comment comment : commentList) {
                        if (comment.getTaskId().equals(taskInstance.getId())) {
                            comments.add(comment);
                            // taskVo.setComment(WfCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                        }
                    }
                    taskVo.setCommentList(comments);
                }
                taskVoList.add(taskVo);
            });
            map.put("flowList", taskVoList);
//            // 查询当前任务是否完成
//            List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInsId).list();
//            if (CollectionUtils.isNotEmpty(taskList)) {
//                map.put("finished", true);
//            } else {
//                map.put("finished", false);
//            }
        }
        // 第一次申请获取初始化表单
        if (StringUtils.isNotBlank(deployId)) {
            SysDeployForm formVo = sysDeployFormService.selectDeployFormByDeployId(deployId);
            if (Objects.isNull(formVo)) {
                throw new BusinessException("请先配置流程表单");
            }
            map.put("formData", JacksonUtil.readValue(formVo.getContent(), Map.class));
        }
        return map;
    }
}
