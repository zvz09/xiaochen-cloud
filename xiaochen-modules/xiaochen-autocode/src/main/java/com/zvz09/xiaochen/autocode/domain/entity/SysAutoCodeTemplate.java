package com.zvz09.xiaochen.autocode.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 系统自动代码模板
 * </p>
 *
 * @author zvz09
 * @since 2023-09-14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_auto_code_template")
@Schema(name = "SysAutoCodeTemplate", description = "系统自动代码模板")
public class SysAutoCodeTemplate extends BaseEntity {

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
}
