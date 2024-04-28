package com.zvz09.xiaochen.mc.component.provider;

import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface EcsOperation extends BaseProductService {

    default List<EcsInstance> listEcsInstances(){
        List<EcsInstance> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    List<EcsInstance> listEcsInstances(String region);

    EcsInstance describeInstance(String region, String instanceId);

    List<Region> listRegions();

    List<ZoneDTO> listZones(String region);

    default List<EcsInstanceType> listAllInstanceTypes(){
        List<EcsInstanceType> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listAllInstanceTypes(region.getRegionCode()));
        });
        return instances;
    }

    List<ImageDTO> listAllImages(String region);

    default List<ImageDTO> listAllImages(){
        List<ImageDTO> images = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            images.addAll(this.listAllImages(region.getRegionCode()));
        });
        return images;
    }

    List<EcsInstanceType> listAllInstanceTypes(String region);

    default ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };
}
