package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
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

@FeignClient(contextId = "RemoteRoleService", path = FeignPath.AUTHORITY, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteRoleService {

    /**
     * 根据角色代码获取角色信息
     *
     * @param roleCodes
     * @return
     */
    @GetMapping("/getByRoleCodes")
    List<SysRole> getByRoleCodes(@RequestParam(name = "roleCodes") List<String> roleCodes);

    @GetMapping("/getById")
    SysRole getById(@RequestParam(name = "roleId") Long roleId);

    @GetMapping("/getByRoleCode")
    SysRole getByRoleCode(@RequestParam(name = "roleCode") String roleCode);
}
