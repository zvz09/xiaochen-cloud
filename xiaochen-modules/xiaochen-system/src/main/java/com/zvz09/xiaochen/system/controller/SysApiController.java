package com.zvz09.xiaochen.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiDto;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import com.zvz09.xiaochen.system.api.domain.vo.SysApiVo;
import com.zvz09.xiaochen.system.service.ISysApiService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "api接口")
@RequiredArgsConstructor
public class SysApiController {

    private final ISysApiService sysApiService;

    @Operation(summary = "新增API")
    @PostMapping("/createApi")
    public ApiResult<String> createApi(@Valid @RequestBody SysApiDto sysApiDto) {
        sysApiService.createApi(sysApiDto);
        return ApiResult.success();
    }

    @Operation(summary = "删除API")
    @DeleteMapping("/deleteApi")
    public ApiResult<String> deleteApi(@Parameter(description = "id") Long id) {
        sysApiService.deleteApi(id);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping("/getApiList")
    public ApiResult<Page<SysApiVo>> getApiPage(SysApiQuery sysApiQuery) {
        return ApiResult.success(sysApiService.getApiPage(sysApiQuery));
    }


    @Operation(summary = "根据ID查询API")
    @GetMapping("/getApiById")
    public ApiResult<SysApi> getApiById(@Parameter(description = "id") Long id) {
        return ApiResult.success(sysApiService.getById(id));
    }

    @Operation(summary = "根据ID更新API")
    @PostMapping("/updateApi")
    public ApiResult<String> updateApi(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) SysApiDto sysApiDto) {
        sysApiService.updateApi(sysApiDto);
        return ApiResult.success();
    }

    @Operation(summary = "api 树")
    @GetMapping("/listTree")
    public ApiResult<List<SysApiVo>> listTree() {
        return ApiResult.success(sysApiService.listTree());
    }

    @Operation(summary = "删除选中Api")
    @DeleteMapping("/deleteApisByIds")
    public ApiResult<String> deleteApisByIds(@RequestBody List<Long> ids) {
        sysApiService.deleteApisByIds(ids);
        return ApiResult.success();
    }
}
