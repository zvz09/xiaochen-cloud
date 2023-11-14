package com.zvz09.xiaochen.autocode.engine;

import com.jfinal.template.Engine;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.engine.enjoy.StringSource;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;


/**
 * @author lizili-YF0033
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnjoyTemplateEngine extends AbstractTemplateEngine implements InitializingBean, ITemplateEngine {

    private Engine engine;

    @Override
    public String engineType() {
        return "Enjoy";
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body) {
        String str = engine.getTemplate(new StringSource(body, templateName)).renderToString(buildParams(genConfig));
        return stringToStringWriter(str);
    }

    @Override
    public StringWriter writer(@NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template) {
        return this.writer(buildParams(genConfig), template);
    }

    @Override
    public StringWriter writer(@NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template) {
        String str = engine.getTemplate(new StringSource(template.getContent(), template.getName())).renderToString(objectMap);
        return stringToStringWriter(str);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (engine == null) {
            engine = new Engine();
            // 支持模板热加载，绝大多数生产环境下也建议配置成 true，除非是极端高性能的场景
            engine.setDevMode(true);
            // 配置极速模式，性能提升 13%
            Engine.setFastMode(true);
            // jfinal 4.9.02 新增配置：支持中文表达式、中文变量名、中文方法名、中文模板函数名
            Engine.setChineseExpression(true);
        }
    }
}
