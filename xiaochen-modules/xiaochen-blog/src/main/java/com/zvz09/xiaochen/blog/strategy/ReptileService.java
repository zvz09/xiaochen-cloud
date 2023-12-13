package com.zvz09.xiaochen.blog.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.blog.compiler.DynamicCompiler;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.domain.entity.ReptileClass;
import com.zvz09.xiaochen.blog.service.IReptileClassService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class ReptileService implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private final IReptileClassService reptileClassService;

    private final List<ReptileDataParserStrategy> strategies = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ReptileService.applicationContext = applicationContext;
        loadDBClass();
        Map<String, ReptileDataParserStrategy> serviceMap = applicationContext.getBeansOfType(ReptileDataParserStrategy.class);
        if (!serviceMap.isEmpty()) {
            List<ReptileDataParserStrategy> serviceList = new ArrayList<>(serviceMap.values());
            strategies.addAll(serviceList);
        }
    }

    public void loadDBClass(){
        List<ReptileClass> reptileClassList = reptileClassService.list();
        reptileClassList.forEach(reptileClass -> {
            try {
                String fullName = "com.zvz09.xiaochen.blog.strategy.impl."+reptileClass.getClassName();

                DynamicCompiler compiler = new DynamicCompiler();
                Class clz = compiler.compileAndLoad(fullName, reptileClass.getContent());

                //获取BeanFactory
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)ReptileService.applicationContext.getAutowireCapableBeanFactory();
                //创建bean信息
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
                //动态注册bean
                defaultListableBeanFactory.registerBeanDefinition(reptileClass.getClassName(),beanDefinitionBuilder.getBeanDefinition());

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 执行操作
     * @return
     */
    public ArticleDTO execute(String url, Document document) {
        for (ReptileDataParserStrategy strategy : strategies){
            if(strategy.isMatch(url)){
                return strategy.parseData(document);
            }
        }
        return null;
    }
}
