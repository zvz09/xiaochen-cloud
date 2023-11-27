/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.auth.service.impl
 * @className com.zvz09.xiaochen.auth.service.impl.LoginServiceImpl
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.zvz09.xiaochen.auth.dto.LoginDto;
import com.zvz09.xiaochen.auth.service.ILoginService;
import com.zvz09.xiaochen.auth.vo.LoginVo;
import com.zvz09.xiaochen.common.core.constant.LoginConstant;
import com.zvz09.xiaochen.common.core.constant.SecurityConstants;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.jwt.JwtUtils;
import com.zvz09.xiaochen.system.api.RemoteRoleMenuService;
import com.zvz09.xiaochen.system.api.RemoteRoleService;
import com.zvz09.xiaochen.system.api.RemoteUserRoleService;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.bo.UserRoleBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import io.lettuce.core.AbstractRedisClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LoginServiceImpl
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/30 13:36
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final RedisTemplate<String,String> redisTemplate;

    private final RemoteUserService remoteUserService;


    private final RemoteUserRoleService remoteUserRoleService;


    @Override
    public LoginVo doLogin(LoginDto loginDto) {

        SysUser sysUser = remoteUserService.getByUserName(loginDto.getUsername());

        if (sysUser == null) {
            throw new BusinessException("登陆失败! 用户名不存在或者密码错误!");
        }

        if (sysUser.getEnable() != 1L) {
            throw new BusinessException("登陆失败! 用户被禁止登录!");
        }

        if (!BCrypt.checkpw(loginDto.getPassword(), sysUser.getPassword())) {
            throw new BusinessException("登陆失败! 用户名不存在或者密码错误!");
        }

        List<String> roleCodes = remoteUserRoleService.getRoleIdByUserId(sysUser.getId());

        List<UserRoleBo> userRoleBos = remoteUserRoleService.getByUserId(sysUser.getId());

        if (userRoleBos == null || userRoleBos.isEmpty()) {
            throw new BusinessException("用户暂未授权角色，请联系管理员");
        }
        List<SysRole> roles = new ArrayList<>();
        userRoleBos.forEach(r -> {
            roles.add(r.convertedToRole());
        });


        SysUserVo user = new SysUserVo(sysUser, roles);

        LoginVo loginVo = new LoginVo();
        loginVo.setUser(user);

        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, user.getNickName());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_ID, roles.get(0).getId());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_CODE, roles.get(0).getRoleCode());

        loginVo.setToken(JwtUtils.createToken(claimsMap));

        return loginVo;

    }

    @Override
    public void jsonInBlacklist() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        // 获取请求token
        String token = request.getHeader(LoginConstant.TOKEN_NAME);
        redisTemplate.opsForSet().add(LoginConstant.JWT_BLACK_LIST, token);
    }
}
 