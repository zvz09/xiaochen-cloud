package com.zvz09.xiaochen.mc.service.impl;

import com.zvz09.xiaochen.mc.component.EcsOperation;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.service.IEcsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IEcsServiceImpl implements IEcsService , InitializingBean {

    private final List<EcsOperation> ecsOperations;

    private Map<String, EcsOperation> ecsOperationMap;

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
        List<EcsInstance> instances = new ArrayList<>();
        List<Region> regions = this.listAllRegions();
        regions.forEach(region -> {
            instances.addAll(ecsOperationMap.get(region.getProviderCode()).listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ecsOperationMap = new HashMap<>();
        ecsOperations.forEach(ecsOperation -> {
            ecsOperationMap.put(ecsOperation.getProviderCode().getValue(),ecsOperation);
        });
    }
}
