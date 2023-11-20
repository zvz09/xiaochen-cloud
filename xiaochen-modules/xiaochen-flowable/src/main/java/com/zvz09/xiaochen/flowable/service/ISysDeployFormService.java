package com.zvz09.xiaochen.flowable.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import org.flowable.bpmn.model.BpmnModel;

/**
 * 流程实例关联表单Service接口
 *
 * @author zvz09
 * @date 2021-04-03
 */
public interface ISysDeployFormService extends IService<SysDeployForm> {
    /**
     * 新增流程实例关联表单
     *
     * @param sysDeployForm 流程实例关联表单
     * @return 结果
     */
    int insertWfDeployForm(SysDeployForm sysDeployForm);

    /**
     * 保存流程实例关联表单
     *
     * @param deployId  部署ID
     * @param bpmnModel bpmnModel对象
     * @return
     */
    boolean saveInternalDeployForm(String deployId, BpmnModel bpmnModel);

    /**
     * 查询流程挂着的表单
     *
     * @param deployId
     * @return
     */
    SysDeployForm selectDeployFormByDeployId(String deployId);
}
