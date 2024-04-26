package com.zvz09.xiaochen.mc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.mapper.EcsInstanceTypeMapper;
import com.zvz09.xiaochen.mc.service.IEcsInstanceTypeService;
import org.springframework.stereotype.Service;

@Service
public class IEcsInstanceTypeServiceImpl extends ServiceImpl<EcsInstanceTypeMapper, EcsInstanceType>
        implements IEcsInstanceTypeService {
    @Override
    public void deleteAll() {
        this.getBaseMapper().deleteAll();
    }
}
