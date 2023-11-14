/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.constant
 * @className com.zvz09.xiaochen.auth.constant.LoginConstant
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.constant;

import static com.zvz09.xiaochen.common.core.constant.Constants.FEIGN_PATH_PREFIX;

/**
 * LoginConstant
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/25 15:54
 */
public interface FeignPath {

    String API = FEIGN_PATH_PREFIX + "/api";
    String AUTHORITY_MENU = FEIGN_PATH_PREFIX + "/authority/menu";
    String AUTHORITY = FEIGN_PATH_PREFIX + "/authority";
    String MENU = FEIGN_PATH_PREFIX + "/menu";
    String USER_AUTHORITY = FEIGN_PATH_PREFIX + "/user/authority";
    String USER = FEIGN_PATH_PREFIX + "/user";

    String DEPARTMENT =  FEIGN_PATH_PREFIX + "/department";

}