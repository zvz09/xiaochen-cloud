package com.zvz09.xiaochen.mc.component.provider;

import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.ArrayList;
import java.util.List;

public interface EcsOperation extends BaseProductService {

    default List<EcsInstance> listEcsInstances(){
        List<EcsInstance> instances = new ArrayList<>();
        this.listRegions().forEach(region -> {
            instances.addAll(this.listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    List<EcsInstance> listEcsInstances(String region);

    EcsInstance describeInstance(String region, String instanceId);

    List<Region> listRegions();

    default ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };
}
