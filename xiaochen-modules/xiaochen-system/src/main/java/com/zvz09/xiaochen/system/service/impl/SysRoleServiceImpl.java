package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.RemoteRoleService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.dto.role.CopySysRoleDto;
import com.zvz09.xiaochen.system.api.domain.dto.role.SysRoleDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysRolePermCode;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserRole;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;
import com.zvz09.xiaochen.system.api.domain.vo.SysRoleVo;
import com.zvz09.xiaochen.system.mapper.SysRoleMapper;
import com.zvz09.xiaochen.system.mapper.SysUserRoleMapper;
import com.zvz09.xiaochen.system.service.ICasbinRuleService;
import com.zvz09.xiaochen.system.service.ISysPermCodeService;
import com.zvz09.xiaochen.system.service.ISysRolePermCodeService;
import com.zvz09.xiaochen.system.service.ISysRoleService;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService, RemoteRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;

    private final ICasbinRuleService casbinRuleService;

    private final ISysRolePermCodeService sysRolePermCodeService;
    private final ISysPermCodeService sysPermCodeService;

    @Override
    public List<SysRole> getByRoleCodes(List<String> roleCodes) {
        return this.list(new LambdaQueryWrapper<SysRole>().in(SysRole::getRoleCode, roleCodes));
    }

    @Override
    public SysRole getById(Long roleId) {
        return super.getById(roleId);
    }

    @Override
    public SysRole getByRoleCode(String roleCode) {
        return this.getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, roleCode));
    }

    @Override
    public IPage<SysRoleVo> getRoleList(BasePage basePage) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(basePage.getKeyword()), SysRole::getRoleName, basePage.getKeyword());

        Page<SysRole> page = this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()), queryWrapper);

        return page.convert(SysRoleVo::new);
    }

    @Override
    @Transactional
    public void bindPerm(Long roleId, List<Long> permIds) {
        sysRolePermCodeService.deleteByRoleId(roleId);
        List<SysRolePermCode> rolePermCodes = new ArrayList<>();
        if (permIds != null) {
            permIds.forEach(id -> {
                rolePermCodes.add(new SysRolePermCode(roleId, id));
            });
            sysRolePermCodeService.saveBatch(rolePermCodes);
        }
    }

    @Override
    public SysRoleVo detail(Long id) {
        SysRole sysRole = this.getById(id);
        if(sysRole == null){
            return null;
        }
        List<Long> permCodeIds = sysRolePermCodeService.getPermCodeIdByRoleId(id);
        if(permCodeIds !=null){
            List<SysPermCodeVo> voList = sysPermCodeService.listTree(permCodeIds);
            return new SysRoleVo(sysRole,permCodeIds,voList);
        }
        return new SysRoleVo(sysRole,new ArrayList<>(),new ArrayList<>());
    }

    @Override
    public void createRole(SysRoleDto sysRoleDto) {
        SysRole sysRole =
                this.getOne(new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, sysRoleDto.getRoleCode())
                        .or().eq(SysRole::getRoleName, sysRoleDto.getRoleName()));
        if (sysRole != null && sysRole.getId() != null) {
            throw new BusinessException("角色编码或角色名称已存在");
        }
        this.save(sysRoleDto.convertedToPo());
    }

    @Override
    @Transactional
    public void copyRole(CopySysRoleDto copySysRoleDto) {
        SysRole oldRole = this.getById(copySysRoleDto.getOldRoleId());
        if (oldRole == null) {
            throw new BusinessException("旧角色不存在");
        }
        SysRole sysRole =
                this.getOne(new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, copySysRoleDto.getRole().getRoleCode())
                        .or().eq(SysRole::getRoleName, copySysRoleDto.getRole().getRoleName()));
        if (sysRole != null && sysRole.getId() != null) {
            throw new BusinessException("角色编码或角色名称已存在");
        }
        //保存角色
        SysRole newSysRole = copySysRoleDto.getRole().convertedToPo();
        this.save(newSysRole);

        //保存权限
        casbinRuleService.copyRule(newSysRole.getRoleCode(), oldRole.getRoleCode());

    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        SysRole sysRole = this.getById(id);
        if (sysRole == null) {
            return;
        }
        if (sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getSysRoleCode, sysRole.getRoleCode())) > 0) {
            throw new BusinessException("此角色有用户正在使用禁止删除");
        }

        casbinRuleService.removeRule(sysRole.getRoleCode());

        this.update(new LambdaUpdateWrapper<SysRole>().eq(SysRole::getId, id)
                .set(SysRole::getDeleted, 1));
    }

    @Override
    public void updateRole(SysRoleDto sysRoleDto) {
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysRole::getRoleName, sysRoleDto.getRoleName());
        updateWrapper.eq(SysRole::getId, sysRoleDto.getId());
        this.update(updateWrapper);
    }

    @Override
    public List<SysRole> getByIds(List<Long> roleIds) {
        return this.list(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roleIds));
    }

}
