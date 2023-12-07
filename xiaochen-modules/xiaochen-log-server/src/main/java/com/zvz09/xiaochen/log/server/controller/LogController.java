package com.zvz09.xiaochen.log.server.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.domain.LogQueryBody;
import com.zvz09.xiaochen.log.server.domain.OperationLogQueryBody;
import com.zvz09.xiaochen.log.server.domain.dto.EsPage;
import com.zvz09.xiaochen.log.server.service.LogEsService;
import com.zvz09.xiaochen.log.server.service.OperationLogEsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author zvz09
 */
@Slf4j
@RestController
@RequestMapping("")
@Tag(name = "日志查询")
@RequiredArgsConstructor
public class LogController {

    private final LogEsService logEsService;

    private final OperationLogEsService operationLogEsService;

    @PostMapping("/log/page")
    @Operation(summary = "系统日志搜索")
    public ApiResult<EsPage<LogIndex>> logPage(@Valid @RequestBody LogQueryBody logQueryBody) throws IOException {
        return ApiResult.success(logEsService.page(logQueryBody));
    }

    @PostMapping("/operation_log/page")
    @Operation(summary = "操作日志搜索")
    public ApiResult<EsPage<OperationLog>> operationLogPage(@Valid @RequestBody OperationLogQueryBody queryBody) throws IOException {
        return ApiResult.success(operationLogEsService.page(queryBody));
    }
}
