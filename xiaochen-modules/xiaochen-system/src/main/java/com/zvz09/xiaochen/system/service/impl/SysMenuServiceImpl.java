package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;
import com.zvz09.xiaochen.system.mapper.SysMenuMapper;
import com.zvz09.xiaochen.system.service.ISysAuthorityMenuService;
import com.zvz09.xiaochen.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单表 服务实现类
 *
 * @author zvz09
 * @date 2023-11-16 15:40:03
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysAuthorityMenuService sysAuthorityMenuService;

    @Override
    public List<SysMenuVo> listTree() {
        List<Long> menuIds = this.sysAuthorityMenuService.getMenuIdByAuthorityId(SecurityContextHolder.getAuthorityId());

        if (menuIds == null || menuIds.isEmpty()) {
            return null;
        }
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getId, menuIds)
                .orderByAsc(SysMenu::getSort));

        return this.buildTree(menuList);
    }
    private List<SysMenuVo> buildTree(List<SysMenu> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return null;
        }
        List<Long> menuIds = menuList.stream().map(SysMenu::getId).collect(Collectors.toList());

        List<SysMenuVo> menuVos = new ArrayList<>();
        menuList.stream().filter(t -> "0".equals(t.getParentId()))
                .forEach((menu) -> {
                    SysMenuVo sysBaseMenuVo = new SysMenuVo(menu);
                    sysBaseMenuVo.setChildren(this.getChildren(menu, menuList));
                    menuVos.add(sysBaseMenuVo);
                });
        return menuVos;
    }

    private List<SysMenuVo> getChildren(SysMenu root, List<SysMenu> allMenus) {
        List<SysMenuVo> menuVos = new ArrayList<>();
        allMenus.stream()
                .filter(t -> t.getParentId().equals(String.valueOf(root.getId())))
                .forEach((menu) -> {
                    SysMenuVo sysBaseMenuVo = new SysMenuVo(menu);
                    sysBaseMenuVo.setChildren(this.getChildren(menu, allMenus));
                    menuVos.add(sysBaseMenuVo);
                });
        return menuVos;
    }
}

