package com.zvz09.xiaochen.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableCopyQuery;
import com.zvz09.xiaochen.flowable.domain.dto.FlowableTaskDto;
import com.zvz09.xiaochen.flowable.domain.vo.FlowableCopyVo;

import java.util.List;

/**
 * @author zvz09
 */
public interface IFlowableCopyService {
    Boolean makeCopy(FlowableTaskDto dto);

    /**
     * 查询流程抄送
     *
     * @param copyId 流程抄送主键
     * @return 流程抄送
     */
    FlowableCopyVo queryById(Long copyId);

    /**
     * 查询流程抄送列表
     *
     * @param flowableCopyDto 流程抄送
     * @return 流程抄送集合
     */
    Page<FlowableCopyVo> selectPageList(FlowableCopyQuery flowableCopyQuery);

    /**
     * 查询流程抄送列表
     *
     * @param flowableCopyQuery 流程抄送
     * @return 流程抄送集合
     */
    List<FlowableCopyVo> selectList(FlowableCopyQuery flowableCopyQuery);

}
