package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.gateway.pop.models.Response;
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
import com.zvz09.xiaochen.mc.component.provider.AbstractVpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
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
public class AliYunVpcOperation extends AbstractVpcOperation<AliYunClient, Response> implements AliYunBaseOperation {

    private final IRegionService regionService;

    public AliYunVpcOperation(AliYunClient client, IRegionService regionService) {
        super(client, 50);
        this.regionService = regionService;
    }

    @Override
    protected Response executeGetRegions(QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, Response response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(Response response, List<Region> regions) {
        regions.addAll(regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue())));
    }

    @Override
    protected Response executeGetZones(String region, QueryParameter queryParameter) {
        return (DescribeZonesResponse) client.handleClient((client) -> {
            DescribeZonesRequest request = DescribeZonesRequest.builder()
                    .regionId(region)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeZones(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, Response response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, Response response, List<ZoneDTO> zones) {
        DescribeZonesResponse resp = (DescribeZonesResponse) response;
        if (resp.getBody().getZones() != null && resp.getBody().getZones().getZone() != null) {
            resp.getBody().getZones().getZone().forEach(zone -> {
                zones.add(new ZoneDTO(zone.getZoneId(), zone.getLocalName()));
            });
        }
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) client.handleClient((client) -> {
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
        return this.buildVpcInstance(response.getBody().getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        client.handleClient((client) -> {
            DeleteVpcRequest request = DeleteVpcRequest.builder()
                    .regionId(region)
                    .vpcId(vpcId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.deleteVpc(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected Response executeDescribeVpcInstances(String region, QueryParameter queryParameter) {
        return (DescribeVpcsResponse) client.handleClient((client) -> {
            DescribeVpcsRequest request = DescribeVpcsRequest.builder()
                    .regionId(region)
                    .pageNumber(queryParameter.getPageNumber())
                    .pageSize(queryParameter.getPageSize())
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVpcs(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, Response response) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;

        return super.haveNext(queryParameter,Long.valueOf(resp.getBody().getTotalCount()));
    }

    @Override
    protected void addVpcInstances(String region, Response response, List<VpcInstance> instances) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        if(resp.getBody().getVpcs() !=null){
            instances.addAll(resp.getBody().getVpcs().getVpc().stream().map(vpc -> {
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

    @Override
    protected VpcInstance buildVpcInstance(String region, Response response) {
        DescribeVpcAttributeResponse resp = (DescribeVpcAttributeResponse) response;
        if(resp.getBody() != null && StringUtils.isNotBlank(resp.getBody().getVpcId())){
            DescribeVpcAttributeResponseBody vpc = resp.getBody();
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
    protected Response executeDescribeVpcInstance(String region, String instanceId) {
        return  (DescribeVpcAttributeResponse) client.handleClient((client) -> {
            DescribeVpcAttributeRequest request = DescribeVpcAttributeRequest.builder()
                    .regionId(region)
                    .vpcId(instanceId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVpcAttribute(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected Response executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter) {
        return (DescribeVSwitchesResponse) client.handleClient((client) -> {
            DescribeVSwitchesRequest request = DescribeVSwitchesRequest.builder()
                    .vpcId(vpcId)
                    .pageNumber(queryParameter.getPageNumber())
                    .pageSize(queryParameter.getPageSize())
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeVSwitches(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected void paddingVSwitchePage(String vpcId, Response response, Page<VSwitcheDTO> page) {
        DescribeVSwitchesResponse resp = (DescribeVSwitchesResponse) response;
        DescribeVSwitchesResponseBody body = resp.getBody();
        page.setTotal(body.getTotalCount());
        page.setRecords(body.getVSwitches().getVSwitch().stream().map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getVSwitchId())
                .vSwitchName(vSwitch.getVSwitchName())
                .zoneId(vSwitch.getZoneId())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(vSwitch.getAvailableIpAddressCount())
                .build()).toList());

    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateVSwitchResponse response = (CreateVSwitchResponse) client.handleClient((client) -> {
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
        client.handleClient((client) -> {
            DeleteVSwitchRequest request = DeleteVSwitchRequest.builder()
                    .regionId(region)
                    .vSwitchId(vSwitchId)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.deleteVSwitch(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected Response executeGetSecurityGroups(String region, QueryParameter queryParameter) {
        return (DescribeSecurityGroupsResponse) client.handleClient((client) -> {
            DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                    .regionId(region)
                    .pageNumber(queryParameter.getPageNumber())
                    .pageSize(queryParameter.getPageSize())
                    .build();
            com.aliyun.sdk.service.ecs20140526.AsyncClient asyncClient = (com.aliyun.sdk.service.ecs20140526.AsyncClient) client;
            return asyncClient.describeSecurityGroups(request).get();
        }, region, ProductEnum.ECS);
    }

    @Override
    protected void paddingSecurityGroupPage(Response response, Page<SecurityGroupDTO> page) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) response;
        page.setTotal(resp.getBody().getTotalCount());
        List<SecurityGroupDTO> records = new ArrayList<>();

        resp.getBody().getSecurityGroups().getSecurityGroup().forEach(securityGroup ->{
            records.add(SecurityGroupDTO.builder()
                    .securityGroupId(securityGroup.getSecurityGroupId())
                    .securityGroupName(securityGroup.getSecurityGroupName())
                    .description(securityGroup.getDescription())
                    .build());
        });
        page.setRecords(records);
    }
}
