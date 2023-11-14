package com.zvz09.xiaochen.flowable.listener.execution.impl;

import com.zvz09.xiaochen.flowable.listener.execution.ICustomizationExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.BaseExecutionListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 示例自定义执行侦听器
 *
 * @author lizili-YF0033
 */
@Slf4j
@Component
public class FlowExecutionListener implements ICustomizationExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        log.info("执行监听器:{}", execution);
    }

    @Override
    public String name() {
        return "示例自定义执行侦听器";
    }

    @Override
    public List<String> typesOfSupport() {
        return Arrays.asList(BaseExecutionListener.EVENTNAME_START, BaseExecutionListener.EVENTNAME_END,
                BaseExecutionListener.EVENTNAME_TAKE);
    }
}
