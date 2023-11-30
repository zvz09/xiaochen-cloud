package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.position.PositionDto;
import com.zvz09.xiaochen.system.api.domain.dto.position.QueryDto;
import com.zvz09.xiaochen.system.service.ISysPositionService;
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
 * 岗位管理 前端控制器
 *
 * @author zvz09
 * @date 2023-10-10 13:53:40
 */

@Slf4j
@RestController
@Tag(name = "岗位管理")
@RequestMapping("/position")
@RequiredArgsConstructor
public class SysPositionController {

    private final ISysPositionService SysPositionService;


    @Operation(summary = "新增岗位")
    @PostMapping("")
    public ApiResult<String> createPosition(@Valid @RequestBody PositionDto positionDto) {
        SysPositionService.createPosition(positionDto);
        return ApiResult.success();
    }

    @Operation(summary = "删除选中岗位")
    @DeleteMapping("")
    public ApiResult<String> deletePositionByIds(@RequestBody List<Long> ids) {
        SysPositionService.deletePositionByIds(ids);
        return ApiResult.success();
    }

    @Operation(summary = "删除岗位")
    @DeleteMapping("/{id}")
    public ApiResult<String> deletePosition(@PathVariable(value = "id") Long id) {
        SysPositionService.deletePosition(id);
        return ApiResult.success();
    }

    @Operation(summary = "根据ID更新岗位")
    @PutMapping("")
    public ApiResult<String> updatePosition(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) PositionDto positionDto) {
        SysPositionService.updatePosition(positionDto);
        return ApiResult.success();
    }

    @Operation(summary = "分页查询岗位")
    @PostMapping("/page")
    public ApiResult getPositionList(@RequestBody QueryDto queryDto) {
        return ApiResult.success(SysPositionService.getPositionList(queryDto));
    }

}
