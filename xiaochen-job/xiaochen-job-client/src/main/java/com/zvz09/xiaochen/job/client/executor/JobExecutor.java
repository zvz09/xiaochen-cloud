package com.zvz09.xiaochen.job.client.executor;


import com.zvz09.xiaochen.job.client.handler.IJobHandler;
import com.zvz09.xiaochen.job.client.handler.impl.MethodJobHandler;
import com.zvz09.xiaochen.job.client.thread.JobThread;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    @Getter
    private static final ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();

    public Set<String> getAllHandlerNames() {
        return jobHandlerRepository.keySet();
    }
    @Override
    public void destroy() throws Exception {
        // destroy jobThreadRepository
        if (!jobThreadRepository.isEmpty()) {
            for (Map.Entry<Long, JobThread> item: jobThreadRepository.entrySet()) {
                JobThread oldJobThread = removeJobThread(item.getKey(), "web container destroy and kill the job.");
                // wait for job thread push result to callback queue
                if (oldJobThread != null) {
                    try {
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        log.error(">>>>>>>>>>> xxl-job, JobThread destroy(join) error, jobId:{}", item.getKey(), e);
                    }
                }
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();
    }

    @Override
    public void afterSingletonsInstantiated() {
        initJobHandlerMethodRepository(applicationContext);
    }

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobExecutor.applicationContext = applicationContext;
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);

        for (String beanDefinitionName : beanDefinitionNames) {

            // get bean
            Object bean = null;
            Lazy onBean = applicationContext.findAnnotationOnBean(beanDefinitionName, Lazy.class);
            if (onBean!=null){
                log.debug("annotation scan, skip @Lazy Bean:{}", beanDefinitionName);
                continue;
            }else {
                bean = applicationContext.getBean(beanDefinitionName);
            }

            // filter method
            Map<Method, XiaoChenJob> annotatedMethods = null;   // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<XiaoChenJob>() {
                            @Override
                            public XiaoChenJob inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, XiaoChenJob.class);
                            }
                        });
            } catch (Throwable ex) {
                log.error("method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            // generate and regist method job handler
            for (Map.Entry<Method, XiaoChenJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XiaoChenJob xiaoChenJob = methodXxlJobEntry.getValue();
                // regist
                registJobHandler(xiaoChenJob, bean, executeMethod);
            }

        }
    }

    public  IJobHandler loadJobHandler(String name){
        return jobHandlerRepository.get(name);
    }

    public  IJobHandler registJobHandler(String name, IJobHandler jobHandler){
        log.info(">>>>>>>>>>> xxl-job register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    protected void registJobHandler(XiaoChenJob xiaoChenJob, Object bean, Method executeMethod){
        if (xiaoChenJob == null) {
            return;
        }

        String name = xiaoChenJob.value();
        //make and simplify the variables since they'll be called several times later
        Class<?> clazz = bean.getClass();
        String methodName = executeMethod.getName();
        if (name.trim().length() == 0) {
            throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + clazz + "#" + methodName + "] .");
        }
        if (loadJobHandler(name) != null) {
            throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
        }

        executeMethod.setAccessible(true);

        // init and destroy
        Method initMethod = null;
        Method destroyMethod = null;

        if (!xiaoChenJob.init().trim().isEmpty()) {
            try {
                initMethod = clazz.getDeclaredMethod(xiaoChenJob.init());
                initMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + clazz + "#" + methodName + "] .");
            }
        }
        if (!xiaoChenJob.destroy().trim().isEmpty()) {
            try {
                destroyMethod = clazz.getDeclaredMethod(xiaoChenJob.destroy());
                destroyMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + clazz + "#" + methodName + "] .");
            }
        }

        // registry jobhandler
        registJobHandler(name, new MethodJobHandler(bean, executeMethod, initMethod, destroyMethod));
    }

    // ---------------------- job thread repository ----------------------
    private  ConcurrentMap<Long, JobThread> jobThreadRepository = new ConcurrentHashMap<Long, JobThread>();
    public  JobThread registJobThread(Long jobId, IJobHandler handler, String removeOldReason){
        JobThread newJobThread = new JobThread(jobId, handler, this);
        newJobThread.start();
        log.info(">>>>>>>>>>> xxl-job regist JobThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);	// putIfAbsent | oh my god, map's put method return the old value!!!
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }

    public  JobThread removeJobThread(Long jobId, String removeOldReason){
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();

            return oldJobThread;
        }
        return null;
    }

    public  JobThread loadJobThread(Long jobId){
        return jobThreadRepository.get(jobId);
    }
}
