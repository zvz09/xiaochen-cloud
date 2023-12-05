package com.zvz09.xiaochen.job.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;
import com.zvz09.xiaochen.job.admin.mapper.JobLogMapper;
import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements IJobLogService {


    @Override
    public IPage<JobLog> listJobLogPage(Long jobId,BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(),basePage.getPageSize()),
                new LambdaQueryWrapper<JobLog>().eq(JobLog::getJobId,jobId));
    }

    @Override
    public void deleteByTime(LocalDateTime dateTime) {
        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String operationTimeStr = dateTime.format(aFormatter);
        this.baseMapper.deleteByTime(operationTimeStr);
    }
}
