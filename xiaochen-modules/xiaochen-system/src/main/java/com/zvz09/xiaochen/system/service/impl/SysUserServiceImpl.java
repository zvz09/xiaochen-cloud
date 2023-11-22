package com.zvz09.xiaochen.system.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.bo.UserRoleBo;
import com.zvz09.xiaochen.system.api.domain.dto.user.RegisterUserDto;
import com.zvz09.xiaochen.system.api.domain.dto.user.SysUserQuery;
import com.zvz09.xiaochen.system.api.domain.dto.user.UpdateUserDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserRole;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import com.zvz09.xiaochen.system.mapper.SysUserMapper;
import com.zvz09.xiaochen.system.service.ISysRoleService;
import com.zvz09.xiaochen.system.service.ISysUserRoleService;
import com.zvz09.xiaochen.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
@Tag(name = "feign-用户接口")
@RequestMapping(FeignPath.USER)
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService, RemoteUserService {

    private final ISysRoleService sysRoleService;

    private final ISysUserRoleService sysUserRoleService;


    @Override
    public SysUser getByUserName(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public SysUser getById(Long id) {
        return super.getById(id);
    }

    @Override
    public IPage<SysUserVo> getUserList(SysUserQuery sysUserQuery) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(sysUserQuery.getUserName()), SysUser::getUsername, sysUserQuery.getUserName());
        queryWrapper.eq(StringUtils.isNotBlank(sysUserQuery.getPhone()), SysUser::getPhone, sysUserQuery.getPhone());

        Page<SysUser> page = this.page(new Page<>(sysUserQuery.getPageNum(), sysUserQuery.getPageSize()), queryWrapper);

        List<Long> userIds = page.getRecords().stream().map(SysUser::getId).toList();

        Map<Long, List<SysRole>> roleMap;
        if (!userIds.isEmpty()) {
            roleMap = this.sysUserRoleService.getByUserIds(userIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            UserRoleBo::getUserId,
                            Collectors.mapping(UserRoleBo::convertedToRole, Collectors.toList())
                    ));
        } else {
            roleMap = new HashMap<>();
        }

        return page.convert(sysUser -> {
            return new SysUserVo(sysUser, roleMap.get(sysUser.getId()));
        });
    }

    @Override
    public SysUserVo getUserInfo() {
        SysUser sysUser = this.getById(SecurityContextHolder.getUserId());
        if (sysUser == null) {
            throw new BusinessException("未登录或用户不存在");
        }

        if (sysUser.getEnable() != 1L) {
            throw new BusinessException("登陆失败! 用户被禁止登录!");
        }

        List<UserRoleBo> userRoleBos = this.sysUserRoleService.getByUserIds(Collections.singletonList(sysUser.getId()));

        List<SysRole> sysRole = new ArrayList<>();

        if (userRoleBos != null && !userRoleBos.isEmpty()) {
            userRoleBos.forEach(userRoleBo -> {
                sysRole.add(userRoleBo.convertedToRole());
            });
        }

        return new SysUserVo(sysUser, sysRole);
    }

    @Override
    public void register(RegisterUserDto registerUserDto) {
        if (this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, registerUserDto.getUserName())) > 0) {
            throw new BusinessException("用户名已注册");
        }
        this.save(registerUserDto.convertedToPo());
    }

    @Override
    public void resetPassword(Long userId) {
        SysUser sysUser = this.getById(userId);
        if (sysUser == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysUser>().eq(SysUser::getId, userId)
                .set(SysUser::getPassword, BCrypt.hashpw("123456", BCrypt.gensalt())));
    }

    @Override
    @Transactional
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        SysUser sysUser = this.getById(updateUserDto.getId());
        if (sysUser == null) {
            return;
        }
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getSysUserId, updateUserDto.getId()));
        List<SysRole> sysRoles = sysRoleService.getByIds(updateUserDto.getRoleIds());
        if (sysRoles != null) {
            List<SysUserRole> sysUserRoles = new ArrayList<>();
            sysRoles.forEach(s -> {
                sysUserRoles.add(new SysUserRole(updateUserDto.getId(), s.getRoleCode()));
            });
            sysUserRoleService.saveBatch(sysUserRoles);
        }
        this.updateById(updateUserDto.convertedToPo());
    }

    @Override
    public void deleteUser(Long id) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysUser>().eq(SysUser::getId, id)
                .set(SysUser::getDeleted, 1));
    }

    @Override
    public IPage<SysUserVo> simpleList(SysUserQuery sysUserQuery) {
        //TODO 暂时全部返回
        sysUserQuery.setPageSize(Long.MAX_VALUE);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(sysUserQuery.getUserName()), SysUser::getUsername, sysUserQuery.getUserName());
        queryWrapper.eq(StringUtils.isNotBlank(sysUserQuery.getPhone()), SysUser::getPhone, sysUserQuery.getPhone());

        IPage<SysUser> page = this.page(new Page<>(sysUserQuery.getPageNum(), sysUserQuery.getPageSize()), queryWrapper);

        return page.convert(sysUser -> new SysUserVo(sysUser.getId(), sysUser.getNickName()));
    }

    @Override
    public void createUser(UpdateUserDto updateUserDto) {
        if (this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, updateUserDto.getUserName())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        SysUser sysUser = updateUserDto.convertedToPo();
        sysUser.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        this.save(sysUser);
        List<SysRole> sysRoles = sysRoleService.getByIds(updateUserDto.getRoleIds());
        if (sysRoles != null) {
            List<SysUserRole> sysUserRoles = new ArrayList<>();
            sysRoles.forEach(s -> {
                sysUserRoles.add(new SysUserRole(sysUser.getId(), s.getRoleCode()));
            });
            sysUserRoleService.saveBatch(sysUserRoles);
        }
    }
}
