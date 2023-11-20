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

@FeignClient(contextId = "RemoteRoleMenuService", path = FeignPath.ROLE_MENU, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteRoleMenuService {

    /**
     * 根据角色代码获取菜单信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getMenuIdByRoleId")
    List<Long> getMenuIdByRoleId(@RequestParam(name = "roleId") Long roleId);

}
