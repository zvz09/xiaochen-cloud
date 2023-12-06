package com.zvz09.xiaochen.message.controller;

import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.log.annotation.BizNo;
import com.zvz09.xiaochen.message.service.IEventRemindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事件提醒 前端控制器
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */

@Slf4j
@RestController
@Tag(name = "事件提醒")
@RequestMapping("/remind")
@RequiredArgsConstructor
public class EventRemindController {

    private final IEventRemindService eventRemindService;

    @Operation(summary = "根据类型获取消息")
    @GetMapping("/page/byType")
    public ApiResult getByType(@RequestParam(required = false) String type, @RequestParam(required = false) Boolean state, BasePage basePage) {
        return ApiResult.success(eventRemindService.getByType(type, state, basePage));
    }

    @Operation(summary = "获取未读消息数")
    @GetMapping("/getAllUnRead")
    public ApiResult getAllUnRead() {
        return ApiResult.success(eventRemindService.getAllUnRead());
    }

    @Operation(summary = "确认消息")
    @PostMapping("/confirm")
    @BizNo(spEl = "{#ids}")
    public ApiResult confirm(@RequestBody List<Long> ids) {
        eventRemindService.confirm(ids);
        return ApiResult.success();
    }
}
