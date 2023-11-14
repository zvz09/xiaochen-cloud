package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.authority.CopySysAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.dto.authority.SysAuthorityDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysAuthority;
import com.zvz09.xiaochen.system.api.domain.vo.SysAuthorityVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysAuthorityService extends IService<SysAuthority> {

    List<SysAuthority> getByAuthorityCodes(List<String> authorityCodes);

    SysAuthority getByAuthorityCode(String authorityCode);

    List<SysAuthorityVo> getAuthorityTree();

    void createAuthority(SysAuthorityDto sysAuthorityDto);

    void copyAuthority(CopySysAuthorityDto copySysAuthorityDto);

    void deleteAuthority(Long id);

    void updateAuthority(SysAuthorityDto sysAuthorityDto);

    List<SysAuthority> getByIds(List<Long> authorityIds);
}
