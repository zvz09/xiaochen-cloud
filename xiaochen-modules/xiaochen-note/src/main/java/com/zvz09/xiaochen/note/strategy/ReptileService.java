package com.zvz09.xiaochen.note.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.note.compiler.DynamicCompiler;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.domain.entity.ReptileParseClass;
import com.zvz09.xiaochen.note.service.IReptileParseClassService;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    private final IReptileParseClassService reptileClassService;

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
        List<ReptileParseClass> reptileParseClassList = reptileClassService.list(new LambdaQueryWrapper<ReptileParseClass>().eq(ReptileParseClass::getStatus,true));
        reptileParseClassList.forEach(reptileClass -> {
            try {
                Class clz = compileAndLoad(reptileClass);
                registerBean(reptileClass.getClassName(), clz);
            } catch (Exception e) {
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

    public Class<ReptileDataParserStrategy> compileAndLoad(@NotNull ReptileParseClass reptileParseClass) throws ClassNotFoundException {

        CheckResult result = checkReptileParseClass(reptileParseClass);
        if(!result.getErrMsg().isEmpty()){
            throw new BusinessException(result.getErrMsgString());
        }
        String fullName = result.getPackageName()+"."+ result.getClassName();

        DynamicCompiler compiler = new DynamicCompiler();
        Class clz = compiler.compileAndLoad(fullName, reptileParseClass.getContent());
        strategyMap.put(result.getClassName(),null);
        return clz;
    }

    public CheckResult checkReptileParseClass(ReptileParseClass reptileParseClass){
        CheckResult checkResult = new CheckResult();

        if(StringUtils.isEmpty(reptileParseClass.getContent())){
            log.error("配置为空");
            checkResult.addErrMsg("配置为空");
            return checkResult;
        }
        if(reptileParseClass.getContent().startsWith("package")){
            Matcher matcher = PACKAGE_NAME_PATTERN.matcher(reptileParseClass.getContent());
            if (matcher.find()) {
                checkResult.setPackageName(matcher.group(1));
            } else {
                log.error("策略类缺少包配置");
                checkResult.addErrMsg("策略类缺少包配置");
            }
        }else {
            log.error("策略类缺少包配置");
            checkResult.addErrMsg("策略类缺少包配置");
        }

        Matcher matcher = CLASS_NAME_PATTERN.matcher(reptileParseClass.getContent());
        if (matcher.find()) {
            checkResult.setClassName(matcher.group(1));
        } else {
            log.error("获取策略类名失败");
            checkResult.addErrMsg("获取策略类名失败");
        }

        if(strategyMap.containsKey(checkResult.getClassName())){
            log.error("策略类名重复");
            checkResult.addErrMsg("策略类名与已加载策略类名重复");
        }
        return checkResult;
    }


    /**
     * 加载新策略类
     * @param reptileParseClass
     */
    public synchronized  void loadClassAndRegisterBean(ReptileParseClass reptileParseClass){
        try {
            Class clz = compileAndLoad(reptileParseClass);
            registerBean(reptileParseClass.getClassName(), clz);
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

    @Data
    public static class CheckResult {
        private String packageName;
        private String className;
        private List<String> errMsg = new ArrayList<>();

        public  void addErrMsg(String errMsg) {
            this.errMsg.add(errMsg);
        }

        public String getErrMsgString() {
            return String.join(",", errMsg);
        }
    }
}
