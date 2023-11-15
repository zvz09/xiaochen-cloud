package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.entity.SysForm;
import com.zvz09.xiaochen.flowable.mapper.SysDeployFormMapper;
import com.zvz09.xiaochen.flowable.mapper.SysFormMapper;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import com.zvz09.xiaochen.flowable.utils.ModelUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 流程实例关联表单Service业务层处理
 *
 * @author zvz09
 * @date 2021-04-03
 */
@Service
@RequiredArgsConstructor
public class SysDeployFormServiceImpl extends ServiceImpl<SysDeployFormMapper, SysDeployForm> implements ISysDeployFormService {

    private final SysFormMapper sysFormMapper;

    @Override
    public int insertWfDeployForm(SysDeployForm sysDeployForm) {
        // 删除部署流程和表单的关联关系
        baseMapper.delete(new LambdaQueryWrapper<SysDeployForm>().eq(SysDeployForm::getDeployId, sysDeployForm.getDeployId()));
        // 新增部署流程和表单关系
        return baseMapper.insert(sysDeployForm);
    }

    @Override
    @Transactional
    public boolean saveInternalDeployForm(String deployId, BpmnModel bpmnModel) {
        List<SysDeployForm> deployFormList = new ArrayList<>();
        // 获取开始节点
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new RuntimeException("开始节点不存在，请检查流程设计是否有误！");
        }
        // 保存开始节点表单信息
        SysDeployForm startDeployForm = buildDeployForm(deployId, startEvent);
        if (ObjectUtil.isNotNull(startDeployForm)) {
            deployFormList.add(startDeployForm);
        }
        // 保存用户节点表单信息
        Collection<UserTask> userTasks = ModelUtils.getAllUserTaskEvent(bpmnModel);
        if (CollUtil.isNotEmpty(userTasks)) {
            for (UserTask userTask : userTasks) {
                SysDeployForm userTaskDeployForm = buildDeployForm(deployId, userTask);
                if (ObjectUtil.isNotNull(userTaskDeployForm)) {
                    deployFormList.add(userTaskDeployForm);
                }
            }
        }
        // 批量新增部署流程和表单关联信息
        return this.saveBatch(deployFormList);
    }

    @Override
    public SysDeployForm selectDeployFormByDeployId(String deployId) {
        List<SysDeployForm> sysDeployForms = this.list(new LambdaQueryWrapper<SysDeployForm>().eq(SysDeployForm::getDeployId, deployId));
        return (sysDeployForms != null && !sysDeployForms.isEmpty()) ? sysDeployForms.get(0) : null;
    }

    /**
     * 构建部署表单关联信息对象
     *
     * @param deployId 部署ID
     * @param node     节点信息
     * @return 部署表单关联对象。若无表单信息（formKey），则返回null
     */
    private SysDeployForm buildDeployForm(String deployId, FlowNode node) {
        String formKey = ModelUtils.getFormKey(node);
        if (StringUtils.isEmpty(formKey)) {
            return null;
        }
        Long formId = Convert.toLong(Long.valueOf(formKey));
        SysForm sysForm = sysFormMapper.selectById(formId);
        if (ObjectUtil.isNull(sysForm)) {
            throw new BusinessException("表单信息查询错误");
        }
        SysDeployForm deployForm = new SysDeployForm();
        deployForm.setDeployId(deployId);
        deployForm.setFormId(sysForm.getId());
        deployForm.setFormKey(formKey);
        deployForm.setNodeKey(node.getId());
        deployForm.setFormName(sysForm.getFormName());
        deployForm.setNodeName(node.getName());
        deployForm.setContent(sysForm.getFormContent());
        return deployForm;
    }
}
