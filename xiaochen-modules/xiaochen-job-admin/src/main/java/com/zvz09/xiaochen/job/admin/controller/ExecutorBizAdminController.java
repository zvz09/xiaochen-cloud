package com.zvz09.xiaochen.job.admin.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;
import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import com.zvz09.xiaochen.job.core.ExecutorBizAdmin;
import com.zvz09.xiaochen.job.core.constant.FeignPath;
import com.zvz09.xiaochen.job.core.model.HandleCallbackParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zvz09
 */
@Slf4j
@RestController
@RequestMapping(FeignPath.JOB_ADMIN)
@Tag(name ="客户端回调")
@RequiredArgsConstructor
public class ExecutorBizAdminController implements ExecutorBizAdmin {

    private final IJobLogService jobLogService;
    @Override
    @Transactional
    public ApiResult<String> callback(HandleCallbackParam handleCallbackParam) {
        JobLog jobLog = new JobLog();
        jobLog.setId(handleCallbackParam.getLogId());
        jobLog.setHandleTime(handleCallbackParam.getLogDateTim());
        jobLog.setHandleCode(handleCallbackParam.getHandleCode());
        jobLog.setHandleMsg(handleCallbackParam.getHandleMsg());
        jobLogService.updateById(jobLog);
        return ApiResult.success();
    }
}
