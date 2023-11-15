package com.zvz09.xiaochen.system.api;


import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysDepartment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zvz09
 */
@FeignClient(contextId = "RemoteDepartmentService", path = FeignPath.DEPARTMENT, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteDepartmentService {

    @GetMapping("/getById")
    SysDepartment getById(Long deptId);
}
