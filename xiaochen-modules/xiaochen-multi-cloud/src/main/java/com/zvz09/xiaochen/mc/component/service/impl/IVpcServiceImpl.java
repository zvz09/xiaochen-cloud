package com.zvz09.xiaochen.mc.component.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.component.product.vpc.AbstractVpcOperation;
import com.zvz09.xiaochen.mc.component.service.IVpcService;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.service.IVpcInstanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IVpcServiceImpl implements IVpcService, InitializingBean {

    private final List<AbstractVpcOperation> operations;

    private final IVpcInstanceService vpcInstanceService;
    private Map<CloudProviderEnum, AbstractVpcOperation> operationsProviderMap;

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
    public List<Region> listAllRegions(CloudProviderEnum provider) {
        return operationsProviderMap.get(provider).listRegions();
    }

    @Override
    public List<ZoneDTO> listZones(CloudProviderEnum provider, String region) {
        return operationsProviderMap.get(provider).listZones(region);
    }

    @Override
    @Transactional
    public VpcInstance createVpc(CloudProviderEnum provider, VpcDTO vpcDTO) {
        VpcInstance vpcInstance = operationsProviderMap.get(provider).createVpc(vpcDTO);
        vpcInstanceService.save(vpcInstance);
        return vpcInstance;
    }

    @Override
    public void deleteVpc(CloudProviderEnum provider,String region, String vpcId) {
        operationsProviderMap.get(provider).deleteVpc(region, vpcId);
    }

    @Override
    public Page<VSwitcheDTO> listVSwitches(CloudProviderEnum provider, String region, String vpcId, Integer pageNumber, Integer pageSize) {
        return operationsProviderMap.get(provider).listVSwitches(region, vpcId, pageNumber, pageSize);
    }

    @Override
    public String createVSwitch(CloudProviderEnum provider, CreateVSwitch createVSwitch) {
        return operationsProviderMap.get(provider).createVSwitch(createVSwitch);
    }

    @Override
    public void deleteVSwitch(CloudProviderEnum provider,String region, String vSwitchId) {
        operationsProviderMap.get(provider).deleteVSwitch(region, vSwitchId);
    }

    @Override
    public Page<SecurityGroupDTO> listSecurityGroups(CloudProviderEnum provider, String region, Integer pageNumber, Integer pageSize) {
        return operationsProviderMap.get(provider).listSecurityGroups(region, pageNumber, pageSize);
    }

    @Override
    public SecurityGroupDTO describeSecurityGroupAttributes(CloudProviderEnum provider, String region, String securityGroupId) {
        return operationsProviderMap.get(provider).describeSecurityGroupAttributes(region, securityGroupId);
    }

    @Override
    public void authorizeSecurityGroupEgress(CloudProviderEnum provider, String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
       operationsProviderMap.get(provider).authorizeSecurityGroupEgress(region, securityGroupId, permissions);
    }

    @Override
    public void authorizeSecurityGroupIngress(CloudProviderEnum provider, String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        operationsProviderMap.get(provider).authorizeSecurityGroupIngress(region, securityGroupId, permissions);
    }

    @Override
    public void revokeSecurityGroupEgress(CloudProviderEnum provider, String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        operationsProviderMap.get(provider).revokeSecurityGroupEgress(region, securityGroupId, permissions);
    }

    @Override
    public void revokeSecurityGroupIngress(CloudProviderEnum provider, String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        operationsProviderMap.get(provider).revokeSecurityGroupIngress(region, securityGroupId, permissions);
    }

    @Override
    public List<VpcInstance> listAllVpcInstances(CloudProviderEnum provider) {
        return operationsProviderMap.get(provider).listVpcInstances();
    }

    @Override
    public List<VpcInstance> listAllVpcInstances(CloudProviderEnum provider, String region) {
        return operationsProviderMap.get(provider).listVpcInstances(region);
    }

    @Override
    public VpcInstance describeInstance(CloudProviderEnum provider, String region, String instanceId) {
        return operationsProviderMap.get(provider).describeInstance(region, instanceId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        operationsProviderMap = new HashMap<>();
        operations.forEach(ecsOperation -> {
            operationsProviderMap.put(ecsOperation.getProviderCode(), ecsOperation);
        });
    }
}
