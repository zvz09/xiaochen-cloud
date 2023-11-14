package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
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

@FeignClient(contextId = "RemoteAuthorityService", path = FeignPath.AUTHORITY, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteAuthorityService {

    /**
     * 根据角色代码获取角色信息
     *
     * @param authorityCodes
     * @return
     */
    @GetMapping("/getByAuthorityCodes")
    List<SysAuthority> getByAuthorityCodes(@RequestParam(name = "authorityCodes") List<String> authorityCodes);

    @GetMapping("/getById")
    SysAuthority getById(@RequestParam(name = "authorityId") Long authorityId);

    @GetMapping("/getByAuthorityCode")
    SysAuthority getByAuthorityCode(@RequestParam(name = "authorityCode")String authorityCode);
}
