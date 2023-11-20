package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.RemoteUserRoleService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.bo.UserRoleBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserRole;
import com.zvz09.xiaochen.system.mapper.SysUserRoleMapper;
import com.zvz09.xiaochen.system.service.ISysUserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "feign-用户角色接口")
@RequestMapping(FeignPath.ROLE_AUTHORITY)
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService, RemoteUserRoleService {

    @Override
    public List<String> getRoleIdByUserId(Long userId) {
        return this.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getSysUserId, userId))
                .stream().map(SysUserRole::getSysRoleCode).collect(Collectors.toList());
    }

    @Override
    public List<UserRoleBo> getByUserIds(List<Long> userIds) {
        return this.baseMapper.getByUserIds(userIds);
    }

    public List<UserRoleBo> getByRoleIds(List<Long> roleIds) {
        return this.baseMapper.getByRoleIds(roleIds);
    }
}
