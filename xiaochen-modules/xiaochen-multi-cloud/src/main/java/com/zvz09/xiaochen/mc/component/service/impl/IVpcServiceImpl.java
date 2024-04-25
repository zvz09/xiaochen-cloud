package com.zvz09.xiaochen.mc.component.service.impl;

import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.component.service.IVpcService;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IVpcServiceImpl implements IVpcService, InitializingBean {

    private final List<VpcOperation> operations;

    private Map<String, VpcOperation> operationsProviderMap;


    @Override
    public List<Region> listAllRegions() {
        List<Region> list = new ArrayList<>();

        operations.forEach(operation -> {
            list.addAll(operation.listRegions());
        });

        return list;
    }

    @Override
    public List<VpcInstance> listAllVpcInstances() {
        List<VpcInstance> list = new ArrayList<>();

        operations.forEach(operation -> {
            list.addAll(operation.listVpcInstances());
        });

        return list;
    }

    @Override
    public List<Region> listAllRegions(String provider) {
        return operationsProviderMap.get(provider).listRegions();
    }

    @Override
    public List<VpcInstance> listAllVpcInstances(String provider) {
        return operationsProviderMap.get(provider).listVpcInstances();
    }

    @Override
    public List<VpcInstance> listAllVpcInstances(String provider, String region) {
        return operationsProviderMap.get(provider).listVpcInstances(region);
    }

    @Override
    public VpcInstance describeInstance(String provider, String region, String instanceId) {
        return operationsProviderMap.get(provider).describeInstance(region,instanceId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        operationsProviderMap = new HashMap<>();
        operations.forEach(ecsOperation -> {
            operationsProviderMap.put(ecsOperation.getProviderCode().getValue(),ecsOperation);
        });
    }
}
