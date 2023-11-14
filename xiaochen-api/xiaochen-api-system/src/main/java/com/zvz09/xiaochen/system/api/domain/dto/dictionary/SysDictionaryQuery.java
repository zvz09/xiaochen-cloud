/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.api
 * @className com.zvz09.xiaochen.system.dto.api.SysApiQuery
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.dictionary;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SysApiQuery
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/12 13:46
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysDictionaryQuery extends BasePage {

    @Schema(description = "字典名（中）")
    private String name;
    @Schema(description = "字典名（英）")
    private String type;
    @Schema(description = "状态")
    private Boolean status;
}
 