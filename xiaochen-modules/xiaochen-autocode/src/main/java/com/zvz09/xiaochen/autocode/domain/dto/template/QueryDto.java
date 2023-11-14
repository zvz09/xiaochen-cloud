/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.dto
 * @className com.zvz09.xiaochen.autocode.dto.template.AutoCodeTemplateQuery
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.dto.template;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AutoCodeTemplateQuery
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/14 14:40
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryDto extends BasePage {
    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "语言类型")
    private String language;

    @Schema(description = "模板引擎类型 Thymeleaf ,FreeMaker")
    private String templateEngine;
}
 