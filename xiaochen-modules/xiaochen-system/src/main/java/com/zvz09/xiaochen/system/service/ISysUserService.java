package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.user.RegisterUserDto;
import com.zvz09.xiaochen.system.api.domain.dto.user.SysUserQuery;
import com.zvz09.xiaochen.system.api.domain.dto.user.UpdateUserDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysUser;
import com.zvz09.xiaochen.system.api.domain.vo.SysUserVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    IPage<SysUserVo> getUserList(SysUserQuery sysUserQuery);

    SysUserVo getUserInfo();

    void register(RegisterUserDto registerUserDto);

    void resetPassword(Long userId);

    void updateUserInfo(UpdateUserDto updateUserDto);

    void deleteUser(Long id);

    IPage<SysUserVo> getAllUser(SysUserQuery sysUserQuery);
}
