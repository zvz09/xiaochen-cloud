package com.zvz09.xiaochen.job.admin.service.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.util.StringUtils;
import com.zvz09.xiaochen.job.admin.domain.dto.JobInfoDto;
import com.zvz09.xiaochen.job.admin.domain.entity.JobInfo;
import com.zvz09.xiaochen.job.admin.domain.vo.JobInfoVo;
import com.zvz09.xiaochen.job.admin.mapper.JobInfoMapper;
import com.zvz09.xiaochen.job.admin.runnable.RunJob;
import com.zvz09.xiaochen.job.admin.service.DynamicScheduledTaskRegistrar;
import com.zvz09.xiaochen.job.admin.service.IJobInfoService;
import com.zvz09.xiaochen.job.admin.service.ServeInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
@Service
@RequiredArgsConstructor
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements IJobInfoService {

    private static final String TASK_NAME_FORMAT = "%s-%s";

    private final ServeInstanceService serveInstanceService;

    private final DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar;

    @Override
    public void createJobInfo(JobInfoDto jobInfoDto) {
        JobInfo jobInfo = jobInfoDto.convertToJobInfo();
        if(this.count(new LambdaQueryWrapper<JobInfo>()
                .eq(JobInfo::getJobGroup,jobInfo.getJobGroup())
                .eq(JobInfo::getExecutorHandler,jobInfo.getExecutorHandler())) > 0){
            throw new BusinessException("已存在相同任务");
        }
        this.save(jobInfo);
        if(jobInfo.getTriggerStatus()){
            registerCronTask(jobInfo);
        }
    }

    @Override
    public void deleteJobInfo(Long id) {
        Optional.ofNullable(this.getById(id)).ifPresent(jobInfo -> {
            cancelCronTask(jobInfo);
            this.removeById(id);
        });
    }

    @Override
    public IPage<JobInfoVo> listJobInfoPage(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(),basePage.getPageSize()),
                new LambdaQueryWrapper<JobInfo>()
                        .like(StringUtils.isNotEmpty(basePage.getKeyword()),JobInfo::getExecutorHandler,basePage.getKeyword()))
                .convert(jobInfo -> {
                    List<Instance> instances =  serveInstanceService.getServiceInstances(jobInfo.getJobGroup());
                    return new JobInfoVo(jobInfo,instances);
                });
    }

    @Override
    public void updateJobInfo(JobInfoDto jobInfoDto) {
        JobInfo jobInfo = jobInfoDto.convertToJobInfo();
        Optional.ofNullable(this.getById(jobInfo.getId())).ifPresent(j -> {
            this.updateById(jobInfo);
            cancelCronTask(j);
            if(jobInfo.getTriggerStatus()){
                registerCronTask(jobInfo);
            }
        });
    }

    @Override
    public void changeStatusJob(Long id) {
        Optional.ofNullable(this.getById(id)).ifPresent(jobInfo -> {
            if(jobInfo.getTriggerStatus()){
                cancelCronTask(jobInfo);
            }else {
                registerCronTask(jobInfo);
            }
            jobInfo.setTriggerStatus(!jobInfo.getTriggerStatus());
            this.updateById(jobInfo);
        });
    }

    public void registerCronTask(JobInfo jobInfo) {
        dynamicScheduledTaskRegistrar.addCronTask(
                String.format(TASK_NAME_FORMAT, jobInfo.getJobGroup(), jobInfo.getExecutorHandler()),
                jobInfo.getScheduleConf(),
                new RunJob(jobInfo)
        );
    }

    public void cancelCronTask(JobInfo jobInfo) {
        dynamicScheduledTaskRegistrar.cancelCronTask(
                String.format(TASK_NAME_FORMAT, jobInfo.getJobGroup(), jobInfo.getExecutorHandler())
        );
    }

    @Override
    public void runJobInfo(Long id) {
        Optional.ofNullable(this.getById(id)).ifPresent(jobInfo -> {
            RunJob runJob = new RunJob(jobInfo);
            runJob.run();
        });
    }

}
