package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.dept.DepartmentDto;
import com.zvz09.xiaochen.system.api.domain.vo.SysDepartmentVo;
import com.zvz09.xiaochen.system.service.ISysDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
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
 * 部门 前端控制器
 *
 * @author zvz09
 * @date 2023-10-10 11:49:52
 */

@Slf4j
@RestController
@Tag(name = "部门")
@RequestMapping("/dept")
@RequiredArgsConstructor
public class SysDepartmentController {

    private final ISysDepartmentService sysDepartmentService;


    @Operation(summary = "新增部门")
    @PostMapping()
    public ApiResult<String> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        sysDepartmentService.createDepartment(departmentDto);
        return ApiResult.success();
    }

    @Operation(summary = "删除选中部门")
    @DeleteMapping()
    public ApiResult<String> deleteDepartmentByIds(@RequestBody List<Long> ids) {
        sysDepartmentService.deleteDepartmentByIds(ids);
        return ApiResult.success();
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteDepartment(@PathVariable(value = "id") Long id) {
        sysDepartmentService.deleteDepartment(id);
        return ApiResult.success();
    }

    @Operation(summary = "根据ID更新部门")
    @PutMapping()
    public ApiResult<String> updateDepartment(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) DepartmentDto departmentDto) {
        sysDepartmentService.updateDepartment(departmentDto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping("/tree")
    public ApiResult<List<SysDepartmentVo>> getDepartmentTree() {
        return ApiResult.success(sysDepartmentService.getDepartmentTree());
    }
}
