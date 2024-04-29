package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.volcengine.model.AbstractResponse;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.CreateSubnetRequest;
import com.volcengine.vpc.model.CreateSubnetResponse;
import com.volcengine.vpc.model.CreateVpcRequest;
import com.volcengine.vpc.model.CreateVpcResponse;
import com.volcengine.vpc.model.DeleteSubnetRequest;
import com.volcengine.vpc.model.DeleteVpcRequest;
import com.volcengine.vpc.model.DescribeSecurityGroupsRequest;
import com.volcengine.vpc.model.DescribeSecurityGroupsResponse;
import com.volcengine.vpc.model.DescribeSubnetsRequest;
import com.volcengine.vpc.model.DescribeSubnetsResponse;
import com.volcengine.vpc.model.DescribeVpcAttributesRequest;
import com.volcengine.vpc.model.DescribeVpcAttributesResponse;
import com.volcengine.vpc.model.DescribeVpcsRequest;
import com.volcengine.vpc.model.DescribeVpcsResponse;
import com.zvz09.xiaochen.mc.component.provider.AbstractVpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VolcengineVpcOperation extends AbstractVpcOperation<VolcengineClient, AbstractResponse> implements VolcengineBaseOperation {

    private final IRegionService regionService;
    private final VolcengineEcsOperation ecsOperation;

    public VolcengineVpcOperation(VolcengineClient client, IRegionService regionService,
                                  VolcengineEcsOperation ecsOperation) {
        super(client, 100);
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    protected AbstractResponse executeGetRegions(QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractResponse response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(AbstractResponse response, List<Region> regions) {
        regions.addAll(regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue())));
    }

    @Override
    protected AbstractResponse executeGetZones(String region, QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractResponse response, List<ZoneDTO> zones) {
        zones.addAll(ecsOperation.listZones(region));
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            CreateVpcRequest request = new CreateVpcRequest();
            request.setCidrBlock(vpcDTO.getCidrBlock());
            request.setEnableIpv6(vpcDTO.isEnableIpv6());
            request.setVpcName(vpcDTO.getVpcName());
            request.setIpv6CidrBlock(vpcDTO.getIpv6CidrBlock());

            return api.createVpc(request);
        }, vpcDTO.getRegion());
        return this.buildVpcInstance(response.getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteVpcRequest request = new DeleteVpcRequest();
            request.setVpcId(vpcId);

            return api.deleteVpc(request);
        }, region);
    }

    @Override
    protected AbstractResponse executeDescribeVpcInstances(String region, QueryParameter queryParameter) {
        return (DescribeVpcsResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcsRequest request = new DescribeVpcsRequest();
            request.setMaxResults(100);

            if(StringUtils.isNotBlank(queryParameter.getNextToken())){
                request.setNextToken(queryParameter.getNextToken());
            }
            return api.describeVpcs(request);
        },region);
    }

    @Override
    protected QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        return super.haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addVpcInstances(String region, AbstractResponse response, List<VpcInstance> instances) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        resp.getVpcs().stream().parallel().forEach(instance ->{
            instances.add(this.buildVpcInstance(region, this.executeDescribeVpcInstance(region,instance.getVpcId())));
        });
    }

    @Override
    protected VpcInstance buildVpcInstance(String region, AbstractResponse response) {
        DescribeVpcAttributesResponse resp = (DescribeVpcAttributesResponse) response;
        return VpcInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(resp.getVpcId())
                .instanceName(resp.getVpcName())
                .status(resp.getStatus())
                .region(region)
                .cidrBlock(resp.getCidrBlock())
                .detail(JSON.toJSONString(resp))
                .build();
    }

    @Override
    protected AbstractResponse executeDescribeVpcInstance(String region, String instanceId) {
        return (DescribeVpcAttributesResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcAttributesRequest request = new DescribeVpcAttributesRequest();
            request.setVpcId(instanceId);

            return api.describeVpcAttributes(request);
        },region);
    }

    @Override
    protected AbstractResponse executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter) {
        return (DescribeSubnetsResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DescribeSubnetsRequest request = new DescribeSubnetsRequest();
            request.setPageNumber(queryParameter.getPageNumber());
            request.setPageSize(queryParameter.getPageSize());
            request.setVpcId(vpcId);
            return api.describeSubnets(request);
        }, region);
    }

    @Override
    protected void paddingVSwitchePage(String vpcId, AbstractResponse response, Page<VSwitcheDTO> page) {
        DescribeSubnetsResponse resp = (DescribeSubnetsResponse) response;
        page.setTotal(resp.getTotalCount());
        page.setRecords(resp.getSubnets().stream().map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getSubnetId())
                .vSwitchName(vSwitch.getSubnetName())
                .zoneId(vSwitch.getZoneId())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(Long.valueOf(vSwitch.getAvailableIpAddressCount()))
                .build()).toList());
    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            CreateSubnetRequest request = new CreateSubnetRequest();
            request.setCidrBlock(createVSwitch.getCidrBlock());
            request.setIpv6CidrBlock(createVSwitch.getIpv6CidrBlock());
            request.setSubnetName(createVSwitch.getVSwitchName());
            request.setVpcId(createVSwitch.getVpcId());
            request.setZoneId(createVSwitch.getZoneId());

            return api.createSubnet(request);
        }, createVSwitch.getRegionId());
        return this.convertedVSwitche(response.getSubnetId(), createVSwitch);
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteSubnetRequest request = new DeleteSubnetRequest();
            request.setSubnetId(vSwitchId);

            return api.deleteSubnet(request);
        }, region);
    }


    @Override
    protected AbstractResponse executeGetSecurityGroups(String region, QueryParameter queryParameter) {
        return  (DescribeSecurityGroupsResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);

            DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
            request.setPageNumber(queryParameter.getPageNumber());
            request.setPageSize(queryParameter.getPageSize());

            return api.describeSecurityGroups(request);
        },region);
    }

    @Override
    protected void paddingSecurityGroupPage(AbstractResponse response, Page<SecurityGroupDTO> page) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) response;

        page.setTotal(resp.getTotalCount());
        List<SecurityGroupDTO> records = new ArrayList<>();

        resp.getSecurityGroups().forEach(securityGroup ->{
            records.add(SecurityGroupDTO.builder()
                    .securityGroupId(securityGroup.getSecurityGroupId())
                    .securityGroupName(securityGroup.getSecurityGroupName())
                    .vpcId(securityGroup.getVpcId())
                    .description(securityGroup.getDescription())
                    .status(securityGroup.getStatus())
                    .type(securityGroup.getType())
                    .build());
        });
        page.setRecords(records);
    }
}
