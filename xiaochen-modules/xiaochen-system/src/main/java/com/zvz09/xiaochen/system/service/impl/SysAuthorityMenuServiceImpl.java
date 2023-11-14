package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.RemoteAuthorityMenuService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthorityMenu;
import com.zvz09.xiaochen.system.mapper.SysAuthorityMenuMapper;
import com.zvz09.xiaochen.system.service.ISysAuthorityMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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
@Tag(name = "feign-角色菜单接口")
@RequestMapping(FeignPath.AUTHORITY_MENU)
@RequiredArgsConstructor
public class SysAuthorityMenuServiceImpl extends ServiceImpl<SysAuthorityMenuMapper, SysAuthorityMenu> implements ISysAuthorityMenuService, RemoteAuthorityMenuService {

    @Override
    public List<Long> getMenuIdByAuthorityId(Long authorityId) {
        return this.list(new LambdaQueryWrapper<SysAuthorityMenu>().eq(SysAuthorityMenu::getSysAuthorityId, authorityId))
                .stream().map(SysAuthorityMenu::getSysBaseMenuId).collect(Collectors.toList());
    }


    /**
     * @param authorityId
     * @param menuIds
     */
    @Override
    @Transactional
    public void addMenuAuthority(Long authorityId, List<Long> menuIds) {
        List<SysAuthorityMenu> sysAuthorityMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            sysAuthorityMenuList.add(new SysAuthorityMenu(menuId, authorityId));
        }
        this.remove(new LambdaQueryWrapper<SysAuthorityMenu>().eq(SysAuthorityMenu::getSysAuthorityId, authorityId));
        this.saveBatch(sysAuthorityMenuList);
    }

    @Override
    public void deleteByMenuId(List<Long> sysBaseMenuIds) {
        this.remove(new LambdaQueryWrapper<SysAuthorityMenu>().in(SysAuthorityMenu::getSysBaseMenuId, sysBaseMenuIds));
    }
}
