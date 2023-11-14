/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.controller
 * @className com.zvz09.xiaochen.auth.controller.LoginController
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.auth.controller;

import com.zvz09.xiaochen.auth.dto.LoginDto;
import com.zvz09.xiaochen.auth.service.ILoginService;
import com.zvz09.xiaochen.auth.vo.LoginVo;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController
 *
 * @author zvz09
 * @version 1.0
 * @description 登录控制器
 * @date 2023/8/25 15:37
 */

@Slf4j
@RestController
@Tag(name = "登录退出")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    @PostMapping("/login")
    public ApiResult<LoginVo> login(@RequestBody LoginDto loginDto) {

        LoginVo loginVo = loginService.doLogin(loginDto);

        return ApiResult.success(loginVo);
    }

    @PostMapping("/jwt/jsonInBlacklist")
    public ApiResult jsonInBlacklist() {
        loginService.jsonInBlacklist();
        return ApiResult.success();
    }
}
 