package com.zvz09.xiaochen.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailQuery;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryDetailVo;
import com.zvz09.xiaochen.system.service.ISysDictionaryDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */

@Slf4j
@RestController
@Tag(name = "字典项")
@RequestMapping("/dictionary_detail")
@RequiredArgsConstructor
public class SysDictionaryDetailController {

    private final ISysDictionaryDetailService sysDictionaryDetailService;

    @PostMapping("/list")
    @Operation(summary = "字典项列表")
    public ApiResult<IPage<SysDictionaryDetailVo>> getSysDictionaryDetailList(@Valid @RequestBody SysDictionaryDetailQuery dictionaryDetailQuery) {
        return ApiResult.success(sysDictionaryDetailService.getSysDictionaryDetailList(dictionaryDetailQuery));
    }

    @PostMapping("/{encode}")
    @Operation(summary = "根据编码获取字典项列表")
    public ApiResult<IPage<SysDictionaryDetailVo>> getByDictionaryType(@PathVariable(value = "encode") String encode, @RequestBody BasePage basePage) {
        return ApiResult.success(sysDictionaryDetailService.getByDictionaryEncode(encode, basePage));
    }


    @GetMapping("/listAll/{encode}")
    @Operation(summary = "根据编码获取所有字典项")
    public ApiResult<List<SysDictionaryDetailVo>> getByDictionaryType(@PathVariable(value = "encode") String encode) {
        return ApiResult.success(sysDictionaryDetailService.getByDictionaryEncode(encode));
    }

    @PostMapping("")
    @Operation(summary = "创建字典项")
    public ApiResult createSysDictionaryDetail(@RequestBody SysDictionaryDetailDto dictionaryDetailDto) {
        sysDictionaryDetailService.createSysDictionaryDetail(dictionaryDetailDto);
        return ApiResult.success();
    }


    @PutMapping("")
    @Operation(summary = "更新字典项")
    public ApiResult updateSysDictionaryDetail(@RequestBody SysDictionaryDetailDto dictionaryDetailDto) {
        sysDictionaryDetailService.updateSysDictionaryDetail(dictionaryDetailDto);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典项")
    public ApiResult deleteSysDictionaryDetail(@PathVariable(value = "id")Long id) {
        sysDictionaryDetailService.deleteSysDictionaryDetail(id);
        return ApiResult.success();
    }

    @PutMapping("/changeStatus/{id}")
    @Operation(summary = "切换状态")
    public ApiResult changeDictionaryDetailStatus(@PathVariable(value = "id") Long id) {
        sysDictionaryDetailService.changeStatus(id);
        return ApiResult.success();
    }
}
