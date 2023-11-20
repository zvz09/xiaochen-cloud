package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.role.CopySysRoleDto;
import com.zvz09.xiaochen.system.api.domain.dto.role.SysRoleDto;
import com.zvz09.xiaochen.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/role")
@Tag(name = "角色")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;


    @PostMapping("")
    public ApiResult createRole(@Validated @RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.createRole(sysRoleDto);
        return ApiResult.success();
    }

    @PostMapping("/copy")
    public ApiResult copyRole(@Validated @RequestBody CopySysRoleDto copySysRoleDto) {
        sysRoleService.copyRole(copySysRoleDto);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteRole(@PathVariable(value = "id") Long id) {
        sysRoleService.deleteRole(id);
        return ApiResult.success();
    }


    @PutMapping("")
    public ApiResult updateRole(@Validated(value = UpdateValidation.class) @RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.updateRole(sysRoleDto);
        return ApiResult.success();
    }

    @Operation(summary = "角色列表")
    @PostMapping("/list")
    public ApiResult getRoleList(@RequestBody BasePage basePage) {
        return ApiResult.success(sysRoleService.getRoleList(basePage));
    }

    @Operation(summary = "绑定权限字")
    @PostMapping("/bind/{id}")
    public ApiResult bindPerm(@PathVariable Long id, @RequestBody List<Long> permIds) {
        sysRoleService.bindPerm(id, permIds);
        return ApiResult.success();
    }
}
