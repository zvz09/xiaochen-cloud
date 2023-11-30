package com.zvz09.xiaochen.autocode.controller;

import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.domain.dto.template.QueryDto;
import com.zvz09.xiaochen.autocode.service.ISysAutoCodeTemplateService;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
 * <p>
 * 系统自动代码模板 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-09-14
 */

@Slf4j
@RestController
@Tag(name = "自动生成代码模板")
@RequestMapping("/autoCodeTemplate")
@RequiredArgsConstructor
public class SysAutoCodeTemplateController {

    private final ISysAutoCodeTemplateService sysAutoCodeTemplateService;


    @Operation(summary = "新增代码模板")
    @PostMapping()
    public ApiResult<String> createAutoCodeTemplate(@Valid @RequestBody AutoCodeTemplateDto autoCodeTemplateDto) {
        sysAutoCodeTemplateService.createAutoCodeTemplate(autoCodeTemplateDto);
        return ApiResult.success();
    }

    @Operation(summary = "删除选中代码模板")
    @DeleteMapping()
    public ApiResult<String> deleteAutoCodeTemplateByIds(@RequestBody List<Long> ids) {
        sysAutoCodeTemplateService.deleteAutoCodeTemplateByIds(ids);
        return ApiResult.success();
    }

    @Operation(summary = "删除代码模板")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteAutoCodeTemplate(@PathVariable(value = "id") Long id) {
        sysAutoCodeTemplateService.deleteAutoCodeTemplate(id);
        return ApiResult.success();
    }

    @Operation(summary = "根据ID更新代码模板")
    @PutMapping()
    public ApiResult<String> updateAutoCodeTemplate(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) AutoCodeTemplateDto autoCodeTemplateDto) {
        sysAutoCodeTemplateService.updateAutoCodeTemplate(autoCodeTemplateDto);
        return ApiResult.success();
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public ApiResult getAutoCodeTemplateList(@RequestBody QueryDto queryDto) {
        return ApiResult.success(sysAutoCodeTemplateService.getAutoCodeTemplateList(queryDto));
    }

    @Operation(summary = "根据ID查询代码模板详情")
    @GetMapping("/{id}")
    public ApiResult getAutoCodeTemplateDetail(@PathVariable(value = "id") Long id) {
        return ApiResult.success(sysAutoCodeTemplateService.getAutoCodeTemplateDetail(id));
    }

}
