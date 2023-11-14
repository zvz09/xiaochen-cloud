package com.zvz09.xiaochen.flowable.domain.vo;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.List;

/**
 * 流程详情视图对象
 * @author 18237
 */
@Data
public class FlowableDetailVo {
    /**
     * 任务表单信息
     */
    private SysFormVo taskFormData;

    /**
     * 历史流程节点信息
     */
    private List<FlowableProcNodeVo> historyProcNodeList;

    /**
     * 流程表单列表
     */
    private List<SysFormVo> processFormList;

    /**
     * 流程XML
     */
    private String bpmnXml;

    private FlowableViewerVo flowViewer;

    /**
     * 是否存在任务表单信息
     * @return true:存在；false:不存在
     */
    public Boolean isExistTaskForm() {
        return ObjectUtil.isNotEmpty(this.taskFormData);
    }
}
