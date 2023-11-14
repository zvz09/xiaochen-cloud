package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.menu.AddMenuAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysBaseMenuDto;
import com.zvz09.xiaochen.system.service.ISysBaseMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/menu")
@Tag(name = "菜单")
@RequiredArgsConstructor
public class SysBaseMenuController {

    private final ISysBaseMenuService sysBaseMenuService;

    /**
     * 获取用户动态路由
     *
     * @return
     */
    @GetMapping("/getMenu")
    public ApiResult getMenu() {
        return ApiResult.success(sysBaseMenuService.getMenuTree());
    }

    /**
     * 获取菜单动态路由
     *
     * @return
     */
    @GetMapping("/getBaseMenuTree")
    public ApiResult getBaseMenuTree() {
        return ApiResult.success(sysBaseMenuService.getBaseMenuTree());
    }

    /**
     * 增加menu和角色关联关系
     */
    @PostMapping("/addMenuAuthority")
    public ApiResult addMenuAuthority(@Validated @RequestBody AddMenuAuthorityDto addMenuAuthorityDto) {
        sysBaseMenuService.addMenuAuthority(addMenuAuthorityDto);
        return ApiResult.success();
    }

    /**
     * 获取指定角色menu
     *
     * @param authorityId
     * @return
     */
    @GetMapping("/getMenuAuthority")
    public ApiResult getMenuAuthority(@RequestParam Long authorityId) {
        return ApiResult.success(sysBaseMenuService.getMenuAuthority(authorityId));
    }

    /**
     * 新增菜单
     *
     * @param sysBaseMenuDto
     * @return
     */
    @PostMapping("/addBaseMenu")
    public ApiResult addBaseMenu(@Validated @RequestBody SysBaseMenuDto sysBaseMenuDto) {
        sysBaseMenuService.addBaseMenu(sysBaseMenuDto);
        return ApiResult.success();
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteBaseMenu")
    public ApiResult deleteBaseMenu(Long id) {
        sysBaseMenuService.deleteBaseMenu(id);
        return ApiResult.success();
    }


    @PostMapping("/updateBaseMenu")
    public ApiResult updateBaseMenu(@Validated(value = UpdateValidation.class) @RequestBody SysBaseMenuDto sysBaseMenuDto) {
        sysBaseMenuService.updateBaseMenu(sysBaseMenuDto);
        return ApiResult.success();
    }

    /**
     * 根据id获取菜单
     *
     * @param id
     * @return
     */
    @GetMapping("/getBaseMenuById")
    public ApiResult getBaseMenuById(Long id) {
        return ApiResult.success(sysBaseMenuService.getBaseMenuById(id));
    }

    /**
     * 根据id获取菜单
     *
     * @return
     */
    @GetMapping("/getMenuList")
    public ApiResult getMenuList(BasePage basePage) {
        return ApiResult.success(sysBaseMenuService.getMenuList(basePage));
    }
}
