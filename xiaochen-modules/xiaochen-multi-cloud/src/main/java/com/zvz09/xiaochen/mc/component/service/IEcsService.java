package com.zvz09.xiaochen.mc.component.service;

import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

import java.util.List;

public interface IEcsService {

    List<Region> listAllRegions();

    List<EcsInstance> listAllEcsInstances();

    List<Region> listAllRegions(CloudProviderEnum provider);

    List<ZoneDTO> listZones(CloudProviderEnum provider, String region);

    List<EcsInstance> listAllEcsInstances(CloudProviderEnum provider);

    List<EcsInstance> listAllEcsInstances(CloudProviderEnum provider, String region);

    EcsInstance describeInstance(CloudProviderEnum provider, String region, String instanceId);

    List<EcsInstanceType> listAllInstanceTypes(CloudProviderEnum provider);

    List<EcsInstanceType> listAllInstanceTypes(CloudProviderEnum provider,String region);
}
