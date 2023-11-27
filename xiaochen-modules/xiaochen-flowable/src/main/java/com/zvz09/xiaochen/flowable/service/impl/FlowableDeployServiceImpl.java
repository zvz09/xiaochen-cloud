package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableDeployVo;
import com.zvz09.xiaochen.flowable.mapper.SysDeployFormMapper;
import com.zvz09.xiaochen.flowable.service.IFlowableDeployService;
import com.zvz09.xiaochen.flowable.utils.ProcessUtils;
import lombok.RequiredArgsConstructor;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
@Service
@RequiredArgsConstructor
public class FlowableDeployServiceImpl implements IFlowableDeployService {

    private final RepositoryService repositoryService;
    private final SysDeployFormMapper sysDeployFormMapper;

    @Override
    public Page<FlowableDeployVo> queryPageList(ProcessQuery processQuery) {
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey()
                .asc();
        // 构建搜索条件
        ProcessUtils.buildProcessSearch(processDefinitionQuery, processQuery);
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return null;
        }
        long offset = processQuery.getPageSize() * (processQuery.getPageNum() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(Math.toIntExact(offset), Math.toIntExact(processQuery.getPageSize()));

        List<FlowableDeployVo> deployVoList = new ArrayList<>(definitionList.size());
        for (ProcessDefinition processDefinition : definitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            FlowableDeployVo vo = FlowableDeployVo.builder()
                    .definitionId(processDefinition.getId())
                    .processKey(processDefinition.getKey())
                    .processName(processDefinition.getName())
                    .version(processDefinition.getVersion())
                    .deploymentId(deploymentId)
                    .suspended(processDefinition.isSuspended())
                    .category(deployment.getCategory())
                    .deploymentTime(deployment.getDeploymentTime())
                    .build();
            deployVoList.add(vo);
        }
        Page<FlowableDeployVo> page = new Page<>(processQuery.getPageNum(), processQuery.getPageSize());
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return page;
    }

    @Override
    public Page<FlowableDeployVo> queryPublishList(String processKey, BasePage basePage) {
        // 创建查询条件
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion()
                .desc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return null;
        }
        // 根据查询条件，查询所有版本
        long offset = basePage.getPageSize() * (basePage.getPageNum() - 1);
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery
                .listPage(Math.toIntExact(offset), Math.toIntExact(basePage.getPageSize()));
        List<FlowableDeployVo> deployVoList = processDefinitionList.stream().map(item -> {
            return FlowableDeployVo.builder()
                    .definitionId(item.getId())
                    .processKey(item.getKey())
                    .processName(item.getName())
                    .version(item.getVersion())
                    .deploymentId(item.getDeploymentId())
                    .suspended(item.isSuspended())
                    .category(item.getCategory())
                    .build();
        }).collect(Collectors.toList());
        Page<FlowableDeployVo> page = new Page<>(basePage.getPageNum(), basePage.getPageSize());
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return page;
    }

    /**
     * 激活或挂起流程
     *
     * @param state        状态
     * @param definitionId 流程定义ID
     */
    @Override
    public void updateState(String definitionId, String state) {
        if (SuspensionState.ACTIVE.toString().equals(state)) {
            // 激活
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
        } else if (SuspensionState.SUSPENDED.toString().equals(state)) {
            // 挂起
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
        }
    }

    @Override
    public String queryBpmnXmlById(String definitionId) {
        InputStream inputStream = repositoryService.getProcessModel(definitionId);
        try {
            return IoUtil.readUtf8(inputStream);
        } catch (IORuntimeException exception) {
            throw new BusinessException("加载xml文件异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> deployIds) {
        for (String deployId : deployIds) {
            repositoryService.deleteDeployment(deployId, true);
            sysDeployFormMapper.delete(new LambdaQueryWrapper<SysDeployForm>().eq(SysDeployForm::getDeployId, deployId));
        }
    }
}
