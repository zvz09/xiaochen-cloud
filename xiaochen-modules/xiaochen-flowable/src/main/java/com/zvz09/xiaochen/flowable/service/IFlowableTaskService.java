package com.zvz09.xiaochen.flowable.service;

import com.zvz09.xiaochen.flowable.domain.dto.FlowableTaskDto;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.runtime.ProcessInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author lizili-YF0033
 */
public interface IFlowableTaskService {
    /**
     * 启动第一个任务
     *
     * @param processInstance 流程实例
     * @param variables       流程参数
     */
    void startFirstTask(ProcessInstance processInstance, Map<String, Object> variables);

    void stopProcess(FlowableTaskDto dto);

    void revokeProcess(FlowableTaskDto dto);

    Map<String, Object> getProcessVariables(String taskId);

    void complete(FlowableTaskDto dto);

    void taskReject(FlowableTaskDto taskBo);

    void taskReturn(FlowableTaskDto dto);

    List<FlowElement> findReturnTaskList(FlowableTaskDto dto);

    void deleteTask(FlowableTaskDto dto);

    void receipted(FlowableTaskDto dto);

    void unReceipted(FlowableTaskDto dto);

    void delegateTask(FlowableTaskDto dto);

    void transferTask(FlowableTaskDto dto);

    InputStream diagram(String processId);
}
