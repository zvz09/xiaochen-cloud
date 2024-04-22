package com.zvz09.xiaochen.mc.component;

import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.List;

public interface EcsOperation extends BaseProductService {

    List<EcsInstance> listEcsInstances(String region);

    List<Region> listRegions();

    default ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };
}
