package com.zvz09.xiaochen.flowable.listener.callback;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.zvz09.xiaochen.flowable.common.constant.ProcessConstants;
import com.zvz09.xiaochen.flowable.common.enums.ProcessStatus;
import com.zvz09.xiaochen.message.api.RemoteEventRemindService;
import com.zvz09.xiaochen.message.api.constant.ActionEnums;
import com.zvz09.xiaochen.message.api.constant.NoticeSourceType;
import com.zvz09.xiaochen.message.api.domain.dto.EventRemindDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowableProcessCallback {

    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final RemoteEventRemindService remoteEventRemindService;

    public void completedCall(FlowableEngineEntityEvent event) {
        String processInstanceId = event.getProcessInstanceId();
        Object variable = runtimeService.getVariable(processInstanceId, ProcessConstants.PROCESS_STATUS_KEY);
        ProcessStatus status = ProcessStatus.getProcessStatus(Convert.toStr(variable));
        if (ObjectUtil.isNotNull(status) && ProcessStatus.RUNNING == status) {
            runtimeService.setVariable(processInstanceId, ProcessConstants.PROCESS_STATUS_KEY, ProcessStatus.COMPLETED.getStatus());
        }
        HistoricProcessInstance hisIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        String message = String.format("【%s】【%s】流程,已结束", processInstanceId, hisIns.getProcessDefinitionName());
        remoteEventRemindService.notice(EventRemindDto.builder().recipientId(hisIns.getStartUserId())
                .action(ActionEnums.FLOWABLE_PROCESS_DELETE).sourceType(NoticeSourceType.FLOWABLE).sourceContent(message).build());
    }
}
