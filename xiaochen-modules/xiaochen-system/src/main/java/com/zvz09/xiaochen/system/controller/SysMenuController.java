package com.zvz09.xiaochen.system.controller;


import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    private final ISysMenuService SysMenuService;

    /**
     * 获取用户动态路由
     *
     * @return
     */
    @GetMapping("/listTree")
    public ApiResult listTree() {
        return ApiResult.success(SysMenuService.listTree());
    }


}

