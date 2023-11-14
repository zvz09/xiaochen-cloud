package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteBaseMenuService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.dto.menu.AddMenuAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysBaseMenuDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuBtn;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuParameter;
import com.zvz09.xiaochen.system.api.domain.vo.SysBaseMenuVo;
import com.zvz09.xiaochen.system.mapper.SysBaseMenuMapper;
import com.zvz09.xiaochen.system.service.ISysAuthorityMenuService;
import com.zvz09.xiaochen.system.service.ISysBaseMenuBtnService;
import com.zvz09.xiaochen.system.service.ISysBaseMenuParameterService;
import com.zvz09.xiaochen.system.service.ISysBaseMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Slf4j
@Service
@RestController
@Tag(name = "feign-菜单接口")
@RequestMapping(FeignPath.MENU)
@RequiredArgsConstructor
public class SysBaseMenuServiceImpl extends ServiceImpl<SysBaseMenuMapper, SysBaseMenu> implements ISysBaseMenuService, RemoteBaseMenuService {


    private final ISysAuthorityMenuService sysAuthorityMenuService;

    private final ISysBaseMenuBtnService sysBaseMenuBtnService;

    private final ISysBaseMenuParameterService sysBaseMenuParameterService;

    @Override
    public List<SysBaseMenu> getDefaultMenu(String defaultRouter, List<Long> sysBaseMenuIds) {
        return this.list(new LambdaQueryWrapper<SysBaseMenu>().eq(SysBaseMenu::getName,defaultRouter)
                .in(SysBaseMenu::getId, sysBaseMenuIds).orderByAsc(SysBaseMenu::getId));
    }

    @Override
    public List<SysBaseMenuVo> getMenuTree() {

        List<Long> menuIds = this.sysAuthorityMenuService.getMenuIdByAuthorityId(SecurityContextHolder.getAuthorityId());

        if (menuIds == null || menuIds.isEmpty()) {
            return null;
        }

        List<SysBaseMenu> menuList = this.list(new LambdaQueryWrapper<SysBaseMenu>().in(SysBaseMenu::getId, menuIds)
                .orderByAsc(SysBaseMenu::getSort));

        return this.buildTree(menuList);
    }

    @Override
    public List<SysBaseMenuVo> getBaseMenuTree() {
        return this.buildTree(this.list());
    }

    @Override
    public void addMenuAuthority(AddMenuAuthorityDto addMenuAuthorityDto) {
        this.sysAuthorityMenuService.addMenuAuthority(addMenuAuthorityDto.getAuthorityId(), addMenuAuthorityDto.getMenuIds());
    }

    @Override
    public List<SysBaseMenu> getMenuAuthority(Long authorityId) {

        List<Long> menuId = this.sysAuthorityMenuService.getMenuIdByAuthorityId(authorityId);

        return this.list(new LambdaQueryWrapper<SysBaseMenu>().in(SysBaseMenu::getId, menuId));
    }

    @Override
    @Transactional
    public void addBaseMenu(SysBaseMenuDto sysBaseMenuDto) {
        if (this.count(new LambdaQueryWrapper<SysBaseMenu>()
                .eq(SysBaseMenu::getName, sysBaseMenuDto.getName())) > 0) {
            throw new BusinessException("存在重复name，请修改name");
        }
        SysBaseMenu sysBaseMenu = sysBaseMenuDto.convertedToPo();
        this.save(sysBaseMenu);
        if (sysBaseMenuDto.getMenuBtn() != null && !sysBaseMenuDto.getMenuBtn().isEmpty()) {
            List<SysBaseMenuBtn> sysBaseMenuBtns = new ArrayList<>();
            sysBaseMenuDto.getMenuBtn().forEach(dto -> {
                SysBaseMenuBtn sysBaseMenuBtn = dto.convertedToPo();
                sysBaseMenuBtn.setSysBaseMenuId(sysBaseMenu.getId());
                sysBaseMenuBtns.add(sysBaseMenuBtn);
            });
            this.sysBaseMenuBtnService.saveBatch(sysBaseMenuBtns);
        }
        if (sysBaseMenuDto.getMenuParameter() != null && !sysBaseMenuDto.getMenuParameter().isEmpty()) {
            List<SysBaseMenuParameter> sysBaseMenuParameters = new ArrayList<>();
            sysBaseMenuDto.getMenuParameter().forEach(dto -> {
                SysBaseMenuParameter sysBaseMenuParameter = dto.convertedToPo();
                sysBaseMenuParameter.setSysBaseMenuId(sysBaseMenu.getId());
                sysBaseMenuParameters.add(sysBaseMenuParameter);
            });
            this.sysBaseMenuParameterService.saveBatch(sysBaseMenuParameters);
        }
    }

    @Override
    @Transactional
    public void deleteBaseMenu(Long sysBaseMenuId) {
        SysBaseMenu sysBaseMenu = this.getById(sysBaseMenuId);
        if (sysBaseMenu == null) {
            return;
        }

        List<Long> sysBaseMenuIds = new ArrayList<>();
        sysBaseMenuIds.add(sysBaseMenuId);
        List<SysBaseMenu> childMenus = this.list(new LambdaQueryWrapper<SysBaseMenu>().eq(SysBaseMenu::getParentId, sysBaseMenuId));
        if (childMenus != null && !childMenus.isEmpty()) {
            sysBaseMenuIds.addAll(childMenus.stream().map(SysBaseMenu::getId).toList());
        }

        //删除菜单
        this.sysAuthorityMenuService.deleteByMenuId(sysBaseMenuIds);
        this.sysBaseMenuBtnService.deleteByMenuId(sysBaseMenuIds);
        this.sysBaseMenuParameterService.deleteByMenuId(sysBaseMenuIds);

        this.update(new LambdaUpdateWrapper<SysBaseMenu>().eq(SysBaseMenu::getId, sysBaseMenuId).set(SysBaseMenu::getDeleted, true));
    }

    @Override
    @Transactional
    public void updateBaseMenu(SysBaseMenuDto sysBaseMenuDto) {
        SysBaseMenu sysBaseMenu = this.getById(sysBaseMenuDto.getId());
        if (sysBaseMenu == null) {
            return;
        }
        if (this.count(new LambdaQueryWrapper<SysBaseMenu>()
                .eq(SysBaseMenu::getName, sysBaseMenuDto.getName())
                .ne(SysBaseMenu::getId, sysBaseMenuDto.getId())) > 0) {
            throw new BusinessException("存在重复name，请修改name");
        }
        this.updateById(sysBaseMenuDto.convertedToPo());

        this.sysBaseMenuBtnService.deleteByMenuId(sysBaseMenuDto.getId());
        this.sysBaseMenuParameterService.deleteByMenuId(sysBaseMenuDto.getId());
        if (sysBaseMenuDto.getMenuBtn() != null && !sysBaseMenuDto.getMenuBtn().isEmpty()) {
            List<SysBaseMenuBtn> sysBaseMenuBtns = new ArrayList<>();
            sysBaseMenuDto.getMenuBtn().forEach(dto -> {
                SysBaseMenuBtn sysBaseMenuBtn = dto.convertedToPo();
                sysBaseMenuBtn.setSysBaseMenuId(sysBaseMenu.getId());
                sysBaseMenuBtns.add(sysBaseMenuBtn);
            });
            this.sysBaseMenuBtnService.saveBatch(sysBaseMenuBtns);
        }
        if (sysBaseMenuDto.getMenuParameter() != null && !sysBaseMenuDto.getMenuParameter().isEmpty()) {
            List<SysBaseMenuParameter> sysBaseMenuParameters = new ArrayList<>();
            sysBaseMenuDto.getMenuParameter().forEach(dto -> {
                SysBaseMenuParameter sysBaseMenuParameter = dto.convertedToPo();
                sysBaseMenuParameter.setSysBaseMenuId(sysBaseMenu.getId());
                sysBaseMenuParameters.add(sysBaseMenuParameter);
            });
            this.sysBaseMenuParameterService.saveBatch(sysBaseMenuParameters);
        }
    }

    @Override
    public SysBaseMenuVo getBaseMenuById(Long menuId) {
        SysBaseMenu sysBaseMenu = this.getById(menuId);
        if (sysBaseMenu == null) {
            throw new BusinessException("菜单不存在");
        }
        List<SysBaseMenuBtn> sysBaseMenuBtns =
                this.sysBaseMenuBtnService.list(new LambdaQueryWrapper<SysBaseMenuBtn>()
                        .eq(SysBaseMenuBtn::getSysBaseMenuId, menuId));
        List<SysBaseMenuParameter> sysBaseMenuParameters =
                this.sysBaseMenuParameterService.list(new LambdaQueryWrapper<SysBaseMenuParameter>()
                        .eq(SysBaseMenuParameter::getSysBaseMenuId, menuId));
        return new SysBaseMenuVo(sysBaseMenu, sysBaseMenuParameters, sysBaseMenuBtns);
    }

    @Override
    public Page<SysBaseMenuVo> getMenuList(BasePage basePage) {
        Page<SysBaseMenuVo> sysBaseMenuVoPage = new Page<>(basePage.getPage(), basePage.getPageSize());
        LambdaQueryWrapper<SysBaseMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(basePage.getKeyword()), SysBaseMenu::getName, basePage.getKeyword());
        queryWrapper.orderByAsc(SysBaseMenu::getId);
        Page<SysBaseMenu> page = this.page(new Page<>(basePage.getPage(), basePage.getPageSize()), queryWrapper);
        sysBaseMenuVoPage.setTotal(page.getTotal());
        sysBaseMenuVoPage.setRecords(this.buildTree(page.getRecords()));

        return sysBaseMenuVoPage;
    }

    private List<SysBaseMenuVo> buildTree(List<SysBaseMenu> menuList) {

        if (menuList == null || menuList.isEmpty()) {
            return null;
        }

        List<Long> menuIds = menuList.stream().map(SysBaseMenu::getId).collect(Collectors.toList());

        List<SysBaseMenuBtn> menuBtns = this.sysBaseMenuBtnService.listByMenuIds(menuIds);

        Map<Long, List<SysBaseMenuBtn>> menuBtnMap = menuBtns.stream().collect(Collectors.groupingBy(SysBaseMenuBtn::getSysBaseMenuId));


        List<SysBaseMenuVo> menuVos = new ArrayList<>();
        menuList.stream().filter(t -> "0".equals(t.getParentId()))
                .forEach((menu) -> {
                    SysBaseMenuVo sysBaseMenuVo = new SysBaseMenuVo(menu, menuBtnMap.get(menu.getId()));
                    sysBaseMenuVo.setChildren(this.getChildren(menu, menuList, menuBtnMap));
                    menuVos.add(sysBaseMenuVo);
                });
        return menuVos;
    }

    private List<SysBaseMenuVo> getChildren(SysBaseMenu root, List<SysBaseMenu> allMenus, Map<Long, List<SysBaseMenuBtn>> menuBtnMap) {
        List<SysBaseMenuVo> menuVos = new ArrayList<>();
        allMenus.stream()
                .filter(t -> t.getParentId().equals(String.valueOf(root.getId())))
                .forEach((menu) -> {
                    SysBaseMenuVo sysBaseMenuVo = new SysBaseMenuVo(menu, menuBtnMap.get(menu.getId()));
                    sysBaseMenuVo.setChildren(this.getChildren(menu, allMenus, menuBtnMap));
                    menuVos.add(sysBaseMenuVo);
                });

        return menuVos;
    }
}
