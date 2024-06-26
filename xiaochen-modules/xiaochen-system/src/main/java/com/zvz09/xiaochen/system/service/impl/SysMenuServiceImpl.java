package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.util.TreeBuilder;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;
import com.zvz09.xiaochen.system.converter.SysMenuTreeConverter;
import com.zvz09.xiaochen.system.mapper.SysMenuMapper;
import com.zvz09.xiaochen.system.service.ISysMenuService;
import com.zvz09.xiaochen.system.service.ISysPermCodeService;
import com.zvz09.xiaochen.system.service.ISysRolePermCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.zvz09.xiaochen.common.core.constant.Constants.SUPER_ADMIN;


/**
 * 菜单表 服务实现类
 *
 * @author zvz09
 * @date 2023-11-16 15:40:03
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysPermCodeService sysPermCodeService;
    private final ISysRolePermCodeService sysRolePermCodeService;

    @Override
    public List<SysMenuVo> listTree() {
        List<SysMenu> menuList ;
        if (SUPER_ADMIN.equals(SecurityContextHolder.getRoleCode())) {
            menuList = this.list();
        } else {
            List<Long> menuIds = this.sysRolePermCodeService.getMenuIdByRoleId(SecurityContextHolder.getRoleId());
            if (menuIds == null || menuIds.isEmpty()) {
                return null;
            }
            menuList = this.list(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getId, menuIds)
                    .orderByAsc(SysMenu::getSort));
        }
        Comparator<SysMenuVo> customComparator = Comparator.comparing(SysMenuVo::getSort);
        return new TreeBuilder<SysMenu, SysMenuVo>(t -> t.getParentId() == 0L)
                .buildTree(menuList, new SysMenuTreeConverter(),customComparator);

    }

    @Override
    @Transactional
    public void createMenu(SysMenuDto sysMenuDto) {
        if (this.count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getName, sysMenuDto.getName())) > 0) {
            throw new BusinessException("存在重复name，请修改name");
        }
        SysMenu sysMenu = sysMenuDto.convertedToPo();
        this.save(sysMenu);
        sysPermCodeService.createAndSave(sysMenu.getId(), sysMenuDto);
    }


    @Override
    public void deleteMenu(Long id) {
        this.remove(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getId, id));
    }

    @Override
    public void updateMenu(SysMenuDto sysMenuDto) {
        SysMenu sysBaseMenu = this.getById(sysMenuDto.getId());
        if (sysBaseMenu == null) {
            return;
        }
        if (this.count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getName, sysMenuDto.getName())
                .ne(SysMenu::getId, sysMenuDto.getId())) > 0) {
            throw new BusinessException("存在重复name，请修改name");
        }
        this.updateById(sysMenuDto.convertedToPo());
        sysPermCodeService.updatePermCode(sysMenuDto.getId(), sysMenuDto);
    }

    @Override
    public List<SysMenuVo> listTree(String microName) {
        SysMenu baseMenu = this.getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getName,microName));
        if(baseMenu == null){
            return new ArrayList<>();
        }
        List<Long> menuIds;
        if (SUPER_ADMIN.equals(SecurityContextHolder.getRoleCode())) {
            menuIds = this.list().stream().map(SysMenu::getId).toList();
        } else {
            menuIds = this.sysRolePermCodeService.getMenuIdByRoleId(SecurityContextHolder.getRoleId());
            if (menuIds == null || menuIds.isEmpty()) {
                return new ArrayList<>();
            }

        }
        if(!menuIds.contains(baseMenu.getId())){
            return new ArrayList<>();
        }
        Comparator<SysMenuVo> customComparator = Comparator.comparing(SysMenuVo::getSort);
        return new TreeBuilder<SysMenu, SysMenuVo>(t -> Objects.equals(t.getParentId(), baseMenu.getId()))
                .buildTree(this.list(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getId, menuIds)
                        .orderByAsc(SysMenu::getSort)), new SysMenuTreeConverter(),customComparator);
    }
}

