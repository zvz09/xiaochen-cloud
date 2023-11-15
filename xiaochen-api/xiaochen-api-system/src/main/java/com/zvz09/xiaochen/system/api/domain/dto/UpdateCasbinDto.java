/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.casbin.dto
 * @className com.zvz09.xiaochen.casbin.dto.UpdateCasbinDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * UpdateCasbinDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/11 18:25
 */
@Data
public class UpdateCasbinDto {

    private String authorityCode;

    private List<CasbinInfo> casbinInfos;
}
