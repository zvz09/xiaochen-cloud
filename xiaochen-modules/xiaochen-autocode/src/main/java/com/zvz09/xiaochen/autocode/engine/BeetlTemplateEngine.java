package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeetlTemplateEngine extends AbstractTemplateEngine implements InitializingBean, ITemplateEngine {

    private GroupTemplate groupTemplate;


    @Override
    public String engineType() {
        return "Beetl";
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body) {
        Template tpl = groupTemplate.getTemplate(body);
        tpl.binding(buildParams(genConfig));
        String str = tpl.render();
        return stringToStringWriter(str);
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template) {
        return this.writer(buildParams(genConfig), template);
    }

    @Override
    public StringWriter writer(@NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template) {
        Template tpl = groupTemplate.getTemplate(template.getContent());
        tpl.binding(objectMap);
        String str = tpl.render();
        return stringToStringWriter(str);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (groupTemplate == null) {
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(resourceLoader, cfg);
        }
    }
}
