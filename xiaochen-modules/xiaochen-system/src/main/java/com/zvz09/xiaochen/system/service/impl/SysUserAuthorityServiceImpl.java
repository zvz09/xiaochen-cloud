package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.RemoteUserAuthorityService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserAuthority;
import com.zvz09.xiaochen.system.mapper.SysUserAuthorityMapper;
import com.zvz09.xiaochen.system.service.ISysUserAuthorityService;
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
@RequestMapping(FeignPath.USER_AUTHORITY)
@RequiredArgsConstructor
public class SysUserAuthorityServiceImpl extends ServiceImpl<SysUserAuthorityMapper, SysUserAuthority> implements ISysUserAuthorityService, RemoteUserAuthorityService {

    @Override
    public List<String> getAuthorityIdByUserId(Long userId) {
        return this.list(new LambdaQueryWrapper<SysUserAuthority>().eq(SysUserAuthority::getSysUserId, userId))
                .stream().map(SysUserAuthority::getSysAuthorityCode).collect(Collectors.toList());
    }

    @Override
    public List<SysUserAuthority> getByUserIds(List<Long> userIds) {
        return this.list(new LambdaQueryWrapper<SysUserAuthority>().in(SysUserAuthority::getSysUserId, userIds));
    }
}
