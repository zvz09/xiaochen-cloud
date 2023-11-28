package com.zvz09.xiaochen.job.admin.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
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

    private final DiscoveryClient discoveryClient;

    @Operation(summary = "获取所有已注册服务")
    @PostMapping(value = "/listServices")
    public ApiResult getAllServices(){
        return ApiResult.success(discoveryClient.getServices());
    }
}
