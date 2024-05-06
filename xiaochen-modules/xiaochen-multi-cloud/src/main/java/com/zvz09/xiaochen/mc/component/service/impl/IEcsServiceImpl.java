package com.zvz09.xiaochen.mc.component.service.impl;

import com.zvz09.xiaochen.mc.component.product.ecs.AbstractEcsOperation;
import com.zvz09.xiaochen.mc.component.service.IEcsService;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IEcsServiceImpl implements IEcsService, InitializingBean {

    private final List<AbstractEcsOperation> ecsOperations;

    private Map<CloudProviderEnum, AbstractEcsOperation> ecsOperationProviderMap;

    @Override
    public List<Region> listAllRegions() {
        List<Region> list = new ArrayList<>();

        ecsOperations.forEach(ecsOperation -> {
            list.addAll(ecsOperation.listRegions());
        });

        return list;
    }

    @Override
    public List<EcsInstance> listAllEcsInstances() {
        List<Region> regions = this.listAllRegions();
        List<EcsInstance> instances = new ArrayList<>();
        regions.stream().parallel().forEach(region -> {
            instances.addAll(ecsOperationProviderMap.get(CloudProviderEnum.getByValue(region.getProviderCode())).listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    @Override
    public List<Region> listAllRegions(CloudProviderEnum provider) {
        return ecsOperationProviderMap.get(provider).listRegions();
    }

    @Override
    public List<ZoneDTO> listZones(CloudProviderEnum provider, String region) {
        return ecsOperationProviderMap.get(provider).listZones(region);
    }

    @Override
    public List<EcsInstance> listAllEcsInstances(CloudProviderEnum provider) {
        return ecsOperationProviderMap.get(provider).listEcsInstances();
    }

    @Override
    public List<EcsInstance> listAllEcsInstances(CloudProviderEnum provider, String region) {
        return ecsOperationProviderMap.get(provider).listEcsInstances(region);
    }

    @Override
    public EcsInstance describeInstance(CloudProviderEnum provider, String region, String instanceId) {
        return ecsOperationProviderMap.get(provider).describeInstance(region,instanceId);
    }

    @Override
    public List<EcsInstanceType> listAllInstanceTypes(CloudProviderEnum provider) {
        return ecsOperationProviderMap.get(provider).listAllInstanceTypes();
    }

    @Override
    public List<EcsInstanceType> listAllInstanceTypes(CloudProviderEnum provider, String region) {
        return ecsOperationProviderMap.get(provider).listAllInstanceTypes(region);
    }

    @Override
    public List<ImageDTO> listAllImages(CloudProviderEnum provider) {
        return ecsOperationProviderMap.get(provider).listAllImages();
    }

    @Override
    public List<ImageDTO> listAllImages(CloudProviderEnum provider, String region) {
        return ecsOperationProviderMap.get(provider).listAllImages(region);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ecsOperationProviderMap = new HashMap<>();
        ecsOperations.forEach(ecsOperation -> {
            ecsOperationProviderMap.put(ecsOperation.getProviderCode(), ecsOperation);
        });
    }
}
