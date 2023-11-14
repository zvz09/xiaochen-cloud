package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuParameter;
import com.zvz09.xiaochen.system.mapper.SysBaseMenuParameterMapper;
import com.zvz09.xiaochen.system.service.ISysBaseMenuParameterService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Service
public class SysBaseMenuParameterServiceImpl extends ServiceImpl<SysBaseMenuParameterMapper, SysBaseMenuParameter> implements ISysBaseMenuParameterService {

    @Override
    public void deleteByMenuId(Long sysBaseMenuId) {
        this.deleteByMenuId(Collections.singletonList(sysBaseMenuId));
    }

    @Override
    public void deleteByMenuId(List<Long> sysBaseMenuIds) {
        this.remove(new LambdaQueryWrapper<SysBaseMenuParameter>().in(SysBaseMenuParameter::getSysBaseMenuId, sysBaseMenuIds));
    }
}
