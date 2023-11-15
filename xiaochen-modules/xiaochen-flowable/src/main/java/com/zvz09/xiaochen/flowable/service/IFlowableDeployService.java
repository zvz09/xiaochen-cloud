package com.zvz09.xiaochen.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;

import java.util.List;

/**
 * @author zvz09
 */
public interface IFlowableDeployService {
    Page<FlowableDeployVo> queryPageList(ProcessQuery processQuery);

    Page<FlowableDeployVo> queryPublishList(String processKey, BasePage basePage);

    void updateState(String definitionId, String state);

    String queryBpmnXmlById(String definitionId);

    void deleteByIds(List<String> deployIds);
}
