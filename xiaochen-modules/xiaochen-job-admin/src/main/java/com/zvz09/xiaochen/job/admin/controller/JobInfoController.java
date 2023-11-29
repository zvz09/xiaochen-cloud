package com.zvz09.xiaochen.job.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.job.admin.entity.JobInfo;
import com.zvz09.xiaochen.job.admin.service.IJobInfoService;
import com.zvz09.xiaochen.job.admin.service.ServeInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizili-YF0033
 */
@RestController
@RequestMapping("/jobinfo")
@Tag(name ="job信息")
@RequiredArgsConstructor
public class JobInfoController {

    private final ServeInstanceService serveInstanceService;
    private final IJobInfoService jobInfoService;
    @Operation(summary = "获取所有已注册服务")
    @PostMapping(value = "/listServices")
    public ApiResult getAllServices(){
        return ApiResult.success(serveInstanceService.getAllServiceName());
    }

    @Operation(summary = "新增任务信息")
    @PostMapping("/")
    public ApiResult<String> createJobInfo(@Valid @RequestBody JobInfo jobInfo) {
        jobInfoService.createJobInfo(jobInfo);
        return ApiResult.success();
    }

    @Operation(summary = "删除任务信息")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteJobInfo(@PathVariable(value = "id") Long id) {
        jobInfoService.deleteJobInfo(id);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping("/list")
    public ApiResult<Page<JobInfo>> listJobInfoPage(BasePage basePage) {
        return ApiResult.success(jobInfoService.listJobInfoPage(basePage));
    }

    @Operation(summary = "根据ID更新任务信息")
    @PostMapping("/update")
    public ApiResult<String> updateJobInfo(@RequestBody @Validated(value = {UpdateValidation.class, Default.class}) JobInfo jobInfo) {
        jobInfoService.updateJobInfo(jobInfo);
        return ApiResult.success();
    }

    @Operation(summary = "更新任务调度状态")
    @PutMapping("/status/{id}")
    public ApiResult<String> stopJobInfo(@PathVariable(value = "id") Long id) {
        jobInfoService.changeStatusJob(id);
        return ApiResult.success();
    }
}