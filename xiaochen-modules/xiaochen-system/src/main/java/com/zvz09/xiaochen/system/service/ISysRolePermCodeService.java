package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.entity.SysRolePermCode;

import java.util.List;

/**
 * <p>
 * 角色与系统权限字对应关系表 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
public interface ISysRolePermCodeService extends IService<SysRolePermCode> {

    List<Long> getMenuIdByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    List<Long> getPermCodeIdByRoleId(Long roleId);
}
