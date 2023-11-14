/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.vo
 * @className com.zvz09.xiaochen.auth.vo.LoginVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.auth.vo;

import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * LoginVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/25 16:06
 */
@Getter
@Setter
@Schema(description = "登录Vo")
public class LoginVo implements Serializable {

    private SysUserVo user;

    private String token;

}
 