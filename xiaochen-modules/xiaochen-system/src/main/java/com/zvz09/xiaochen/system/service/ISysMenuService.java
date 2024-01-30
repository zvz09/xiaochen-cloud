package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;

import java.util.List;


/**
 * 菜单表 服务类
 *
 * @author zvz09
 * @date 2023-11-16 15:40:03
 */

public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenuVo> listTree();

    void createMenu(SysMenuDto sysMenuDto);

    void deleteMenu(Long id);

    void updateMenu(SysMenuDto sysMenuDto);

    List<SysMenuVo> listTree(String microName);
}

