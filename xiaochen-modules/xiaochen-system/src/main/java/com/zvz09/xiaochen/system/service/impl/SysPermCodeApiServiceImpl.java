package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCodeApi;
import com.zvz09.xiaochen.system.mapper.SysPermCodeApiMapper;
import com.zvz09.xiaochen.system.service.ISysPermCodeApiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限字和api接口关联表 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Service
public class SysPermCodeApiServiceImpl extends ServiceImpl<SysPermCodeApiMapper, SysPermCodeApi> implements ISysPermCodeApiService {

    @Override
    @Transactional
    public void deleteByPermCodeId(Long id) {
        this.remove(new LambdaQueryWrapper<SysPermCodeApi>().eq(SysPermCodeApi::getPermCodeId, id));
    }

    @Override
    public List<Long> getApiIdByPermCodeId(Long permCodeId) {
        return this.list(new LambdaQueryWrapper<SysPermCodeApi>().eq(SysPermCodeApi::getPermCodeId,permCodeId))
                .stream().map(SysPermCodeApi::getApiId).collect(Collectors.toList());
    }
}
