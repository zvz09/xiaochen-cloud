package com.zvz09.xiaochen.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.flowable.domain.dto.SysFormQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysDeployForm;
import com.zvz09.xiaochen.flowable.domain.entity.SysForm;
import com.zvz09.xiaochen.flowable.mapper.SysDeployFormMapper;
import com.zvz09.xiaochen.flowable.mapper.SysFormMapper;
import com.zvz09.xiaochen.flowable.service.ISysFormService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 流程表单Service业务层处理
 *
 * @author zvz09
 * @date 2021-04-03
 */
@Service
@RequiredArgsConstructor
public class SysFormServiceImpl extends ServiceImpl<SysFormMapper, SysForm> implements ISysFormService {

    private final SysDeployFormMapper sysDeployFormMapper;


    /**
     * 查询流程表单
     *
     * @param formId 流程表单ID
     * @return 流程表单
     */
    @Override
    public SysForm selectSysFormById(Long formId) {
        return this.getById(formId);
    }

    /**
     * 查询流程表单列表
     *
     * @param sysForm 流程表单
     * @return 流程表单
     */
    @Override
    public List<SysForm> selectSysFormList(SysForm sysForm) {
        LambdaQueryWrapper<SysForm> queryWrapper = new LambdaQueryWrapper<SysForm>()
                .like(StringUtils.isNotBlank(sysForm.getFormName()), SysForm::getFormName, sysForm.getFormName());
        return this.list(queryWrapper);
    }

    /**
     * 新增流程表单
     *
     * @param sysForm 流程表单
     */
    @Override
    @Transactional
    public void insertSysForm(SysForm sysForm) {
        if (this.count(new LambdaQueryWrapper<SysForm>().eq(SysForm::getFormName, sysForm.getFormName())) > 0) {
            throw new BusinessException("流程表单名称已存在");
        }
        this.save(sysForm);
    }

    /**
     * 修改流程表单
     *
     * @param sysForm 流程表单
     */
    @Override
    @Transactional
    public void updateSysForm(SysForm sysForm) {
        this.update(new LambdaUpdateWrapper<SysForm>().set(SysForm::getFormName, sysForm.getFormName())
                .set(SysForm::getFormContent, sysForm.getFormContent())
                .set(SysForm::getThumbnail, sysForm.getThumbnail())
                .eq(SysForm::getId, sysForm.getId()));
    }

    /**
     * 批量删除流程表单
     *
     * @param formIds 需要删除的流程表单ID
     */
    @Override
    @Transactional
    public void deleteSysFormByIds(Long[] formIds) {
        if (this.sysDeployFormMapper.selectCount(new LambdaQueryWrapper<SysDeployForm>().in(SysDeployForm::getFormId, Arrays.asList(formIds))) > 0) {
            throw new BusinessException("流程表单已被流程部署，无法删除");
        }
        this.removeByIds(Arrays.asList(formIds));
    }

    /**
     * 删除流程表单信息
     *
     * @param formId 流程表单ID
     */
    @Override
    @Transactional
    public void deleteSysFormById(Long formId) {
        if (this.sysDeployFormMapper.selectCount(new LambdaQueryWrapper<SysDeployForm>().eq(SysDeployForm::getFormId, formId)) > 0) {
            throw new BusinessException("流程表单已被流程部署，无法删除");
        }
        this.removeById(formId);
    }

    @Override
    public Page<SysForm> selectSysFormPage(SysFormQuery sysFormQuery) {
        LambdaQueryWrapper<SysForm> queryWrapper = new LambdaQueryWrapper<SysForm>()
                .like(StringUtils.isNotBlank(sysFormQuery.getKeyword()), SysForm::getFormName, sysFormQuery.getKeyword());
        return this.page(new Page<>(sysFormQuery.getPageNum(), sysFormQuery.getPageSize()), queryWrapper);
    }
}
