package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.operationRecord.SysOperationRecordQuery;
import com.zvz09.xiaochen.system.service.ISysOperationRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Slf4j
@RestController
@Tag(name = "操作日志")
@RequestMapping("/operationRecord")
@RequiredArgsConstructor
public class SysOperationRecordController {

    private final ISysOperationRecordService sysOperationRecordService;

    @GetMapping("/getSysOperationRecordList")
    public ApiResult getSysDictionaryList(SysOperationRecordQuery sysOperationRecordQuery) {
        return ApiResult.success(sysOperationRecordService.getSysDictionaryList(sysOperationRecordQuery));
    }
}
