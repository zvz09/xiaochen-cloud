package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
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

@FeignClient(contextId = "RemoteUserRoleService", path = FeignPath.ROLE_AUTHORITY, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteUserRoleService {

    /**
     * 根据用户Id查询角色编码
     *
     * @param userId
     * @return
     */
    @GetMapping("/getRoleIdByUserId")
    List<String> getRoleIdByUserId(@RequestParam(name = "userId") Long userId);

}
