package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.system.api.domain.dto.authority.CopySysAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.authority.SysAuthorityDto;
import com.zvz09.xiaochen.system.service.ISysAuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/authority")
@Tag(name = "角色")
@RequiredArgsConstructor
public class SysAuthorityController {

    private final ISysAuthorityService sysAuthorityService;


    @PostMapping("/createAuthority")
    public ApiResult createAuthority(@Validated @RequestBody SysAuthorityDto sysAuthorityDto) {
        sysAuthorityService.createAuthority(sysAuthorityDto);
        return ApiResult.success();
    }

    @PostMapping("/copyAuthority")
    public ApiResult copyAuthority(@Validated @RequestBody CopySysAuthorityDto copySysAuthorityDto) {
        sysAuthorityService.copyAuthority(copySysAuthorityDto);
        return ApiResult.success();
    }

    @DeleteMapping("/deleteAuthority")
    public ApiResult deleteAuthority(@Parameter(description = "id") Long id) {
        sysAuthorityService.deleteAuthority(id);
        return ApiResult.success();
    }


    @PutMapping("/updateAuthority")
    public ApiResult updateAuthority(@Validated(value = UpdateValidation.class) @RequestBody SysAuthorityDto sysAuthorityDto) {
        sysAuthorityService.updateAuthority(sysAuthorityDto);
        return ApiResult.success();
    }

    @Operation(summary = "设置角色资源权限")
    @PostMapping("/setDataAuthority")
    public ApiResult<String> setDataAuthority(BasePage basePage) {
        //TODO

        return ApiResult.success();
    }

    @Operation(summary = "角色树")
    @GetMapping("/getAuthorityTree")
    public ApiResult getAuthorityTree() {
        return ApiResult.success(sysAuthorityService.getAuthorityTree());
    }

}
