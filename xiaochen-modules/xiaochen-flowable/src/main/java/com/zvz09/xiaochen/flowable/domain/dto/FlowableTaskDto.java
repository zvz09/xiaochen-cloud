package com.zvz09.xiaochen.flowable.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zvz09
 */
@Data
public class FlowableTaskDto {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 任务意见
     */
    private String comment;
    /**
     * 流程实例Id
     */
    private String procInsId;
    /**
     * 节点
     */
    private String targetKey;
    /**
     * 流程变量信息
     */
    private Map<String, Object> variables;
    /**
     * 审批人
     */
    private String assignee;
    /**
     * 候选人
     */
    private List<String> candidateUsers;
    /**
     * 审批组
     */
    private List<String> candidateGroups;
    /**
     * 抄送用户Id
     */
    private List<Long> copyUserIds;
    /**
     * 下一节点审批人
     */
    private List<Long> nextUserIds;
}
