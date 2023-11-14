package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.entity.SysUserAuthority;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysUserAuthorityService extends IService<SysUserAuthority> {

    List<String> getAuthorityIdByUserId(Long userId);

    List<SysUserAuthority> getByUserIds(List<Long> userIds);
}
