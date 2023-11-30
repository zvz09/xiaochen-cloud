package com.zvz09.xiaochen.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryQuery;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryVo;
import com.zvz09.xiaochen.system.service.ISysDictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Tag(name = "字典")
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class SysDictionaryController {

    private final ISysDictionaryService sysDictionaryService;

    @PostMapping("/page")
    @Operation(summary = "字典列表")
    public ApiResult<IPage<SysDictionaryVo>> getSysDictionaryList(@RequestBody SysDictionaryQuery sysDictionaryQuery) {
        return ApiResult.success(sysDictionaryService.getSysDictionaryList(sysDictionaryQuery));
    }


    @PostMapping("")
    @Operation(summary = "新增字典")
    public ApiResult createDictionary(@RequestBody SysDictionaryDto sysDictionaryDto) {
        sysDictionaryService.createSysDictionary(sysDictionaryDto);
        return ApiResult.success();
    }

    @PutMapping("")
    @Operation(summary = "更新字典")
    public ApiResult updateDictionary(@RequestBody SysDictionaryDto sysDictionaryDto) {
        sysDictionaryService.updateSysDictionary(sysDictionaryDto);
        return ApiResult.success();
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典")
    public ApiResult deleteDictionary(@PathVariable(value = "id") Long id) {
        sysDictionaryService.deleteSysDictionary(id);
        return ApiResult.success();
    }
    @PutMapping("/changeStatus/{id}")
    @Operation(summary = "更新状态")
    public ApiResult changeDictionaryStatus(@PathVariable(value = "id") Long id) {
        sysDictionaryService.changeStatus(id);
        return ApiResult.success();
    }
}
