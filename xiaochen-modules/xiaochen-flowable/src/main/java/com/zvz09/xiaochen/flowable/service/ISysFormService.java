package com.zvz09.xiaochen.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.flowable.domain.dto.SysFormQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysForm;

import java.util.List;


/**
 * 表单
 *
 * @author Tony
 * @date 2021-04-03
 */
public interface ISysFormService extends IService<SysForm> {
    /**
     * 查询流程表单
     *
     * @param formId 流程表单ID
     * @return 流程表单
     */
    SysForm selectSysFormById(Long formId);

    /**
     * 查询流程表单列表
     *
     * @param sysForm 流程表单
     * @return 流程表单集合
     */
    List<SysForm> selectSysFormList(SysForm sysForm);

    /**
     * 新增流程表单
     *
     * @param sysForm 流程表单
     */
    void insertSysForm(SysForm sysForm);

    /**
     * 修改流程表单
     *
     * @param sysForm 流程表单
     */
    void updateSysForm(SysForm sysForm);

    /**
     * 批量删除流程表单
     *
     * @param formIds 需要删除的流程表单ID
     */
    void deleteSysFormByIds(Long[] formIds);

    /**
     * 删除流程表单信息
     *
     * @param formId 流程表单ID
     */
    void deleteSysFormById(Long formId);

    Page<SysForm> selectSysFormPage(SysFormQuery sysFormQuery);
}
