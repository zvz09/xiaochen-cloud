package com.zvz09.xiaochen.job.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.job.admin.domain.dto.JobInfoDto;
import com.zvz09.xiaochen.job.admin.domain.entity.JobInfo;
import com.zvz09.xiaochen.job.admin.domain.vo.JobInfoVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
public interface IJobInfoService extends IService<JobInfo> {

    void createJobInfo(JobInfoDto jobInfoDto);

    void deleteJobInfo(Long id);

    IPage<JobInfoVo> listJobInfoPage(BasePage basePage);

    void updateJobInfo(JobInfoDto jobInfoDto);

    void changeStatusJob(Long id);

    void registerCronTask(JobInfo jobInfo);

    void cancelCronTask(JobInfo jobInfo);

    void runJobInfo(Long id);
}
