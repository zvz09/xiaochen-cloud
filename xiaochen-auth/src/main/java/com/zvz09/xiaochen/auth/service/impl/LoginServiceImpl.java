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
import com.zvz09.xiaochen.system.api.RemoteAuthorityMenuService;
import com.zvz09.xiaochen.system.api.RemoteAuthorityService;
import com.zvz09.xiaochen.system.api.RemoteBaseMenuService;
import com.zvz09.xiaochen.system.api.RemoteUserAuthorityService;
import com.zvz09.xiaochen.system.api.RemoteUserService;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
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

    private final RemoteAuthorityService remoteAuthorityService;

    private final RemoteUserAuthorityService remoteUserAuthorityService;

    private final RemoteAuthorityMenuService remoteAuthorityMenuService;

    private final RemoteBaseMenuService remoteBaseMenuService;

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

        List<String> authorityCodes = remoteUserAuthorityService.getAuthorityIdByUserId(sysUser.getId());

        SysAuthority sysAuthority = remoteAuthorityService.getById(sysUser.getAuthorityId());

        List<SysAuthority> sysAuthorities = null;

        if (authorityCodes != null && !authorityCodes.isEmpty()) {
            sysAuthorities = remoteAuthorityService.getByAuthorityCodes(authorityCodes);
        }

        List<Long> sysBaseMenuIds = remoteAuthorityMenuService.getMenuIdByAuthorityId(sysUser.getAuthorityId());

        List<SysBaseMenu> sysBaseMenus = remoteBaseMenuService.getDefaultMenu(sysAuthority.getDefaultRouter(), sysBaseMenuIds);

        //用户角色默认路由不在菜单内，设置默认路由为 404 页面
        if (sysBaseMenus == null || sysBaseMenus.isEmpty()) {
            sysAuthority.setDefaultRouter("404");
        }

        SysUserVo user = new SysUserVo(sysUser, sysAuthority, sysAuthorities);

        LoginVo loginVo = new LoginVo();
        loginVo.setUser(user);

        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, user.getId());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, user.getNickName());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_ID, sysAuthority.getId());
        claimsMap.put(SecurityConstants.DETAILS_AUTHORITY_CODE, sysAuthority.getAuthorityCode());

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
 