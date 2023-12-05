package com.zvz09.xiaochen.log.server.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.domain.QueryBody;
import com.zvz09.xiaochen.log.server.service.EsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.biz.EsPageInfo;
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
@RequestMapping("/log/server")
@Tag(name = "日子搜索")
@RequiredArgsConstructor
public class LogController {

    private final EsService esService ;

    @PostMapping("/page")
    public ApiResult<EsPageInfo<LogIndex>> page(@Valid @RequestBody QueryBody queryBody) throws IOException {
        return ApiResult.success(esService.page(queryBody));
    }


}
