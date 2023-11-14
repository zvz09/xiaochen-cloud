package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.user.RegisterUserDto;
import com.zvz09.xiaochen.system.api.domain.dto.user.SysUserQuery;
import com.zvz09.xiaochen.system.api.domain.dto.user.UpdateUserDto;
import com.zvz09.xiaochen.system.service.ISysUserService;
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
    @GetMapping("/getUserList")
    public ApiResult getUserList(SysUserQuery sysUserQuery) {
        return ApiResult.success(sysUserService.getUserList(sysUserQuery));
    }

    @GetMapping("/getAllUser")
    public ApiResult getAllUser(SysUserQuery sysUserQuery) {
        return ApiResult.success(sysUserService.getAllUser(sysUserQuery));
    }


    @GetMapping("/getUserInfo")
    public ApiResult getUserInfo() {
        return ApiResult.success(sysUserService.getUserInfo());
    }

    @PostMapping("/register")
    public ApiResult register(@RequestBody RegisterUserDto registerUserDto) {
        sysUserService.register(registerUserDto);
        return ApiResult.success();
    }

    @PostMapping("/resetPassword")
    public ApiResult resetPassword(Long id) {
        sysUserService.resetPassword(id);
        return ApiResult.success();
    }

    @PutMapping("/updateUserInfo")
    public ApiResult updateUserInfo(@RequestBody UpdateUserDto updateUserDto) {
        sysUserService.updateUserInfo(updateUserDto);
        return ApiResult.success();
    }

    @DeleteMapping("/deleteUser")
    public ApiResult deleteUser(Long id) {
        sysUserService.deleteUser(id);
        return ApiResult.success();
    }
}
