package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class FreemarkerTemplateEngine extends AbstractTemplateEngine implements InitializingBean, ITemplateEngine {
    private Configuration configuration;

    @Override
    public @NotNull String engineType() {
        return "FreeMarker";
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body) {
        StringWriter stringWriter = new StringWriter();
        try {
            Template tpl = new Template(templateName, body, configuration);
            tpl.process(buildParams(genConfig), stringWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return stringWriter;
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template) {
        return this.writer(buildParams(genConfig), template);
    }

    @Override
    public StringWriter writer(@NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template) {
        StringWriter sw = new StringWriter();
        try {
            Template tpl = new Template(template.getName(), template.getContent(), configuration);
            tpl.process(objectMap, sw);
        } catch (TemplateNotFoundException templateNotFoundException) {
            throw new BusinessException("模板不存在");
        } catch (Exception e) {
            log.error("系统异常：", e);
            throw new BusinessException("系统异常");
        }
        return sw;
    }

    @Override
    public void afterPropertiesSet() {
        if (configuration == null) {
            configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("UTF-8");
        }
    }
}
