/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.casbin.dto
 * @className com.zvz09.xiaochen.casbin.dto.CasbinInfo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto;

import lombok.Data;

/**
 * CasbinInfo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/11 18:30
 */
@Data
public class CasbinInfo {
    private String method;

    private String path;
}
 