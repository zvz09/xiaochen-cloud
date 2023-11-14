package com.zvz09.xiaochen.flowable.domain.vo;

import com.zvz09.xiaochen.flowable.domain.entity.FlowableCopy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlowableCopyVo {
    /**
     * 抄送主键
     */
    private Long copyId;

    /**
     * 抄送标题
     */
    private String title;

    /**
     * 流程主键
     */
    private String processId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程分类主键
     */
    private String categoryId;

    /**
     * 部署主键
     */
    private String deploymentId;

    /**
     * 流程实例主键
     */
    private String instanceId;

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 发起人Id
     */
    private Long originatorId;

    /**
     * 发起人名称
     */
    private String originatorName;

    /**
     * 抄送时间（创建时间）
     */
    private LocalDateTime createTime;

    public FlowableCopyVo(FlowableCopy flowableCopy) {

        this.copyId = flowableCopy.getId();
        this.title = flowableCopy.getTitle();
        this.processId = flowableCopy.getProcessId();
        this.processName = flowableCopy.getProcessName();
        this.categoryId = flowableCopy.getCategoryId();
        this.deploymentId = flowableCopy.getDeploymentId();
        this.instanceId = flowableCopy.getInstanceId();
        this.taskId = flowableCopy.getTaskId();
        this.userId = flowableCopy.getUserId();
        this.originatorId = flowableCopy.getOriginatorId();
        this.originatorName = flowableCopy.getOriginatorName();
        this.createTime = flowableCopy.getCreatedAt();
    }
}
