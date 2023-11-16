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
import com.zvz09.xiaochen.system.api.domain.dto.user.RegisterUserDto;
import com.zvz09.xiaochen.system.api.domain.dto.user.SysUserQuery;
import com.zvz09.xiaochen.system.api.domain.dto.user.UpdateUserDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserAuthority;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import com.zvz09.xiaochen.system.mapper.SysUserMapper;
import com.zvz09.xiaochen.system.service.ISysAuthorityMenuService;
import com.zvz09.xiaochen.system.service.ISysAuthorityService;
import com.zvz09.xiaochen.system.service.ISysUserAuthorityService;
import com.zvz09.xiaochen.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final ISysAuthorityService sysAuthorityService;

    private final ISysUserAuthorityService sysUserAuthorityService;

    private final ISysAuthorityMenuService sysAuthorityMenuService;


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

        Page<SysUser> page = this.page(new Page<>(sysUserQuery.getPage(), sysUserQuery.getPageSize()), queryWrapper);

        List<Long> authorityIds = page.getRecords().stream().map(SysUser::getAuthorityId).collect(Collectors.toList());

        List<SysAuthority> sysAuthorities = this.sysAuthorityService.getByIds(authorityIds);
        Map<Long, SysAuthority> authorityMap = sysAuthorities.stream()
                .collect(Collectors.toMap(SysAuthority::getId, sysAuthority -> sysAuthority));
        return page.convert(sysUser -> {
            return new SysUserVo(sysUser, authorityMap.get(sysUser.getAuthorityId()));
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

        List<String> authorityCodes = sysUserAuthorityService.getAuthorityIdByUserId(sysUser.getId());

        SysAuthority sysAuthority = sysAuthorityService.getById(sysUser.getAuthorityId());

        List<SysAuthority> sysAuthorities = null;

        if (authorityCodes != null && !authorityCodes.isEmpty()) {
            sysAuthorities = sysAuthorityService.getByAuthorityCodes(authorityCodes);
        }

        List<Long> sysBaseMenuIds = sysAuthorityMenuService.getMenuIdByAuthorityId(sysUser.getAuthorityId());

        SysUserVo user = new SysUserVo(sysUser, sysAuthority, sysAuthorities);
        return user;
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
        sysUserAuthorityService.remove(new LambdaQueryWrapper<SysUserAuthority>().eq(SysUserAuthority::getSysUserId, updateUserDto.getId()));
        SysAuthority sysAuthority = sysAuthorityService.getById(updateUserDto.getAuthorityId());
        if (sysAuthority != null) {
            sysUserAuthorityService.save(new SysUserAuthority(updateUserDto.getId(), sysAuthority.getAuthorityCode()));
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
    public IPage<SysUserVo> getAllUser(SysUserQuery sysUserQuery) {
        //TODO 暂时全部返回
        sysUserQuery.setPageSize(Long.MAX_VALUE);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(sysUserQuery.getUserName()), SysUser::getUsername, sysUserQuery.getUserName());
        queryWrapper.eq(StringUtils.isNotBlank(sysUserQuery.getPhone()), SysUser::getPhone, sysUserQuery.getPhone());

        IPage<SysUser> page = this.page(new Page<>(sysUserQuery.getPage(), sysUserQuery.getPageSize()), queryWrapper);

        return page.convert(sysUser -> new SysUserVo(sysUser.getId(), sysUser.getNickName()));
    }
}
