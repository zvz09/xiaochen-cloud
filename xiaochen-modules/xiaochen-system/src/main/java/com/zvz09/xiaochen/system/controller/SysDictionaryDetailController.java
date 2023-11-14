package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailQuery;
import com.zvz09.xiaochen.system.service.ISysDictionaryDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "字典项")
@RequestMapping("/dictionaryDetail")
@RequiredArgsConstructor
public class SysDictionaryDetailController {

    private final ISysDictionaryDetailService sysDictionaryDetailService;

    @GetMapping("/getSysDictionaryDetailList")
    public ApiResult getSysDictionaryDetailList(@Valid SysDictionaryDetailQuery dictionaryDetailQuery) {
        return ApiResult.success(sysDictionaryDetailService.getSysDictionaryDetailList(dictionaryDetailQuery));
    }

    @GetMapping("/getByDictionaryType")
    public ApiResult getByDictionaryType(@NotNull String sysDictionaryType, BasePage basePage) {
        return ApiResult.success(sysDictionaryDetailService.getByDictionaryType(sysDictionaryType, basePage));
    }

    @Operation(summary = "根据类型获取所有")
    @GetMapping("/getAllByDictionaryType")
    public ApiResult getByDictionaryType(@NotNull String type) {
        return ApiResult.success(sysDictionaryDetailService.getByDictionaryType(type));
    }

    @PostMapping("/createSysDictionaryDetail")
    public ApiResult createSysDictionaryDetail(@RequestBody SysDictionaryDetailDto dictionaryDetailDto) {
        sysDictionaryDetailService.createSysDictionaryDetail(dictionaryDetailDto);
        return ApiResult.success();
    }


    @PutMapping("/updateSysDictionaryDetail")
    public ApiResult updateSysDictionaryDetail(@RequestBody SysDictionaryDetailDto dictionaryDetailDto) {
        sysDictionaryDetailService.updateSysDictionaryDetail(dictionaryDetailDto);
        return ApiResult.success();
    }

    @DeleteMapping("/deleteSysDictionaryDetail")
    public ApiResult deleteSysDictionaryDetail(Long id) {
        sysDictionaryDetailService.deleteSysDictionaryDetail(id);
        return ApiResult.success();
    }
}
