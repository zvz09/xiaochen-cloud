package com.zvz09.xiaochen.system.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.system.api.domain.dto.UpdateCasbinDto;
import com.zvz09.xiaochen.system.api.domain.vo.CasbinVo;
import com.zvz09.xiaochen.system.service.ICasbinRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zvz09
 */
@Slf4j
@RestController
@RequestMapping("/casbin")
@Tag(name = "CasbinRule")
@RequiredArgsConstructor
public class CasbinRuleController {
    private final ICasbinRuleService casbinRuleService;

    @GetMapping("/getPolicyPathByRoleId")
    public ApiResult<List<CasbinVo>> getPolicyPathByRoleId(String roleCode) {
        return ApiResult.success(casbinRuleService.listByRoleCode(roleCode));
    }


    @PostMapping("/updateCasbin")
    public ApiResult<String> updateCasbin(@RequestBody UpdateCasbinDto updateCasbinDto) {
        casbinRuleService.updateCasbin(updateCasbinDto);
        return ApiResult.success();
    }
}