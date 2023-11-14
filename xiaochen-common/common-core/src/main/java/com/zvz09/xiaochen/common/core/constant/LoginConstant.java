/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.constant
 * @className com.zvz09.xiaochen.common.core.constant.LoginConstant
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.core.constant;

import java.util.Arrays;
import java.util.List;

/**
 * LoginConstant
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/25 15:54
 */
public interface LoginConstant {

    /**
     * 默认的token名称
     */
    String TOKEN_NAME = "xc-token";

    /**
     * 默认的token过期时间 30分钟
     */
    Integer DEFAULT_TOKEN_EXPIRE_MINUTES = 30;

    /**
     * 管理员角色编码
     */
    String ADMIN_ROLE_CODE = "admin";

    /**
     * 管理员admin角色ID
     */
    List<Long> ADMIN_ROLE_ID_LIST = Arrays.asList(1L);

    String JWT_BLACK_LIST = "jwt_black_list";
}