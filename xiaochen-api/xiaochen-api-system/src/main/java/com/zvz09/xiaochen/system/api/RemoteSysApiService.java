package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lizili-YF0033
 */
@FeignClient(contextId = "RemoteSysApiService", path = FeignPath.API, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteSysApiService {

    @GetMapping("/list")
    List<SysApi> list(@RequestParam(name = "serviceName") String serviceName);

    @PostMapping("/list")
    void saveBatch(@RequestBody List<SysApi> sysApis);
}
