package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.dto.perm.SysPermCodeDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;

/**
 * <p>
 * 系统权限字 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
public interface ISysPermCodeService extends IService<SysPermCode> {

    void createAndSave(Long menuId, SysMenuDto sysMenuDto);

    SysPermCode getByMenuId(String parentId);

    void create(SysPermCodeDto sysPermCodeDto);

    void delete(Long id);

    void updatePermCode(SysPermCodeDto sysPermCodeDto);

    IPage<SysPermCodeVo> listTree(BasePage basePage);

}
