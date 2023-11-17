package com.zvz09.xiaochen.system.controller;


import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;
import com.zvz09.xiaochen.system.service.ISysMenuService;
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
 * 菜单表 前端控制器
 *
 * @author zvz09
 * @date 2023-11-16 15:40:03
 */

@Slf4j
@RestController
@Tag(name = "菜单表")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @Operation(summary = "新增菜单")
    @PostMapping()
    public ApiResult<String> createMenu(@Valid @RequestBody SysMenuDto sysMenuDto) {
        sysMenuService.createMenu(sysMenuDto);
        return ApiResult.success();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteMenu(@PathVariable(value = "id") Long id) {
        sysMenuService.deleteMenu(id);
        return ApiResult.success();
    }

    @Operation(summary = "根据ID更新菜单")
    @PutMapping()
    public ApiResult<String> updateMenu(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) SysMenuDto sysMenuDto) {
        sysMenuService.updateMenu(sysMenuDto);
        return ApiResult.success();
    }

    /**
     * 获取用户动态路由
     */
    @GetMapping("/listTree")
    public ApiResult<List<SysMenuVo>> listTree() {
        return ApiResult.success(sysMenuService.listTree());
    }


}

