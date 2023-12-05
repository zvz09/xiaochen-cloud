package com.zvz09.xiaochen.job.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
public interface IJobLogService extends IService<JobLog> {
    IPage<JobLog> listJobLogPage(Long jobId,BasePage basePage);

    void deleteByTime(LocalDateTime dateTime);
}
