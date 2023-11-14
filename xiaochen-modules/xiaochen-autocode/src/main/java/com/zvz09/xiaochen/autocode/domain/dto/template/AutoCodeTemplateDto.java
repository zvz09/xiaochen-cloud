/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.dto
 * @className com.zvz09.xiaochen.autocode.dto.template.AutoCodeTemplateDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.dto.template;

import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import com.zvz09.xiaochen.common.web.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * AutoCodeTemplateDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/14 15:14
 */
@Getter
@Setter
@Schema(name = "自动生成代码模板", description = "")
public class AutoCodeTemplateDto extends BaseDto {
    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "语言类型")
    private String language;

    @Schema(description = "模板引擎类型 Thymeleaf ,FreeMaker")
    private String templateEngine;

    @Schema(description = "模板类型")
    private String templateType;

    @Schema(description = "默认文件名")
    private String defaultFileName;

    @Schema(description = "模板内容")
    private String content;

    public SysAutoCodeTemplate convertedToPo() {
        return SysAutoCodeTemplate.builder().id(this.getId())
                .name(this.name)
                .language(this.language)
                .templateEngine(this.templateEngine)
                .templateType(this.templateType)
                .defaultFileName(this.defaultFileName)
                .content(this.content)
                .build();
    }
}
 