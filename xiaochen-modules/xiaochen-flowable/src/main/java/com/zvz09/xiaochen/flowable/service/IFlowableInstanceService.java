package com.zvz09.xiaochen.flowable.service;

import java.util.Map;

/**
 * @author zvz09
 */
public interface IFlowableInstanceService {

    /**
     * 查询流程详情信息
     *
     * @param procInsId 流程实例ID
     * @param deployId  流程部署ID
     */
    Map<String, Object> queryDetailProcess(String procInsId, String deployId);
}
