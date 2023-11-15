package com.zvz09.xiaochen.flowable.listener.execution;

import org.flowable.engine.delegate.ExecutionListener;

import java.util.List;

/**
 * 自定义执行侦听器
 *
 * @author zvz09
 */
public interface ICustomizationExecutionListener extends ExecutionListener {


    String name();

    /**
     * 启动(start)、结束(end)、在用(take)
     *
     * @return
     */
    List<String> typesOfSupport();
}
