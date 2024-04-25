package com.zvz09.xiaochen.mc.component.provider;

import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.ArrayList;
import java.util.List;

public interface VpcOperation extends BaseProductService {

    default List<VpcInstance> listVpcInstances(){
        List<VpcInstance> instances = new ArrayList<>();
        this.listRegions().forEach(region -> {
            instances.addAll(this.listVpcInstances(region.getRegionCode()));
        });
        return instances;
    }

    List<VpcInstance> listVpcInstances(String region);

    VpcInstance describeInstance(String region, String instanceId);

    List<Region> listRegions();

    default ProductEnum getProductCode(){
        return ProductEnum.VPC;
    };
}
