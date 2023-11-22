package com.zvz09.xiaochen.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.user.RegisterUserDto;
import com.zvz09.xiaochen.system.api.domain.dto.user.SysUserQuery;
import com.zvz09.xiaochen.system.api.domain.dto.user.UpdateUserDto;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import com.zvz09.xiaochen.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户")
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    /**
     * 分页获取用户列表
     *
     * @return ApiResult
     */
    @PostMapping("/list")
    @Operation(summary = "用户列表")
    public ApiResult<IPage<SysUserVo>> getUserList(@RequestBody SysUserQuery sysUserQuery) {
        return ApiResult.success(sysUserService.getUserList(sysUserQuery));
    }

    @PostMapping("/simpleList")
    @Operation(summary = "用户简要信息列表")
    public ApiResult<IPage<SysUserVo>> simpleList(@RequestBody SysUserQuery sysUserQuery) {
        return ApiResult.success(sysUserService.simpleList(sysUserQuery));
    }


    @GetMapping("/currentUser")
    @Operation(summary = "当前用户信息")
    public ApiResult<SysUserVo> currentUser() {
        return ApiResult.success(sysUserService.getUserInfo());
    }

    @PostMapping("")
    @Operation(summary = "新增用户")
    public ApiResult<String> createUser(@RequestBody UpdateUserDto updateUserDto) {
        sysUserService.createUser(updateUserDto);
        return ApiResult.success();
    }

    @PostMapping("/register")
    @Operation(summary = "注册用户")
    public ApiResult<String> register(@RequestBody RegisterUserDto registerUserDto) {
        sysUserService.register(registerUserDto);
        return ApiResult.success();
    }

    @PostMapping("/resetPassword/{id}")
    @Operation(summary = "重置密码")
    public ApiResult<String> resetPassword(@PathVariable(value = "id") Long id) {
        sysUserService.resetPassword(id);
        return ApiResult.success();
    }

    @PutMapping()
    @Operation(summary = "更新用户")
    public ApiResult<String> updateUserInfo(@RequestBody UpdateUserDto updateUserDto) {
        sysUserService.updateUserInfo(updateUserDto);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ApiResult<String> deleteUser(@PathVariable(value = "id") Long id) {
        sysUserService.deleteUser(id);
        return ApiResult.success();
    }
}
