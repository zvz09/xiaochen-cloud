package com.zvz09.xiaochen.autocode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.autocode.domain.entity.SysGenCodeHistory;
import com.zvz09.xiaochen.autocode.service.ISysGenCodeHistoryService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 历史记录 前端控制器
 *
 * @author zvz09
 * @date 2023-10-11 10:42:55
 */

@Slf4j
@RestController
@Tag(name = "历史记录")
@RequestMapping("/gencode")
@RequiredArgsConstructor
public class SysGenCodeHistoryController {

    private final ISysGenCodeHistoryService SysGenCodeHistoryService;

    @Operation(summary = "分页查询历史记录")
    @PostMapping("/page")
    public ApiResult<Page<SysGenCodeHistory>> getGenCodeHistoryList(@RequestBody BasePage queryDto) {
        return ApiResult.success(SysGenCodeHistoryService.getGenCodeHistoryList(queryDto));
    }

}
