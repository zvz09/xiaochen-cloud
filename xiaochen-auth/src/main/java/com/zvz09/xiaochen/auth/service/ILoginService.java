/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.service
 * @className com.zvz09.xiaochen.auth.service.ILoginService
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.auth.service;

import com.zvz09.xiaochen.auth.dto.LoginDto;
import com.zvz09.xiaochen.auth.vo.LoginVo;

/**
 * ILoginService
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/30 13:36
 */
public interface ILoginService {
    LoginVo doLogin(LoginDto loginDto);

    void jsonInBlacklist();
}