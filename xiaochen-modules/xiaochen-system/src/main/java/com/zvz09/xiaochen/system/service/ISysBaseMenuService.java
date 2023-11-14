package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.domain.dto.menu.AddMenuAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysBaseMenuDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysBaseMenuVo;
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
public interface ISysBaseMenuService extends IService<SysBaseMenu> {

    List<SysBaseMenu> getDefaultMenu(String defaultRouter,List<Long> sysBaseMenuIds);

    List<SysBaseMenuVo> getMenuTree();

    List<SysBaseMenuVo> getBaseMenuTree();

    void addMenuAuthority(AddMenuAuthorityDto addMenuAuthorityDto);

    List<SysBaseMenu> getMenuAuthority(Long authorityId);

    void addBaseMenu(SysBaseMenuDto sysBaseMenuDto);

    void deleteBaseMenu(Long sysBaseMenuId);

    void updateBaseMenu(SysBaseMenuDto sysBaseMenuDto);

    SysBaseMenuVo getBaseMenuById(Long menuId);

    Page<SysBaseMenuVo> getMenuList(BasePage basePage);
}
