package com.zvz09.xiaochen.mc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;

public interface EcsInstanceTypeMapper extends BaseMapper<EcsInstanceType> {

    void deleteAll();
}
