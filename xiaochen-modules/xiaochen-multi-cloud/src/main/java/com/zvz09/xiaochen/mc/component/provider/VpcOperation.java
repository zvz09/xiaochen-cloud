package com.zvz09.xiaochen.mc.component.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface VpcOperation extends BaseProductService {

    List<Region> listRegions();

    List<ZoneDTO> listZones(String region);

    VpcInstance createVpc(VpcDTO vpcDTO);

    void deleteVpc(String region, String vpcId);

    Page<VSwitcheDTO> listVSwitches(String region, String vpcId, Integer pageNumber, Integer pageSize);

    VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch);

    void deleteVSwitch(String region, String vSwitchId);

    default List<VpcInstance> listVpcInstances(){
        List<VpcInstance> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listVpcInstances(region.getRegionCode()));
        });
        return instances;
    }

    List<VpcInstance> listVpcInstances(String region);

    VpcInstance describeInstance(String region, String instanceId);

    default ProductEnum getProductCode(){
        return ProductEnum.VPC;
    }

    default VSwitcheDTO convertedVSwitche(String vSwitchId, CreateVSwitch createVSwitch) {
        return VSwitcheDTO.builder()
                .vSwitchId(vSwitchId)
                .vSwitchName(createVSwitch.getVSwitchName())
                .zoneId(createVSwitch.getZoneId())
                .cidrBlock(createVSwitch.getCidrBlock())
                .build();
    }



    default VpcInstance convertedInstance(String vpcId, VpcDTO vpcDTO) {
        return VpcInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(vpcId)
                .instanceName(vpcDTO.getVpcName())
                .region(vpcDTO.getRegion())
                .cidrBlock(vpcDTO.getCidrBlock())
                .ipv6CidrBlock(vpcDTO.getIpv6CidrBlock())
                .build();
    }

    Page<SecurityGroupDTO> listSecurityGroups(String region,Integer pageNumber, Integer pageSize);
}
