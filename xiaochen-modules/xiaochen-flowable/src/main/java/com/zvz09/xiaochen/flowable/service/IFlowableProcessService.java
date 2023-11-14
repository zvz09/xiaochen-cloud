package com.zvz09.xiaochen.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDetailVo;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableTaskVo;
import com.zvz09.xiaochen.flowable.domain.vo.SysFormVo;

import java.util.Map;

/**
 * @author 18237
 */
public interface IFlowableProcessService {
    Page<FlowableDeployVo> selectPageStartProcessList(ProcessQuery processQuery);

    Page<FlowableTaskVo> selectPageOwnProcessList(ProcessQuery processQuery);

    Page<FlowableTaskVo> selectPageTodoProcessList(ProcessQuery processQuery);

    Page<FlowableTaskVo> selectPageReceiptedProcessList(ProcessQuery processQuery);

    Page<FlowableTaskVo>  selectPageFinishedProcessList(ProcessQuery processQuery);

    SysFormVo selectFormContent(String definitionId, String deployId, String procInsId);

    void startProcessByDefId(String processDefId, Map<String, Object> variables);

    void deleteProcessByIds(String[] instanceIds);

    String queryBpmnXmlById(String processDefId);

    FlowableDetailVo queryProcessDetail(String procInsId, String taskId);
}
