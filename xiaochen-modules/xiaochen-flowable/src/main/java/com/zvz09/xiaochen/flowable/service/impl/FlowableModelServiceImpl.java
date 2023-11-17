package com.zvz09.xiaochen.flowable.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.flowable.common.constant.ProcessConstants;
import com.zvz09.xiaochen.flowable.common.enums.FormType;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableMetaInfoDto;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelDto;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableModelQuery;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableModelVo;
import com.zvz09.xiaochen.flowable.domain.vo.ListenerVo;
import com.zvz09.xiaochen.flowable.listener.execution.ICustomizationExecutionListener;
import com.zvz09.xiaochen.flowable.listener.task.ICustomizationTaskListener;
import com.zvz09.xiaochen.flowable.service.IFlowableModelService;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import com.zvz09.xiaochen.flowable.utils.ModelUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zvz09
 */
@Service
@RequiredArgsConstructor
public class FlowableModelServiceImpl implements IFlowableModelService {

    private final RepositoryService repositoryService;
    private final ISysDeployFormService sysDeployFormService;

    private final ApplicationContext applicationContext;

    @Override
    public Page<FlowableModelVo> list(FlowableModelQuery flowableModelQuery) {
        ModelQuery modelQuery = buildModelQuery(flowableModelQuery);
        // 执行查询
        long pageTotal = modelQuery.count();
        if (pageTotal <= 0) {
            return new Page<>();
        }
        long offset = flowableModelQuery.getPageSize() * (flowableModelQuery.getPageNum() - 1);
        List<Model> modelList = modelQuery.listPage(Math.toIntExact(offset), Math.toIntExact(flowableModelQuery.getPageSize()));
        List<FlowableModelVo> modelVoList = buildFlowableModelVos(modelList);
        Page<FlowableModelVo> page = new Page<>(flowableModelQuery.getPageNum(), flowableModelQuery.getPageSize());
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return page;
    }

    @Override
    public List<FlowableModelVo> listAll(FlowableModelQuery flowableModelQuery) {
        ModelQuery modelQuery = buildModelQuery(flowableModelQuery);
        List<Model> modelList = modelQuery.list();
        return buildFlowableModelVos(modelList);
    }

    @Override
    public Page<FlowableModelVo> historyList(@NotNull String modelKey, BasePage basePage) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
                .modelKey(modelKey)
                .orderByModelVersion()
                .desc();
        // 执行查询（不显示最新版，-1）
        long pageTotal = modelQuery.count() - 1;
        if (pageTotal <= 0) {
            return new Page<>();
        }
        // offset+1，去掉最新版
        int offset = (int) (1 + basePage.getPageSize() * (basePage.getPageNum() - 1));
        List<Model> modelList = modelQuery.listPage(offset, Math.toIntExact(basePage.getPageSize()));
        List<FlowableModelVo> modelVoList = buildFlowableModelVos(modelList);
        Page<FlowableModelVo> page = new Page<>(basePage.getPageNum(), basePage.getPageSize());
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return page;
    }

    @Override
    public FlowableModelVo getModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new BusinessException("流程模型不存在！");
        }
        // 获取流程图
        String bpmnXml = this.queryBpmnXmlById(modelId);
        FlowableModelVo modelVo = buildFlowableModelVo(model);
        FlowableMetaInfoDto metaInfo = JacksonUtil.readValue(model.getMetaInfo(), FlowableMetaInfoDto.class);
        if (FormType.PROCESS.getType().equals(metaInfo.getFormType())) {
            //TODO 表单数据
            /*  modelVo.setContent(wfFormVo.getContent());*/
        }
        return modelVo;
    }

    @Override
    public String queryBpmnXmlById(String modelId) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        return StrUtil.utf8Str(bpmnBytes);
    }

    @Override
    public void insertModel(FlowableModelDto flowableModelDto) {
        Model model = repositoryService.newModel();
        model.setName(flowableModelDto.getModelName());
        model.setKey(flowableModelDto.getModelKey());
        model.setCategory(flowableModelDto.getCategory());
        String metaInfo = buildMetaInfo(new FlowableMetaInfoDto(), flowableModelDto.getDescription());
        model.setMetaInfo(metaInfo);
        // 保存流程模型
        repositoryService.saveModel(model);
    }

    @Override
    public void updateModel(FlowableModelDto flowableModelDto) {
        // 根据模型Key查询模型信息
        Model model = repositoryService.getModel(flowableModelDto.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new BusinessException("流程模型不存在！");
        }
        model.setCategory(flowableModelDto.getCategory());
        FlowableMetaInfoDto metaInfoDto = JacksonUtil.readValue(model.getMetaInfo(), FlowableMetaInfoDto.class);
        String metaInfo = buildMetaInfo(metaInfoDto, flowableModelDto.getDescription());
        model.setMetaInfo(metaInfo);
        // 保存流程模型
        repositoryService.saveModel(model);
    }

    @Override
    public void saveModel(FlowableModelDto flowableModelDto) {
        // 查询模型信息
        Model model = repositoryService.getModel(flowableModelDto.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new BusinessException("流程模型不存在！");
        }
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(flowableModelDto.getBpmnXml());
        if (ObjectUtil.isEmpty(bpmnModel)) {
            throw new BusinessException("获取模型设计失败！");
        }
        String processName = bpmnModel.getMainProcess().getName();
        // 获取开始节点
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new BusinessException("开始节点不存在，请检查流程设计是否有误！");
        }
        // 获取开始节点配置的表单Key
        String customizationFormKey = startEvent.getAttributeValue("http://flowable.org/bpmn", "customizationFormKey");
        if (StringUtils.isBlank(customizationFormKey)) {
            throw new BusinessException("请配置流程表单");
        }
        Model newModel;
        if (Boolean.TRUE.equals(flowableModelDto.getNewVersion())) {
            newModel = repositoryService.newModel();
            newModel.setName(processName);
            newModel.setKey(model.getKey());
            newModel.setCategory(model.getCategory());
            newModel.setMetaInfo(model.getMetaInfo());
            newModel.setVersion(model.getVersion() + 1);
        } else {
            newModel = model;
            // 设置流程名称
            newModel.setName(processName);
        }
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        byte[] bpmnXmlBytes = StringUtils.getBytes(flowableModelDto.getBpmnXml(), StandardCharsets.UTF_8);
        repositoryService.addModelEditorSource(newModel.getId(), bpmnXmlBytes);
    }

    @Override
    public void latestModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new BusinessException("流程模型不存在！");
        }
        Integer latestVersion = repositoryService.createModelQuery()
                .modelKey(model.getKey())
                .latestVersion()
                .singleResult()
                .getVersion();
        if (model.getVersion().equals(latestVersion)) {
            throw new BusinessException("当前版本已是最新版！");
        }
        // 获取 BPMN XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        Model newModel = repositoryService.newModel();
        newModel.setName(model.getName());
        newModel.setKey(model.getKey());
        newModel.setCategory(model.getCategory());
        newModel.setMetaInfo(model.getMetaInfo());
        newModel.setVersion(latestVersion + 1);
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        repositoryService.addModelEditorSource(newModel.getId(), bpmnBytes);
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        ids.forEach(id -> {
            Model model = repositoryService.getModel(id);
            if (ObjectUtil.isNull(model)) {
                throw new BusinessException("流程模型不存在！");
            }
            repositoryService.deleteModel(id);
        });
    }

    @Override
    public boolean deployModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new BusinessException("流程模型不存在！");
        }
        // 获取流程图
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        String bpmnXml = StringUtils.toEncodedString(bpmnBytes, StandardCharsets.UTF_8);
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bpmnXml);
        String processName = model.getName() + ProcessConstants.SUFFIX;
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .key(model.getKey())
                .category(model.getCategory())
                .addBytes(processName, bpmnBytes)
                .deploy();
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        // 修改流程定义的分类，便于搜索流程
        repositoryService.setProcessDefinitionCategory(procDef.getId(), model.getCategory());
        // 保存部署表单
        return sysDeployFormService.saveInternalDeployForm(deployment.getId(), bpmnModel);
    }

    @Override
    @Cacheable("CustomizationTaskListener")
    public List<ListenerVo> listTaskListener() {
        Map<String, ICustomizationTaskListener> serviceMap = applicationContext.getBeansOfType(ICustomizationTaskListener.class);
        List<ListenerVo> taskListenerVos = new ArrayList<>();
        if (!serviceMap.isEmpty()) {
            serviceMap.forEach((k, v) -> {
                ListenerVo listenerVo = new ListenerVo();
                listenerVo.setName(v.name());
                listenerVo.setTypesOfSupport(v.typesOfSupport());
                listenerVo.setClassPath(v.getClass().getName());
                taskListenerVos.add(listenerVo);
            });
        }
        return taskListenerVos;
    }

    @Override
    @Cacheable("CustomizationExecutionListener")
    public List<ListenerVo> listExecutionListener() {
        List<ListenerVo> executionListenerVos = new ArrayList<>();
        Map<String, ICustomizationExecutionListener> serviceMap = applicationContext.getBeansOfType(ICustomizationExecutionListener.class);
        if (!serviceMap.isEmpty()) {
            serviceMap.forEach((k, v) -> {
                ListenerVo listenerVo = new ListenerVo();
                listenerVo.setName(v.name());
                listenerVo.setTypesOfSupport(v.typesOfSupport());
                listenerVo.setClassPath(v.getClass().getName());
                executionListenerVos.add(listenerVo);
            });
        }
        return executionListenerVos;
    }

    @NotNull
    private static List<FlowableModelVo> buildFlowableModelVos(List<Model> modelList) {
        List<FlowableModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            FlowableModelVo modelVo = buildFlowableModelVo(model);
            modelVoList.add(modelVo);
        });
        return modelVoList;
    }

    @NotNull
    private static FlowableModelVo buildFlowableModelVo(Model model) {
        FlowableModelVo modelVo = new FlowableModelVo();
        modelVo.setModelId(model.getId());
        modelVo.setModelName(model.getName());
        modelVo.setModelKey(model.getKey());
        modelVo.setCategory(model.getCategory());
        modelVo.setCreateTime(model.getCreateTime());
        modelVo.setVersion(model.getVersion());
        FlowableMetaInfoDto metaInfo = JacksonUtil.readValue(model.getMetaInfo(), FlowableMetaInfoDto.class);
        if (metaInfo != null) {
            modelVo.setDescription(metaInfo.getDescription());
            modelVo.setFormType(metaInfo.getFormType());
            modelVo.setFormId(metaInfo.getFormId());
        }
        return modelVo;
    }

    private ModelQuery buildModelQuery(FlowableModelQuery flowableModelQuery) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByCreateTime().desc();
        if (flowableModelQuery == null) {
            return modelQuery;
        }
        // 构建查询条件
        if (StringUtils.isNotBlank(flowableModelQuery.getModelKey())) {
            modelQuery.modelKey(flowableModelQuery.getModelKey());
        }
        if (StringUtils.isNotBlank(flowableModelQuery.getModelName())) {
            modelQuery.modelNameLike("%" + flowableModelQuery.getModelName() + "%");
        }
        if (StringUtils.isNotBlank(flowableModelQuery.getCategory())) {
            modelQuery.modelCategory(flowableModelQuery.getCategory());
        }
        return modelQuery;
    }

    /**
     * 构建模型扩展信息
     *
     * @return
     */
    private String buildMetaInfo(FlowableMetaInfoDto flowableMetaInfoDto, String description) {
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StringUtils.isNotEmpty(description)) {
            flowableMetaInfoDto.setDescription(description);
        }
        if (StringUtils.isNotEmpty(flowableMetaInfoDto.getCreateUser())) {
            flowableMetaInfoDto.setCreateUser(SecurityContextHolder.getUserName());
        }
        return JacksonUtil.writeValueAsString(flowableMetaInfoDto);
    }
}
