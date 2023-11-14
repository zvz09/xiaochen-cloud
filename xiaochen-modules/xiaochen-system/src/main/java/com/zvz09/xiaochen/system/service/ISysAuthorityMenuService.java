package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthorityMenu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysAuthorityMenuService extends IService<SysAuthorityMenu> {

    List<Long> getMenuIdByAuthorityId(Long authorityId);

    /**
     * @param authorityId
     * @param menuIds
     */
    void addMenuAuthority(Long authorityId, List<Long> menuIds);

    void deleteByMenuId(List<Long> sysBaseMenuIds);
}
