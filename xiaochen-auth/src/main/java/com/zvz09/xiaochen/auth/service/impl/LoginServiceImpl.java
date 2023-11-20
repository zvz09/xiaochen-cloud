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
import com.zvz09.xiaochen.common.jwt.JwtUtils;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteRoleMenuService;
import com.zvz09.xiaochen.system.api.RemoteRoleService;
import com.zvz09.xiaochen.system.api.RemoteUserRoleService;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.entity.SysRole;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    private final RedissonClient redissonClient;

    private final RemoteUserService remoteUserService;

    private final RemoteRoleService remoteRoleService;

    private final RemoteUserRoleService remoteUserRoleService;

    private final RemoteRoleMenuService remoteRoleMenuService;

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

        SysRole sysRole = remoteRoleService.getById(sysUser.getRoleId());

        List<SysRole> sysAuthorities = null;

        if (roleCodes != null && !roleCodes.isEmpty()) {
            sysAuthorities = remoteRoleService.getByRoleCodes(roleCodes);
        }

        List<Long> sysBaseMenuIds = remoteRoleMenuService.getMenuIdByRoleId(sysUser.getRoleId());


        SysUserVo user = new SysUserVo(sysUser, sysRole, sysAuthorities);

        LoginVo loginVo = new LoginVo();
        loginVo.setUser(user);

        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, user.getNickName());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_ID, sysRole.getId());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_CODE, sysRole.getRoleCode());

        loginVo.setToken(JwtUtils.createToken(claimsMap));

        return loginVo;

    }

    @Override
    public void jsonInBlacklist() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        // 获取请求token
        String token = request.getHeader(LoginConstant.TOKEN_NAME);
        RSet<String> blacklist = redissonClient.getSet(LoginConstant.JWT_BLACK_LIST);
        blacklist.add(token);
    }
}
 