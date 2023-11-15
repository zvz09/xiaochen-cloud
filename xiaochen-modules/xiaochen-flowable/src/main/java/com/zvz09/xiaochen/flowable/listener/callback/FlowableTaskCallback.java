package com.zvz09.xiaochen.flowable.listener.callback;

import com.zvz09.xiaochen.message.api.RemoteEventRemindService;
import com.zvz09.xiaochen.message.api.constant.ActionEnums;
import com.zvz09.xiaochen.message.api.constant.NoticeSourceType;
import com.zvz09.xiaochen.message.api.domain.dto.EventRemindDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowableTaskCallback {

    private final HistoryService historyService;

    private final RemoteEventRemindService remoteEventRemindService;

    @Async
    public void assignedCall(FlowableEngineEntityEvent event) {
        TaskEntityImpl taskEntity = (TaskEntityImpl) event.getEntity();

        String processInstanceId = taskEntity.getProcessInstanceId();
        HistoricProcessInstance hisIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        String message = String.format("【%s】【%s】流程，【%s】节点已被委派", processInstanceId, hisIns.getProcessDefinitionName(), taskEntity.getName());

        remoteEventRemindService.notice(EventRemindDto.builder().recipientId(hisIns.getStartUserId())
                .action(ActionEnums.FLOWABLE_TASK_ASSIGNMENT).sourceType(NoticeSourceType.FLOWABLE).sourceContent(message).build());
    }

    @Async
    public void completedCall(FlowableEngineEntityEvent event) {
        TaskEntityImpl taskEntity = (TaskEntityImpl) event.getEntity();

        String processInstanceId = taskEntity.getProcessInstanceId();
        HistoricProcessInstance hisIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        String message = String.format("【%s】【%s】流程，【%s】节点已审批完成", processInstanceId, hisIns.getProcessDefinitionName(), taskEntity.getName());

        remoteEventRemindService.notice(EventRemindDto.builder().recipientId(hisIns.getStartUserId())
                .action(ActionEnums.FLOWABLE_TASK_COMPLETE).sourceType(NoticeSourceType.FLOWABLE).sourceContent(message).build());
    }

    @Async
    public void createdCall(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl) event;
        TaskEntityImpl taskEntity = (TaskEntityImpl) eventImpl.getEntity();

    }
}
