package com.zvz09.xiaochen.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableCopyDto;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableTaskDto;
import com.zvz09.xiaochen.flowable.domain.entity.FlowableCopy;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableCopyVo;
import com.zvz09.xiaochen.flowable.mapper.FlowableCopyMapper;
import com.zvz09.xiaochen.flowable.service.IFlowableCopyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zvz09
 */
@Service
@RequiredArgsConstructor
public class FlowableCopyServiceImpl extends ServiceImpl<FlowableCopyMapper, FlowableCopy> implements IFlowableCopyService {

    private final HistoryService historyService;

    @Override
    @Transactional
    public Boolean makeCopy(FlowableTaskDto dto) {
        if (dto.getCopyUserIds() == null || dto.getCopyUserIds().isEmpty()) {
            // 若抄送用户为空，则不需要处理，返回成功
            return true;
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(dto.getProcInsId()).singleResult();
        List<FlowableCopy> copyList = new ArrayList<>(dto.getCopyUserIds().size());
        Long originatorId = SecurityContextHolder.getUserId();
        String originatorName = SecurityContextHolder.getUserName();
        String title = historicProcessInstance.getProcessDefinitionName() + "-" + dto.getTaskName();
        for (Long userId : dto.getCopyUserIds()) {
            FlowableCopy copy = new FlowableCopy();
            copy.setTitle(title);
            copy.setProcessId(historicProcessInstance.getProcessDefinitionId());
            copy.setProcessName(historicProcessInstance.getProcessDefinitionName());
            copy.setDeploymentId(historicProcessInstance.getDeploymentId());
            copy.setInstanceId(dto.getProcInsId());
            copy.setTaskId(dto.getTaskId());
            copy.setUserId(userId);
            copy.setOriginatorId(originatorId);
            copy.setOriginatorName(originatorName);
            copyList.add(copy);
        }
        return saveBatch(copyList);
    }

    @Override
    public FlowableCopyVo queryById(Long copyId) {
        FlowableCopy flowableCopy = getById(copyId);
        if (flowableCopy == null) {
            return null;
        }
        return new FlowableCopyVo(flowableCopy);
    }

    @Override
    public Page<FlowableCopyVo> selectPageList(FlowableCopyDto flowableCopyDto, BasePage basePage) {
        LambdaQueryWrapper<FlowableCopy> lqw = buildQueryWrapper(flowableCopyDto);
        lqw.orderByDesc(FlowableCopy::getCreatedAt);
        Page<FlowableCopy> result = baseMapper.selectPage(new Page<>(basePage.getPage(), basePage.getPageSize()), lqw);
        Page<FlowableCopyVo> flowableCopyVoPage = new Page<>(basePage.getPage(), basePage.getPageSize());
        List<FlowableCopyVo> voList = new ArrayList<>();
        result.getRecords().forEach(flowableCopy -> {
            voList.add(new FlowableCopyVo(flowableCopy));
        });
        flowableCopyVoPage.setRecords(voList);
        return flowableCopyVoPage;
    }

    @Override
    public List<FlowableCopyVo> selectList(FlowableCopyDto flowableCopyDto) {
        LambdaQueryWrapper<FlowableCopy> lqw = buildQueryWrapper(flowableCopyDto);
        lqw.orderByDesc(FlowableCopy::getCreatedAt);
        List<FlowableCopy> flowableCopyList = baseMapper.selectList(lqw);

        List<FlowableCopyVo> flowableCopyVoList = new ArrayList<>();
        flowableCopyList.forEach(flowableCopy -> {
            flowableCopyVoList.add(new FlowableCopyVo(flowableCopy));
        });
        return flowableCopyVoList;
    }

    private LambdaQueryWrapper<FlowableCopy> buildQueryWrapper(FlowableCopyDto dto) {
        LambdaQueryWrapper<FlowableCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, FlowableCopy::getUserId, dto.getUserId());
        lqw.like(StringUtils.isNotBlank(dto.getProcessName()), FlowableCopy::getProcessName, dto.getProcessName());
        lqw.like(StringUtils.isNotBlank(dto.getOriginatorName()), FlowableCopy::getOriginatorName, dto.getOriginatorName());
        return lqw;
    }
}
