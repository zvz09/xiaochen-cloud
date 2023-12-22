package com.zvz09.xiaochen.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelDto;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelQuery;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableModelVo;
import com.zvz09.xiaochen.flowable.domain.vo.ListenerVo;

import java.util.Collection;
import java.util.List;

/**
 * @author zvz09
 * @date 2021-04-03 14:41
 */
public interface IFlowableModelService {

    /**
     * 查询流程模型列表
     */
    Page<FlowableModelVo> list(FlowableModelQuery flowableModelQuery);

    /**
     * 查询流程模型列表
     */
    List<FlowableModelVo> listAll(FlowableModelQuery flowableModelQuery);

    /**
     * 查询流程模型列表
     */
    Page<FlowableModelVo> historyList(String modelKey, BasePage basePage);

    /**
     * 查询流程模型详情信息
     */
    FlowableModelVo getModel(String modelId);

    /**
     * 查询流程BpmnXml详细信息
     */
    String queryBpmnXmlById(String modelId);

    /**
     * 新增模型信息
     */
    void insertModel(FlowableModelDto flowableModelDto);

    /**
     * 修改模型信息
     */
    void updateModel(FlowableModelDto flowableModelDto);

    /**
     * 保存流程模型信息
     */
    void saveModel(FlowableModelDto flowableModelDto);

    /**
     * 设为最新流程模型
     */
    void latestModel(String modelId);

    /**
     * 删除流程模型
     */
    void deleteByIds(Collection<String> ids);

    /**
     * 部署流程模型
     */
    boolean deployModel(String modelId);

    List<ListenerVo> listTaskListener();

    List<ListenerVo> listExecutionListener();
}
