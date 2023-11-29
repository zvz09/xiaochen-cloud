package com.zvz09.xiaochen.job.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.job.admin.entity.JobInfo;
import com.zvz09.xiaochen.system.api.domain.vo.SysApiVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
public interface IJobInfoService extends IService<JobInfo> {

    void createJobInfo(JobInfo jobInfo);

    void deleteJobInfo(Long id);

    IPage<JobInfo> listJobInfoPage(BasePage basePage);

    void updateJobInfo(JobInfo jobInfo);

    void changeStatusJob(Long id);

    void registerCronTask(JobInfo jobInfo);

    void cancelCronTask(JobInfo jobInfo);
}
