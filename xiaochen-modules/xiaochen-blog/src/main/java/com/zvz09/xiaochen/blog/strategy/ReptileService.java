package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.compiler.DynamicCompiler;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.domain.entity.ReptileClass;
import com.zvz09.xiaochen.blog.service.IReptileClassService;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReptileService implements ApplicationContextAware {

    private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("package\\s+([^\\s;]+)\\s*;");
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("class\\s+(\\w+)\\s+implements\\s+ReptileDataParserStrategy");


    private static ApplicationContext applicationContext;

    private final IReptileClassService reptileClassService;

    private final Map<String, ReptileDataParserStrategy> strategyMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ReptileService.applicationContext = applicationContext;
        loadDbClass();
        Map<String, ReptileDataParserStrategy> beans = applicationContext.getBeansOfType(ReptileDataParserStrategy.class);
        if (!beans.isEmpty()) {
            strategyMap.putAll(beans);
        }
    }

    public void loadDbClass(){
        List<ReptileClass> reptileClassList = reptileClassService.list();
        reptileClassList.forEach(reptileClass -> {
            try {
                Class clz = compileAndLoad(reptileClass);
                registerBean(reptileClass.getClassName(), clz);
            } catch (ClassNotFoundException e) {
                log.error("未找到对应类", e);
                throw new BusinessException("未找到对应类");
            }
        });
        strategyMap.clear();
    }

    private void registerBean(String beanName, Class clz) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)ReptileService.applicationContext.getAutowireCapableBeanFactory();
        //创建bean信息
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        //动态注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName,beanDefinitionBuilder.getBeanDefinition());
    }

    private Class compileAndLoad(@NotNull ReptileClass reptileClass) throws ClassNotFoundException {
        if(StringUtils.isEmpty(reptileClass.getContent())){
            log.error("配置为空");
            throw new BusinessException("配置为空");
        }

        String packageName="" ,className = "";
        if(reptileClass.getContent().startsWith("package")){
            Matcher matcher = PACKAGE_NAME_PATTERN.matcher(reptileClass.getContent());
            if (matcher.find()) {
                packageName = matcher.group(1);
            } else {
                log.error("策略类缺少包配置");
                throw new BusinessException("策略类缺少包配置");
            }
        }else {
            log.error("策略类缺少包配置");
            throw new BusinessException("策略类缺少包配置");
        }

        Matcher matcher = CLASS_NAME_PATTERN.matcher(reptileClass.getContent());
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            log.error("获取策略类名失败");
            throw new BusinessException("获取策略类名失败");
        }

        if(strategyMap.containsKey(className)){
            log.error("策略类名重复");
            throw new BusinessException("策略类名重复");
        }

        String fullName = packageName+"."+ className;

        DynamicCompiler compiler = new DynamicCompiler();
        Class clz = compiler.compileAndLoad(fullName, reptileClass.getContent());
        strategyMap.put(className,null);
        return clz;
    }

    /**
     * 加载新策略类
     * @param reptileClass
     */
    public synchronized  void loadClass(ReptileClass reptileClass){
        try {
            Class clz = compileAndLoad(reptileClass);
            registerBean(reptileClass.getClassName(), clz);
            strategyMap.clear();
            Map<String, ReptileDataParserStrategy> beans = applicationContext.getBeansOfType(ReptileDataParserStrategy.class);
            if (!beans.isEmpty()) {
                strategyMap.putAll(beans);
            }
        } catch (ClassNotFoundException e) {
            log.error("未找到对应类", e);
            throw new BusinessException("未找到对应类");
        }
    }

    /**
     * 卸载策略类
     * @param beanName
     */
    public synchronized  void removeBean(String beanName){
        if(strategyMap.containsKey(beanName)){
            //获取BeanFactory
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)ReptileService.applicationContext.getAutowireCapableBeanFactory();

            //卸载bean
            defaultListableBeanFactory.removeBeanDefinition(beanName);
            //删除
            strategyMap.remove(beanName);
        }
    }

    /**
     * 执行操作
     * @return
     */
    public synchronized  ArticleDTO execute(String url, Document document) {
        for (String beanName : strategyMap.keySet()){
            if(strategyMap.get(beanName)!=null && strategyMap.get(beanName).isMatch(url)){
                return strategyMap.get(beanName).parseData(document);
            }
        }
        return null;
    }
}
