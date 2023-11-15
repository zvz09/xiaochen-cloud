package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
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

@FeignClient(contextId = "RemoteBaseMenuService", path = FeignPath.MENU, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteBaseMenuService {

    /**
     * 获取角色默认菜单信息
     *
     * @param defaultRouter
     * @param sysBaseMenuIds
     * @return
     */
    @GetMapping("/getDefaultMenu")
    List<SysBaseMenu> getDefaultMenu(@RequestParam(name = "defaultRouter")String defaultRouter, @RequestParam(name = "sysBaseMenuIds") List<Long> sysBaseMenuIds);
}
