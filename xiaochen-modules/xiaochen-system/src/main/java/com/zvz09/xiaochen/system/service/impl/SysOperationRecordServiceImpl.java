package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.domain.dto.operationRecord.SysOperationRecordQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysOperationRecord;
import com.zvz09.xiaochen.system.mapper.SysOperationRecordMapper;
import com.zvz09.xiaochen.system.service.ISysOperationRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Service
public class SysOperationRecordServiceImpl extends ServiceImpl<SysOperationRecordMapper, SysOperationRecord> implements ISysOperationRecordService {

    @Override
    public Page<SysOperationRecord> getSysDictionaryList(SysOperationRecordQuery sysOperationRecordQuery) {
        return this.page(new Page<>(sysOperationRecordQuery.getPage(), sysOperationRecordQuery.getPageSize()),
                new LambdaQueryWrapper<SysOperationRecord>()
                        .eq(StringUtils.isNotBlank(sysOperationRecordQuery.getMethod()), SysOperationRecord::getMethod, sysOperationRecordQuery.getMethod())
                        .eq(sysOperationRecordQuery.getStatus() != null, SysOperationRecord::getStatus, sysOperationRecordQuery.getStatus())
                        .like(StringUtils.isNotBlank(sysOperationRecordQuery.getPath()), SysOperationRecord::getPath, sysOperationRecordQuery.getPath())
                        .orderByDesc(SysOperationRecord::getCreatedAt));
    }
}
