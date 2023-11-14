package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VelocityTemplateEngine extends AbstractTemplateEngine implements InitializingBean, ITemplateEngine {
    private VelocityEngine velocityEngine;

    @Override
    public @NotNull String engineType() {
        return "Velocity";
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body) {
        String tmpTemplateName = String.format("%s-%s-%s", templateName, UUID.randomUUID(), LocalDateTime.now());
        StringResourceRepository repo = StringResourceLoader.getRepository();
        repo.putStringResource(tmpTemplateName, body);
        VelocityContext velocityContext = new VelocityContext(buildParams(genConfig));
        // 渲染模板
        StringWriter sw = new StringWriter();
        Template tpl = velocityEngine.getTemplate(tmpTemplateName, "UTF-8");
        tpl.merge(velocityContext, sw);
        //删除临时模板
        repo.removeStringResource(tmpTemplateName);
        return sw;
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template) {
        return this.writer(buildParams(genConfig), template);
    }

    @Override
    public StringWriter writer(@NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template) {
        VelocityContext velocityContext = new VelocityContext(objectMap);
        StringResourceRepository repo = StringResourceLoader.getRepository();
        repo.putStringResource(template.getName(), template.getContent());
        // 渲染模板
        StringWriter sw = new StringWriter();
        Template tpl = velocityEngine.getTemplate(template.getName(), "UTF-8");
        tpl.merge(velocityContext, sw);
        repo.removeStringResource(template.getName());
        return sw;
    }

    @Override
    public void afterPropertiesSet() {
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty("resource.loaders", "string");
            p.setProperty("resource.loader.string.class",
                    "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            p.setProperty("resource.loader.string.repository.class",
                    "org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl");  //这是自定义的获取模板实现类
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            // 初始化Velocity引擎，指定配置Properties
            velocityEngine = new VelocityEngine(p);
            velocityEngine.init();
        }
    }
}
