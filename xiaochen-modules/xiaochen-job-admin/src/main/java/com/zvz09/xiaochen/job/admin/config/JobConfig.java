package com.zvz09.xiaochen.job.admin.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.job.admin.entity.JobInfo;
import com.zvz09.xiaochen.job.admin.runnable.RunJob;
import com.zvz09.xiaochen.job.admin.service.DynamicScheduledTaskRegistrar;
import com.zvz09.xiaochen.job.admin.service.IJobInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobConfig implements ApplicationRunner {

    private final IJobInfoService jobInfoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<JobInfo> jobInfos =
                this.jobInfoService.list(new LambdaQueryWrapper<JobInfo>().eq(JobInfo::getTriggerStatus,true));

        for (JobInfo jobInfo :jobInfos){
           jobInfoService.registerCronTask(jobInfo);
        }
    }
}
