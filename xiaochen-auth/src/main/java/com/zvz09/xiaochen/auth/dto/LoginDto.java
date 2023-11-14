/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.dto
 * @className com.zvz09.xiaochen.auth.dto.LoginDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginDto
 *
 * @author zvz09
 * @version 1.0
 * @description 登录请求体
 * @date 2023/8/25 15:51
 */
@Data
@Schema(description = "LoginDto")
public class LoginDto {

    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @Schema(description = "验证码ID", example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;

}
 