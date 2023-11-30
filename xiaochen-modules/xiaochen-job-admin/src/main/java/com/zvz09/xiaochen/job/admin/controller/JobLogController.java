package com.zvz09.xiaochen.job.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.admin.domain.entity.JobLog;
import com.zvz09.xiaochen.job.admin.domain.vo.JobInfoVo;
import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
@RestController
@Tag(name ="job调度日志")
@RequiredArgsConstructor
@RequestMapping("/jobLog")
public class JobLogController {

    private final IJobLogService jobLogService;
    @Operation(summary = "列表查询")
    @PostMapping("/{jobId}/list")
    public ApiResult<Page<JobLog>> listJobLogPage(@PathVariable(value = "jobId")Long jobId, @RequestBody BasePage basePage) {
        return ApiResult.success(jobLogService.listJobLogPage(jobId,basePage));
    }
}

