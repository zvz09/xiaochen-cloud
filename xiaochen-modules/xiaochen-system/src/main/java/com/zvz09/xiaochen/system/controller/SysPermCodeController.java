package com.zvz09.xiaochen.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.perm.SysPermCodeDto;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;
import com.zvz09.xiaochen.system.service.ISysPermCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

/**
 * <p>
 * 系统权限字 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@RestController
@RequestMapping("/perm_code")
@Tag(name = "权限字")
@RequiredArgsConstructor
public class SysPermCodeController {

    private final ISysPermCodeService sysPermCodeService;


    @PostMapping()
    @Operation(summary = "新增权限字")
    public ApiResult<String> create(@Valid @RequestBody SysPermCodeDto sysPermCodeDto) {
        sysPermCodeService.create(sysPermCodeDto);
        return ApiResult.success();
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限字")
    public ApiResult<String> delete(@PathVariable(value = "id") Long id) {
        sysPermCodeService.delete(id);
        return ApiResult.success();
    }


    @PutMapping()
    @Operation(summary = "根据ID更新权限字")
    public ApiResult<String> update(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) SysPermCodeDto sysPermCodeDto) {
        sysPermCodeService.updatePermCode(sysPermCodeDto);
        return ApiResult.success();
    }

    @PutMapping("/bind/{id}")
    @Operation(summary = "绑定权限资源")
    public ApiResult<String> bindApis(@PathVariable(value = "id") Long id, @RequestBody List<Long> apiIds) {
        sysPermCodeService.bindApis(id, apiIds);
        return ApiResult.success();
    }

    /**
     * 列表
     */
    @PostMapping("/listTree")
    @Operation(summary = "列表")
    public ApiResult<IPage<SysPermCodeVo>> listTree(@RequestBody(required = false) BasePage basePage) {
        return ApiResult.success(sysPermCodeService.listTree(basePage));
    }



    @GetMapping("/{id}")
    @Operation(summary = "详情")
    public ApiResult<SysPermCodeVo> detail(@PathVariable(value = "id") Long id) {
        return ApiResult.success(sysPermCodeService.detail(id));
    }

    /**
     * 列表
     */
    @GetMapping()
    @Operation(summary = "获取当前角色所有权限字")
    public ApiResult<Map<String, List<String>>> listPermCodes() {
        return ApiResult.success(sysPermCodeService.listPermCodes());
    }
}

