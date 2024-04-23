package com.zvz09.xiaochen.mc.component.service;

import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;

import java.util.List;

public interface IEcsService {

    List<Region> listAllRegions();

    List<EcsInstance> listAllEcsInstances();

    List<Region> listAllRegions(String provider);

    List<EcsInstance> listAllEcsInstances(String provider);

    List<EcsInstance> listAllEcsInstances(String provider,String region);

    EcsInstance describeInstance(String provider,String region, String instanceId);
}
