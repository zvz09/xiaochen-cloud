package com.zvz09.xiaochen.system.controller;


import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.system.api.domain.dto.SysMicroDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysMicro;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;
import com.zvz09.xiaochen.system.service.ISysMicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2024-01-29
 */

@Slf4j
@RestController
@Tag(name = "微前端")
@RequestMapping("/micro")
public class SysMicroController extends BaseController<ISysMicroService, SysMicro, SysMicroDto> {

    private final ISysMicroService sysMicroService;

    public SysMicroController(ISysMicroService sysMicroService) {
        super.setBaseService(sysMicroService);
        this.sysMicroService = sysMicroService;
    }

    @GetMapping("/tree")
    public ApiResult<List<SysMicro>> listTree() {
        return ApiResult.success(sysMicroService.listTree());
    }

}

