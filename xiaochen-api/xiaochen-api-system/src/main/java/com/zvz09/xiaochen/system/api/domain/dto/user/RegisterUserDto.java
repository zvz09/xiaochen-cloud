/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.user
 * @className com.zvz09.xiaochen.system.dto.user.RegisterUserDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.user;

import cn.hutool.crypto.digest.BCrypt;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * RegisterUserDto
 *
 * @author zvz09
 * @version 1.0
 * @description 添加用户
 * @date 2023/9/12 15:57
 */
@Data
@NoArgsConstructor
public class RegisterUserDto {


    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "头型地址")
    private String headerImg;
    @Schema(description = "是否启动")
    private Long enable;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱地址")
    private String email;

    public SysUser convertedToPo() {
        return SysUser.builder()
                .username(this.userName)
                .password(BCrypt.hashpw(this.password, BCrypt.gensalt()))
                .nickName(this.nickName)
                .headerImg(this.headerImg)
                .enable(this.enable)
                .phone(this.phone)
                .email(this.email)
                .build();
    }
}
 