package com.zvz09.xiaochen.flowable.listener.task;

import org.flowable.engine.delegate.TaskListener;

import java.util.List;

/**
 * 自定义任务侦听器
 *
 * @author lizili-YF0033
 */
public interface ICustomizationTaskListener extends TaskListener {

    String name();

    /**
     * create（创建）:在任务被创建且所有的任务属性设置完成后才触发
     * assignment（指派）：在任务被分配给某个办理人之后触发
     * complete（完成）：在配置了监听器的上一个任务完成时触发
     * delete（删除）：在任务即将被删除前触发。请注意任务由completeTask正常完成时也会触发
     *
     * @return
     */
    List<String> typesOfSupport();
}
