package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryQuery;
import com.zvz09.xiaochen.system.service.ISysDictionaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "字典")
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class SysDictionaryController {

    private final ISysDictionaryService sysDictionaryService;

    @GetMapping("/getSysDictionaryList")
    public ApiResult getSysDictionaryList(SysDictionaryQuery sysDictionaryQuery) {
        return ApiResult.success(sysDictionaryService.getSysDictionaryList(sysDictionaryQuery));
    }


    @PostMapping("/createSysDictionary")
    public ApiResult createSysDictionary(@RequestBody SysDictionaryDto sysDictionaryDto) {
        sysDictionaryService.createSysDictionary(sysDictionaryDto);
        return ApiResult.success();
    }

    @PutMapping("/updateSysDictionary")
    public ApiResult updateSysDictionary(@RequestBody SysDictionaryDto sysDictionaryDto) {
        sysDictionaryService.updateSysDictionary(sysDictionaryDto);
        return ApiResult.success();
    }


    @DeleteMapping("/deleteSysDictionary")
    public ApiResult deleteSysDictionary(Long id) {
        sysDictionaryService.deleteSysDictionary(id);
        return ApiResult.success();
    }
}
