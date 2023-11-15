package com.zvz09.xiaochen.flowable.listener.task.impl;

import com.zvz09.xiaochen.flowable.listener.task.ICustomizationTaskListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.BaseTaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 示例自定义任务侦听器
 *
 * @author zvz09
 */
@Slf4j
@Component
public class TestTaskListener implements ICustomizationTaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("示例自定义任务侦听器:{}", delegateTask);
    }

    @Override
    public String name() {
        return "示例自定义任务侦听器";
    }

    @Override
    public List<String> typesOfSupport() {
        return Arrays.asList(BaseTaskListener.EVENTNAME_CREATE, BaseTaskListener.EVENTNAME_ASSIGNMENT,
                BaseTaskListener.EVENTNAME_DELETE, BaseTaskListener.EVENTNAME_COMPLETE);
    }
}
