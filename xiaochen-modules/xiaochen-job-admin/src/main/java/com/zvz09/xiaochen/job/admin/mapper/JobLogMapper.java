package com.zvz09.xiaochen.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    void deleteByTime(String operationTimeStr);
}
