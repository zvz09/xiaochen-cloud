package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.ecs20140526.models.DescribeSecurityGroupsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeSecurityGroupsResponse;
import com.aliyun.sdk.service.vpc20160428.AsyncClient;
import com.aliyun.sdk.service.vpc20160428.models.CreateVSwitchRequest;
import com.aliyun.sdk.service.vpc20160428.models.CreateVSwitchResponse;
import com.aliyun.sdk.service.vpc20160428.models.CreateVpcRequest;
import com.aliyun.sdk.service.vpc20160428.models.CreateVpcResponse;
import com.aliyun.sdk.service.vpc20160428.models.DeleteVSwitchRequest;
import com.aliyun.sdk.service.vpc20160428.models.DeleteVpcRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVSwitchesRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVSwitchesResponse;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVSwitchesResponseBody;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeResponse;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeResponseBody;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcsRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcsResponse;
import com.aliyun.sdk.service.vpc20160428.models.DescribeZonesRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeZonesResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AliYunVpcOperationImpl extends AliYunBaseOperation implements VpcOperation {

    private final static Integer PAGE_SIZE = 50;


    private final IRegionService regionService;

    public AliYunVpcOperationImpl(AliYunClient aliYunClient,IRegionService regionService) {
        super(aliYunClient);
        this.regionService = regionService;
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) aliYunClient.handleClient((client) -> {
            CreateVpcRequest request = CreateVpcRequest.builder()
                    .regionId(vpcDTO.getRegion())
                    .cidrBlock(vpcDTO.getCidrBlock())
                    .ipv6CidrBlock(vpcDTO.getIpv6CidrBlock())
                    .enableIpv6(vpcDTO.isEnableIpv6())
                    .vpcName(vpcDTO.getVpcName())
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.createVpc(request).get();
        }, vpcDTO.getRegion(), this.getProductCode());
        return this.convertedInstance(response.getBody().getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
       aliYunClient.handleClient((client) -> {
            DeleteVpcRequest request = DeleteVpcRequest.builder()
                    .regionId(region)
                    .vpcId(vpcId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.deleteVpc(request).get();
        }, region, this.getProductCode());
    }

    @Override
    public Page<VSwitcheDTO> listVSwitches(String region, String vpcId, Integer pageNumber, Integer pageSize) {
        DescribeVSwitchesResponse response = (DescribeVSwitchesResponse) aliYunClient.handleClient((client) -> {
            DescribeVSwitchesRequest request = DescribeVSwitchesRequest.builder()
                    .vpcId(vpcId)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVSwitches(request).get();
        }, region, this.getProductCode());

        DescribeVSwitchesResponseBody body = response.getBody();

        Page<VSwitcheDTO> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setTotal(body.getTotalCount());
        page.setSize(pageSize);
        page.setRecords(body.getVSwitches().getVSwitch().stream().map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getVSwitchId())
                .vSwitchName(vSwitch.getVSwitchName())
                .zoneId(vSwitch.getZoneId())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(vSwitch.getAvailableIpAddressCount())
                .build()).toList());

        return page;
    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateVSwitchResponse response = (CreateVSwitchResponse) aliYunClient.handleClient((client) -> {
            CreateVSwitchRequest request = CreateVSwitchRequest.builder()
                    .regionId(createVSwitch.getRegionId())
                    .zoneId(createVSwitch.getZoneId())
                    .cidrBlock(createVSwitch.getCidrBlock())
                    .ipv6CidrBlock(createVSwitch.getIpv6CidrBlock())
                    .vpcId(createVSwitch.getVpcId())
                    .vSwitchName(createVSwitch.getVSwitchName())
                    .vpcIpv6CidrBlock(createVSwitch.getVpcIpv6CidrBlock())
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.createVSwitch(request).get();
        }, createVSwitch.getRegionId(), this.getProductCode());
        return this.convertedVSwitche(response.getBody().getVSwitchId(), createVSwitch);
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        aliYunClient.handleClient((client) -> {
            DeleteVSwitchRequest request = DeleteVSwitchRequest.builder()
                    .regionId(region)
                    .vSwitchId(vSwitchId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.deleteVSwitch(request).get();
        }, region, this.getProductCode());
    }

    @Override
    public List<VpcInstance> listVpcInstances(String region) {
        List<VpcInstance> instances = new ArrayList<>();

        DescribeVpcsResponse response = executeDescribeVpcs(region,1);
        if(response.getBody().getTotalCount() > 0){
            addInstance(region, instances, response);
            // 获取其他页
            Integer total = response.getBody().getTotalCount();
            for (int i = 2; i <= total / PAGE_SIZE + 1; i++)  {
                response = executeDescribeVpcs(region,i);
                addInstance(region, instances, response);
            }
        }

        return instances;
    }

    @Override
    public List<ZoneDTO> listZones(String region) {
        List<ZoneDTO> zoneDTOS = new ArrayList<>();
        DescribeZonesResponse response = (DescribeZonesResponse) aliYunClient.handleClient((client) -> {
            DescribeZonesRequest request = DescribeZonesRequest.builder()
                    .regionId(region)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeZones(request).get();
        }, region, this.getProductCode());

        if (response.getBody().getZones() != null && response.getBody().getZones().getZone() != null) {
            response.getBody().getZones().getZone().forEach(zone -> {
                zoneDTOS.add(new ZoneDTO(zone.getZoneId(), zone.getLocalName()));
            });
        }
        return zoneDTOS;
    }

    @Override
    public VpcInstance describeInstance(String region, String instanceId) {
        DescribeVpcAttributeResponse response = (DescribeVpcAttributeResponse) aliYunClient.handleClient((client) -> {
            DescribeVpcAttributeRequest request = DescribeVpcAttributeRequest.builder()
                    .regionId(region)
                    .vpcId(instanceId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVpcAttribute(request).get();
        }, region, this.getProductCode());

        if(response.getBody() != null && StringUtils.isNotBlank(response.getBody().getVpcId())){
            DescribeVpcAttributeResponseBody vpc = response.getBody();
            return VpcInstance.builder()
                    .provider(this.getProviderCode().getValue())
                    .instanceId(vpc.getVpcId())
                    .instanceName(vpc.getVpcName())
                    .status(vpc.getStatus())
                    .region(region)
                    .cidrBlock(vpc.getCidrBlock())
                    .ipv6CidrBlock(vpc.getIpv6CidrBlock())
                    .detail(JSON.toJSONString(vpc))
                    .build();
        }else {
            return null;
        }
    }

    @Override
    public Page<SecurityGroupDTO> listSecurityGroups(String region, Integer pageNumber, Integer pageSize) {

        DescribeSecurityGroupsResponse response = (DescribeSecurityGroupsResponse) aliYunClient.handleClient((client) -> {
            DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                    .regionId(region)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .build();
            com.aliyun.sdk.service.ecs20140526.AsyncClient asyncClient = (com.aliyun.sdk.service.ecs20140526.AsyncClient) client;
            return asyncClient.describeSecurityGroups(request).get();
        }, region, ProductEnum.ECS);



        Page<SecurityGroupDTO> page = new Page<>();
        page.setTotal(response.getBody().getTotalCount());
        page.setCurrent(pageNumber);
        page.setSize(pageSize);
        List<SecurityGroupDTO> records = new ArrayList<>();

        response.getBody().getSecurityGroups().getSecurityGroup().forEach(securityGroup ->{
            records.add(SecurityGroupDTO.builder()
                    .securityGroupId(securityGroup.getSecurityGroupId())
                    .securityGroupName(securityGroup.getSecurityGroupName())
                    .description(securityGroup.getDescription())
                    .build());
        });
        page.setRecords(records);
        return page;
    }

    @Override
    public List<Region> listRegions() {
       return regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue()));
    }

    private DescribeVpcsResponse executeDescribeVpcs(String region,Integer pageNumber) {
        return (DescribeVpcsResponse) aliYunClient.handleClient((client) -> {
            DescribeVpcsRequest request = DescribeVpcsRequest.builder()
                    .regionId(region)
                    .pageNumber(pageNumber)
                    .pageSize(PAGE_SIZE)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVpcs(request).get();
        }, region, this.getProductCode());
    }

    private void addInstance(String region, List<VpcInstance> instances, DescribeVpcsResponse response) {
        instances.addAll(response.getBody().getVpcs().getVpc().stream().map(vpc -> {
            return VpcInstance.builder()
                    .provider(this.getProviderCode().getValue())
                    .instanceId(vpc.getVpcId())
                    .instanceName(vpc.getVpcName())
                    .status(vpc.getStatus())
                    .region(region)
                    .cidrBlock(vpc.getCidrBlock())
                    .ipv6CidrBlock(vpc.getIpv6CidrBlock())
                    .detail(JSON.toJSONString(vpc))
                    .build();
        }).toList());
    }
}
