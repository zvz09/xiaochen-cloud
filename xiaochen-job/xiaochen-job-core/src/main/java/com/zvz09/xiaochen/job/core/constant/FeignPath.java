/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.constant
 * @className com.zvz09.xiaochen.auth.constant.LoginConstant
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.job.core.constant;

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

    String JOB_CLIENT = FEIGN_PATH_PREFIX + "/job/client";
    String JOB_ADMIN = FEIGN_PATH_PREFIX + "/job/admin";
}