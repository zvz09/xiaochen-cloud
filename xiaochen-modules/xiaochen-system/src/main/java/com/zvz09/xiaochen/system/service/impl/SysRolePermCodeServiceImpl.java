package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.domain.entity.SysRolePermCode;
import com.zvz09.xiaochen.system.mapper.SysRolePermCodeMapper;
import com.zvz09.xiaochen.system.service.ISysRolePermCodeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色与系统权限字对应关系表 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Service
public class SysRolePermCodeServiceImpl extends ServiceImpl<SysRolePermCodeMapper, SysRolePermCode> implements ISysRolePermCodeService {

    @Override
    public List<Long> getMenuIdByRoleId(Long roleId) {
        return this.baseMapper.selectMenuIdByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        this.remove(new LambdaQueryWrapper<SysRolePermCode>().eq(SysRolePermCode::getRoleId, roleId));
    }

    @Override
    public List<Long> getPermCodeIdByRoleId(Long roleId) {
        return this.list(new LambdaQueryWrapper<SysRolePermCode>()
                .eq(SysRolePermCode::getRoleId, roleId)).stream().map(SysRolePermCode::getPermCodeId).collect(Collectors.toList());

    }
}
