package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.bo.UserRoleBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    List<UserRoleBo> getByUserIds(List<Long> userIds);

    List<UserRoleBo> getByRoleIds(List<Long> roleIds);

}
