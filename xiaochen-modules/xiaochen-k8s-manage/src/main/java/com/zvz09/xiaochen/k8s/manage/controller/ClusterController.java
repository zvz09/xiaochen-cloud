package com.zvz09.xiaochen.k8s.manage.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.k8s.manage.domain.dto.ClusterDTO;
import com.zvz09.xiaochen.k8s.manage.service.IClusterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/cluster")
@Tag(name = "cluster接口")
@RequiredArgsConstructor
public class ClusterController {

    private final IClusterService clusterService;
    @Operation(summary = "新增cluster")
    @PostMapping("/")
    public ApiResult<String> create(@Valid @RequestBody ClusterDTO clusterDTO) {
        clusterService.create(clusterDTO);
        return ApiResult.success();
    }

    @Operation(summary = "删除cluster")
    @DeleteMapping("/")
    public ApiResult<String> delete(@Parameter(description = "id") Long id) {
        clusterService.delete(id);
        return ApiResult.success();
    }
}
