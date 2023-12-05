package com.zvz09.xiaochen.job.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zvz09
 */
@Slf4j
@Service
public class DynamicScheduledTaskRegistrar extends ScheduledTaskRegistrar {

    private final Map<String, ScheduledTask> scheduledTaskMap = new LinkedHashMap<>(16);

    public DynamicScheduledTaskRegistrar(){
        super();
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(8);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("dynamic-scheduled-task-");
        taskScheduler.initialize();
        this.setScheduler(taskScheduler);
    }
    /**
     * 新增任务
     * @param taskName
     * @param cron
     * @param runnable
     */
    public void addCronTask(String taskName,String cron,Runnable runnable){
        if(scheduledTaskMap.containsKey(taskName)){
            log.error("定时任务["+ taskName+"]已存在，添加失败");
        }
        CronTask cronTask = new CronTask(runnable,cron);
        ScheduledTask scheduledTask = this.scheduleCronTask(cronTask);
        scheduledTaskMap.put(taskName,scheduledTask);
        log.info("定时任务["+taskName+"]新增成功");
    }

    /**
     * 删除任务
     * @param taskName
     */
    public void cancelCronTask(String taskName){
        ScheduledTask scheduledTask = scheduledTaskMap.get(taskName);
        if(null != scheduledTask){
            scheduledTask.cancel();
            scheduledTaskMap.remove(taskName);
        }
        log.info("定时任务["+taskName+"]删除成功");
    }

    @Override
    public void destroy() {
        super.destroy();
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
    }
}
