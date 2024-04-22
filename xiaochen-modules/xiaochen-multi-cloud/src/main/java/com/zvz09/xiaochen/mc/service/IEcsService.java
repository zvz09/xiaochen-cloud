package com.zvz09.xiaochen.mc.service;

import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;

import java.util.List;

public interface IEcsService {

    List<Region> listAllRegions();

    List<EcsInstance> listAllEcsInstances();
}
