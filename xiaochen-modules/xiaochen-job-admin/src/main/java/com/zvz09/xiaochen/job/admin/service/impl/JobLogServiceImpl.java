package com.zvz09.xiaochen.job.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;
import com.zvz09.xiaochen.job.admin.mapper.JobLogMapper;
import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import org.springframework.stereotype.Service;

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

}
