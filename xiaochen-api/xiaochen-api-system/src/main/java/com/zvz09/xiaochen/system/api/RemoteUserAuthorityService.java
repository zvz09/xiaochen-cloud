package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */

@FeignClient(contextId = "RemoteUserAuthorityService", path = FeignPath.USER_AUTHORITY, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteUserAuthorityService {

    /**
     * 根据用户Id查询角色编码
     *
     * @param userId
     * @return
     */
    @GetMapping("/getAuthorityIdByUserId")
    List<String> getAuthorityIdByUserId(@RequestParam(name = "userId") Long userId);

}
