package com.zvz09.xiaochen.mc.component.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

import java.util.List;

public interface IVpcService {

    List<Region> listAllRegions();

    List<Region> listAllRegions(CloudProviderEnum provider);

    List<ZoneDTO> listZones(CloudProviderEnum provider, String region);

    List<VpcInstance> listAllVpcInstances();

    List<VpcInstance> listAllVpcInstances(CloudProviderEnum provider);

    List<VpcInstance> listAllVpcInstances(CloudProviderEnum provider, String region);

    VpcInstance describeInstance(CloudProviderEnum provider, String region, String instanceId);

    VpcInstance createVpc(CloudProviderEnum provider, VpcDTO vpcDTO);

    void deleteVpc(CloudProviderEnum provider,String region, String vpcId);

    Page<VSwitcheDTO> listVSwitches(CloudProviderEnum provider, String region, String vpcId, Integer pageNumber, Integer pageSize);

    VSwitcheDTO createVSwitch(CloudProviderEnum provider, CreateVSwitch createVSwitch);

    void deleteVSwitch(CloudProviderEnum provider,String region, String vSwitchId);

    Page<SecurityGroupDTO> listSecurityGroups(CloudProviderEnum provider, String region, Integer pageNumber, Integer pageSize);
}
