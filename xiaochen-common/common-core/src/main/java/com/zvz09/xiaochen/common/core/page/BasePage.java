/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.page
 * @className com.zvz09.xiaochen.common.core.page.BasePage
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BasePage
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/1 14:01
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasePage {

    @Schema(description = "页码")
    private Long pageNum = 1L;

    @Schema(description = "每页大小")
    private Long pageSize = 10L;

    @Schema(description = "关键字")
    private String keyword;
}
 