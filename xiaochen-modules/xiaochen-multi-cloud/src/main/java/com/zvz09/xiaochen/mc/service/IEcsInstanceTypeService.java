package com.zvz09.xiaochen.mc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;

public interface IEcsInstanceTypeService extends IService<EcsInstanceType> {

    void deleteAll();
}
