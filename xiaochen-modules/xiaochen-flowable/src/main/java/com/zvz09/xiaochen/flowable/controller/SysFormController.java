package com.zvz09.xiaochen.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.log.annotation.BizNo;
import com.zvz09.xiaochen.flowable.domain.dto.SysFormQuery;
import com.zvz09.xiaochen.flowable.domain.entity.SysForm;
import com.zvz09.xiaochen.flowable.service.ISysDeployFormService;
import com.zvz09.xiaochen.flowable.service.ISysFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 流程表单Controller
 *
 * @author zvz09
 * @date 2021-04-03
 */
@Slf4j
@Tag(name = "流程表单")
@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/form")
public class SysFormController {
    private final ISysFormService sysFormService;

    @Autowired
    private ISysDeployFormService sysDeployFormService;

    /**
     * 查询流程表单列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询流程表单列表")
    public ApiResult list(@RequestBody SysFormQuery sysFormQuery) {
        Page<SysForm> page = sysFormService.selectSysFormPage(sysFormQuery);
        return ApiResult.success(page);
    }

    /**
     * 查询所有流程表单
     *
     * @param sysForm
     * @return
     */
    @PostMapping("/list")
    @Operation(summary = "查询所有流程表单")
    public ApiResult formList(SysForm sysForm) {
        List<SysForm> list = sysFormService.selectSysFormList(sysForm);
        return ApiResult.success(list);
    }

    /**
     * 获取流程表单详细信息
     */
    @GetMapping(value = "/{formId}/info")
    @Operation(summary = "获取流程表单详细信息")
    public ApiResult getInfo(@PathVariable(value = "formId") Long formId) {
        return ApiResult.success(sysFormService.selectSysFormById(formId));
    }

    /**
     * 新增流程表单
     */
    @PostMapping()
    @Operation(summary = "新增流程表单")
    @BizNo(spEl = "{#sysForm.formName}")
    public ApiResult add(@RequestBody SysForm sysForm) {
        sysFormService.insertSysForm(sysForm);
        return ApiResult.success();
    }

    /**
     * 修改流程表单
     */
    @PutMapping()
    @Operation(summary = "修改流程表单")
    @BizNo(spEl = "{#sysForm.formName}")
    public ApiResult edit(@RequestBody SysForm sysForm) {
        sysFormService.updateSysForm(sysForm);
        return ApiResult.success();
    }

    /**
     * 删除流程表单
     */
    @DeleteMapping()
    @Operation(summary = "删除流程表单")
    @BizNo(spEl = "{#formIds}")
    public ApiResult remove(Long[] formIds) {
        sysFormService.deleteSysFormByIds(formIds);
        return ApiResult.success();
    }

    /**
     * 删除流程表单
     */
    @DeleteMapping("/{formId}")
    @Operation(summary = "删除流程表单")
    @BizNo(spEl = "{#formId}")
    public ApiResult remove(@PathVariable(value = "formId") Long formId) {
        sysFormService.deleteSysFormById(formId);
        return ApiResult.success();
    }
}
