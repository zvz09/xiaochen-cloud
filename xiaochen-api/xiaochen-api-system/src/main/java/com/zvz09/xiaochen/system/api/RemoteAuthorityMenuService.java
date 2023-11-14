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

@FeignClient(contextId = "RemoteAuthorityMenuService", path = FeignPath.AUTHORITY_MENU, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteAuthorityMenuService {

    /**
     * 根据角色代码获取菜单信息
     *
     * @param authorityId
     * @return
     */
    @GetMapping("/getMenuIdByAuthorityId")
    List<Long> getMenuIdByAuthorityId(@RequestParam(name = "authorityId") Long authorityId);

}
