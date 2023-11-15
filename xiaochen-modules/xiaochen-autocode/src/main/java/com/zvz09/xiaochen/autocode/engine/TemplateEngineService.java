package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
@Lazy
@Component
public class TemplateEngineService implements ApplicationContextAware, InitializingBean {

    private Map<String, ITemplateEngine> templateEngineMap;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 执行处理
     */
    public StringWriter execute(@NotNull String engineType, @NotNull GenConfig genConfig, @NotNull String templateName, @NotNull String body) {
        ITemplateEngine templateEngine = templateEngineMap.get(engineType);
        if (templateEngine == null) {
            throw new BusinessException(String.format("%s:模板类型暂不支持", engineType));
        }
        return templateEngine.writer(genConfig, templateName, body);
    }

    public StringWriter execute(@NotNull String engineType, @NotNull GenConfig genConfig, @NotNull SysAutoCodeTemplate template) {
        ITemplateEngine templateEngine = templateEngineMap.get(engineType);
        if (templateEngine == null) {
            throw new BusinessException(String.format("%s:模板类型暂不支持", engineType));
        }
        return templateEngine.writer(genConfig, template);
    }

    public StringWriter execute(@NotNull String engineType, @NotNull Map<String, Object> objectMap, @NotNull SysAutoCodeTemplate template) {
        ITemplateEngine templateEngine = templateEngineMap.get(engineType);
        if (templateEngine == null) {
            throw new BusinessException(String.format("%s:模板类型暂不支持", engineType));
        }
        return templateEngine.writer(objectMap, template);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, ITemplateEngine> serviceMap = applicationContext.getBeansOfType(ITemplateEngine.class);
        if (!serviceMap.isEmpty()) {
            templateEngineMap = serviceMap.values().stream()
                    .collect(Collectors.toMap(ITemplateEngine::engineType, c -> c));
        }
    }
}
