package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author lizili-YF0033
 */
public interface ITemplateEngine {
    String engineType();

    StringWriter writer(@NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body);

    StringWriter writer(@NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template);

    StringWriter writer(@NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template);
}
