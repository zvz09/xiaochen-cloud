package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteAuthorityService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.dto.authority.CopySysAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.authority.SysAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthorityBtn;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthorityMenu;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserAuthority;
import com.zvz09.xiaochen.system.api.domain.vo.SysAuthorityVo;
import com.zvz09.xiaochen.system.mapper.SysAuthorityMapper;
import com.zvz09.xiaochen.system.mapper.SysUserAuthorityMapper;
import com.zvz09.xiaochen.system.service.ISysAuthorityBtnService;
import com.zvz09.xiaochen.system.service.ISysAuthorityMenuService;
import com.zvz09.xiaochen.system.service.ISysAuthorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
@Tag(name = "feign-角色接口")
@RequestMapping(FeignPath.AUTHORITY)
@RequiredArgsConstructor
public class SysAuthorityServiceImpl extends ServiceImpl<SysAuthorityMapper, SysAuthority> implements ISysAuthorityService, RemoteAuthorityService {

    private final SysUserAuthorityMapper sysUserAuthorityMapper;

    private final ISysAuthorityMenuService sysAuthorityMenuService;

    private final ISysAuthorityBtnService sysAuthorityBtnService;


    @Override
    public List<SysAuthority> getByAuthorityCodes(List<String> authorityCodes) {
        return this.list(new LambdaQueryWrapper<SysAuthority>().in(SysAuthority::getAuthorityCode, authorityCodes));
    }

    @Override
    public SysAuthority getById(Long authorityId) {
        return super.getById(authorityId);
    }

    @Override
    public SysAuthority getByAuthorityCode(String authorityCode) {
        return this.getOne(new LambdaQueryWrapper<SysAuthority>().eq(SysAuthority::getAuthorityCode, authorityCode));
    }

    @Override
    public List<SysAuthorityVo> getAuthorityTree() {

        List<SysAuthority> authorityList = this.list();

        if (authorityList == null || authorityList.isEmpty()) {
            return null;
        }

        List<SysAuthorityVo> authorityVoList = new ArrayList<>();
        authorityList.stream().filter(t -> t.getParentId() == 0)
                .forEach((authority) -> {
                    SysAuthorityVo sysAuthorityVo = new SysAuthorityVo(authority);
                    sysAuthorityVo.setChildren(this.getChildren(authority, authorityList));
                    authorityVoList.add(sysAuthorityVo);
                });
        return authorityVoList;
    }

    @Override
    public void createAuthority(SysAuthorityDto sysAuthorityDto) {
        SysAuthority sysAuthority =
                this.getOne(new LambdaQueryWrapper<SysAuthority>()
                        .eq(SysAuthority::getAuthorityCode, sysAuthorityDto.getAuthorityCode())
                        .or().eq(SysAuthority::getAuthorityName, sysAuthorityDto.getAuthorityName()));
        if (sysAuthority != null && sysAuthority.getId() != null) {
            throw new BusinessException("角色编码或角色名称已存在");
        }
        if (sysAuthorityDto.getParentId() != 0) {
            SysAuthority parentSysAuthority = this.getById(sysAuthorityDto.getParentId());
            if (parentSysAuthority == null || parentSysAuthority.getId() == null) {
                throw new BusinessException("所选父角色不存在");
            }
        }
        this.save(sysAuthorityDto.convertedToPo());
    }

    @Override
    @Transactional
    public void copyAuthority(CopySysAuthorityDto copySysAuthorityDto) {
        SysAuthority oldAuthority = this.getById(copySysAuthorityDto.getOldAuthorityId());
        if (oldAuthority == null) {
            throw new BusinessException("旧角色不存在");
        }
        SysAuthority sysAuthority =
                this.getOne(new LambdaQueryWrapper<SysAuthority>()
                        .eq(SysAuthority::getAuthorityCode, copySysAuthorityDto.getAuthority().getAuthorityCode())
                        .or().eq(SysAuthority::getAuthorityName, copySysAuthorityDto.getAuthority().getAuthorityName()));
        if (sysAuthority != null && sysAuthority.getId() != null) {
            throw new BusinessException("角色编码或角色名称已存在");
        }
        //保存角色
        SysAuthority newSysAuthority = copySysAuthorityDto.getAuthority().convertedToPo();
        this.save(newSysAuthority);

        //保存角色菜单
        List<Long> menuIds = this.sysAuthorityMenuService.list(new LambdaQueryWrapper<SysAuthorityMenu>()
                        .eq(SysAuthorityMenu::getSysAuthorityId, copySysAuthorityDto.getOldAuthorityId()))
                .stream()
                .map(SysAuthorityMenu::getSysBaseMenuId).toList();

        this.sysAuthorityMenuService.addMenuAuthority(newSysAuthority.getId(), menuIds);

        //保存角色按钮
        List<SysAuthorityBtn> btns = this.sysAuthorityBtnService.list(new LambdaQueryWrapper<SysAuthorityBtn>()
                .eq(SysAuthorityBtn::getAuthorityId, copySysAuthorityDto.getOldAuthorityId()));

        for (SysAuthorityBtn sysAuthorityBtn : btns) {
            sysAuthorityBtn.setAuthorityId(newSysAuthority.getId());
        }
        this.sysAuthorityBtnService.saveBatch(btns);

        //保存角色菜单
        //TODO
       /* List<List<String>> list = enforcer.getFilteredPolicy(0, oldAuthority.getAuthorityCode());
        list.forEach(l -> l.set(0, newSysAuthority.getAuthorityCode()));
        enforcer.removeFilteredPolicy(0, newSysAuthority.getAuthorityCode());
        enforcer.addPolicies(list);*/
    }

    @Override
    @Transactional
    public void deleteAuthority(Long id) {
        SysAuthority sysAuthority = this.getById(id);
        if (sysAuthority == null) {
            return;
        }
        if (sysUserAuthorityMapper.selectCount(new LambdaQueryWrapper<SysUserAuthority>()
                .eq(SysUserAuthority::getSysAuthorityCode, sysAuthority.getAuthorityCode())) > 0) {
            throw new BusinessException("此角色有用户正在使用禁止删除");
        }

        if (this.baseMapper.selectCount(new LambdaQueryWrapper<SysAuthority>()
                .eq(SysAuthority::getParentId, id)) > 0) {
            throw new BusinessException("此角色存在子角色不允许删除");
        }

        //TODO
        /* enforcer.removeFilteredPolicy(0, sysAuthority.getAuthorityCode());*/
        this.sysAuthorityMenuService.remove(new LambdaQueryWrapper<SysAuthorityMenu>().eq(SysAuthorityMenu::getSysAuthorityId, id));
        this.sysAuthorityBtnService.remove(new LambdaQueryWrapper<SysAuthorityBtn>().eq(SysAuthorityBtn::getAuthorityId, id));
        this.update(new LambdaUpdateWrapper<SysAuthority>().eq(SysAuthority::getId, id)
                .set(SysAuthority::getDeleted, 1));
    }

    @Override
    public void updateAuthority(SysAuthorityDto sysAuthorityDto) {
        LambdaUpdateWrapper<SysAuthority> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysAuthority::getAuthorityName, sysAuthorityDto.getAuthorityName());
        updateWrapper.set(SysAuthority::getParentId, sysAuthorityDto.getParentId());
        updateWrapper.eq(SysAuthority::getId, sysAuthorityDto.getId());
        this.update(updateWrapper);
    }

    @Override
    public List<SysAuthority> getByIds(List<Long> authorityIds) {
        return this.list(new LambdaQueryWrapper<SysAuthority>().in(SysAuthority::getId, authorityIds));
    }

    private List<SysAuthorityVo> getChildren(SysAuthority root, List<SysAuthority> allAuthorityList) {
        List<SysAuthorityVo> authorityVoList = new ArrayList<>();
        allAuthorityList.stream()
                .filter(t -> root.getId().equals(t.getParentId()))
                .forEach((authority) -> {
                    SysAuthorityVo sysAuthorityVo = new SysAuthorityVo(authority);
                    sysAuthorityVo.setChildren(this.getChildren(authority, allAuthorityList));
                    authorityVoList.add(sysAuthorityVo);
                });
        return authorityVoList;
    }
}
