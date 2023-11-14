package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.operationRecord.SysOperationRecordQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysOperationRecord;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysOperationRecordService extends IService<SysOperationRecord> {

    Page<SysOperationRecord> getSysDictionaryList(SysOperationRecordQuery sysOperationRecordQuery);
}
