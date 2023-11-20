package com.zvz09.xiaochen.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvz09.xiaochen.system.api.domain.entity.SysRolePermCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色与系统权限字对应关系表 Mapper 接口
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
public interface SysRolePermCodeMapper extends BaseMapper<SysRolePermCode> {

    List<Long> selectMenuIdByRoleId(@Param("roleId") Long roleId);
}
