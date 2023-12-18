package com.zvz09.xiaochen.system.api;

import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */

@FeignClient(contextId = "RemoteUserService", path = FeignPath.USER, value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteUserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/getByUserName")
    SysUser getByUserName(@RequestParam(name = "username") String username);

    @GetMapping("/getById")
    SysUser getById(@RequestParam(name = "id") Long id);

    @GetMapping("/getByIds")
    Map<Long,SysUser> getByIds(@RequestParam(name = "id") List<Long> ids);

    @GetMapping("/getUserInfo")
    SysUserVo getUserInfo();
}
