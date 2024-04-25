package com.zvz09.xiaochen.mc.component.service;

import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;

import java.util.List;

public interface IVpcService {

    List<Region> listAllRegions();

    List<VpcInstance> listAllVpcInstances();

    List<Region> listAllRegions(String provider);

    List<VpcInstance> listAllVpcInstances(String provider);

    List<VpcInstance> listAllVpcInstances(String provider,String region);

    VpcInstance describeInstance(String provider,String region, String instanceId);
}
