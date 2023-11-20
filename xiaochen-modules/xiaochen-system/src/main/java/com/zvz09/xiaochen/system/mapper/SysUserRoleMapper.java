package com.zvz09.xiaochen.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvz09.xiaochen.system.api.domain.bo.UserRoleBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<UserRoleBo> getByUserIds(List<Long> userIds);

    List<UserRoleBo> getByRoleIds(List<Long> roleIds);
}
