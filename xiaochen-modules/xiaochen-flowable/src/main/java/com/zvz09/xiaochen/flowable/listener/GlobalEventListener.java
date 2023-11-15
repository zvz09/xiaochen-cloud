package com.zvz09.xiaochen.flowable.listener;

import com.zvz09.xiaochen.flowable.listener.callback.FlowableEntityCallback;
import com.zvz09.xiaochen.flowable.listener.callback.FlowableProcessCallback;
import com.zvz09.xiaochen.flowable.listener.callback.FlowableTaskCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.springframework.stereotype.Component;

/**
 * Flowable 全局监听器
 *
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalEventListener extends AbstractFlowableEngineEventListener {


    private final FlowableTaskCallback flowableTaskCallback;
    private final FlowableEntityCallback flowableEntityCallback;
    private final FlowableProcessCallback flowableProcessCallback;

    /**
     * @param event
     */
    @Override
    protected void entityInitialized(FlowableEngineEntityEvent event) {
        flowableEntityCallback.initializedCall(event);
        super.entityInitialized(event);
    }

    /**
     * 用户任务分配/签收完成
     *
     * @param event
     */
    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
        flowableTaskCallback.assignedCall(event);
        super.taskAssigned(event);
    }

    /**
     * 用户审批任务
     *
     * @param event
     */
    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        flowableTaskCallback.completedCall(event);
        super.taskCompleted(event);
    }

    @Override
    protected void processCompletedWithTerminateEnd(FlowableEngineEntityEvent event) {
        log.info("processCompletedWithTerminateEnd:{}", event.toString());
        super.processCompletedWithTerminateEnd(event);
    }

    /**
     * 流程结束监听器
     */
    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        flowableProcessCallback.completedCall(event);
        super.processCompleted(event);
    }
}

