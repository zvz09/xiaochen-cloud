package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.domain.dto.role.CopySysRoleDto;
import com.zvz09.xiaochen.system.api.domain.dto.role.SysRoleDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.vo.SysRoleVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> getByRoleCodes(List<String> roleCodes);

    SysRole getByRoleCode(String roleCode);

    void createRole(SysRoleDto sysRoleDto);

    void copyRole(CopySysRoleDto copySysRoleDto);

    void deleteRole(Long id);

    void updateRole(SysRoleDto sysRoleDto);

    List<SysRole> getByIds(List<Long> roleIds);

    IPage<SysRoleVo> getRoleList(BasePage basePage);

    void bindPerm(Long roleId, List<Long> permIds);

    SysRoleVo detail(Long id);
}
