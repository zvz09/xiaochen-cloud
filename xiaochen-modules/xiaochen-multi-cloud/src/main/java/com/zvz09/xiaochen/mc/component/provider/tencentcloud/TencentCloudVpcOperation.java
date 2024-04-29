package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcResponse;
import com.tencentcloudapi.vpc.v20170312.models.DeleteSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.DeleteVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupsResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsResponse;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TencentCloudVpcOperation extends AbstractVpcOperation<TencentCloudClient, AbstractModel> implements TencentCloudBaseOperation {

    private final IRegionService regionService;
    private final TencentCloudEcsOperation ecsOperation;

    public TencentCloudVpcOperation(TencentCloudClient client, IRegionService regionService,
                                    TencentCloudEcsOperation ecsOperation) {
        super(client, 100);
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    protected AbstractModel executeGetRegions(QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(AbstractModel response, List<Region> regions) {
        regions.addAll(regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue())));
    }

    @Override
    protected AbstractModel executeGetZones(String region, QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractModel response, List<ZoneDTO> zones) {
        zones.addAll(ecsOperation.listZones(region));
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) client.handleClient((abstractClient) -> {
            CreateVpcRequest req = new CreateVpcRequest();
            req.setVpcName(vpcDTO.getVpcName());
            req.setCidrBlock(vpcDTO.getCidrBlock());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateVpc(req);
        }, vpcDTO.getRegion(), this.getProductCode());
        return this.buildVpcInstance(response.getVpc().getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        client.handleClient((abstractClient) -> {
            DeleteVpcRequest req = new DeleteVpcRequest();
            req.setVpcId(vpcId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteVpc(req);
        }, region, this.getProductCode());
    }

    @Override
    protected AbstractModel executeDescribeVpcInstances(String region, QueryParameter queryParameter) {
        return (DescribeVpcsResponse) client.handleClient((abstractClient)->{
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        },region, this.getProductCode());
    }

    @Override
    protected QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, AbstractModel response) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;

        return super.haveNext(queryParameter, resp.getTotalCount());
    }

    @Override
    protected void addVpcInstances(String region, AbstractModel response, List<VpcInstance> instances) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        Arrays.stream(resp.getVpcSet()).forEach(instance ->{
            instances.add(this.buildVpcInstance(region,instance));
        });
    }

    @Override
    protected VpcInstance buildVpcInstance(String region, AbstractModel response) {
        if(response == null){
            return  null;
        }
        Vpc vpc = (Vpc) response;
        return VpcInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(vpc.getVpcId())
                .instanceName(vpc.getVpcName())
                .status("Available")
                .region(region)
                .cidrBlock(vpc.getCidrBlock())
                .ipv6CidrBlock(vpc.getIpv6CidrBlock())
                .detail(JSON.toJSONString(vpc))
                .build();
    }

    @Override
    protected AbstractModel executeDescribeVpcInstance(String region, String instanceId) {
        DescribeVpcsResponse response =  (DescribeVpcsResponse) client.handleClient((abstractClient)->{
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setVpcIds(new String[]{instanceId});
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        },region, this.getProductCode());
        if(response.getVpcSet() !=null && response.getVpcSet().length > 0){
            return response.getVpcSet()[0];
        }
        return null;
    }

    @Override
    protected AbstractModel executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter) {
        return  (DescribeSubnetsResponse) client.handleClient((abstractClient) -> {
            DescribeSubnetsRequest req = new DescribeSubnetsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSubnets(req);
        }, region, this.getProductCode());
    }

    @Override
    protected void paddingVSwitchePage(String vpcId, AbstractModel response, Page<VSwitcheDTO> page) {
        DescribeSubnetsResponse resp = (DescribeSubnetsResponse) response;
        page.setTotal(resp.getTotalCount());
        page.setRecords(Arrays.stream(resp.getSubnetSet()).map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getSubnetId())
                .vSwitchName(vSwitch.getSubnetName())
                .zoneId(vSwitch.getZone())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(vSwitch.getAvailableIpAddressCount())
                .build()).toList());
    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) client.handleClient((abstractClient) -> {
            CreateSubnetRequest req = new CreateSubnetRequest();
            req.setVpcId(createVSwitch.getVpcId());
            req.setSubnetName(createVSwitch.getVSwitchName());
            req.setCidrBlock(createVSwitch.getCidrBlock());
            req.setZone(createVSwitch.getZoneId());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateSubnet(req);
        }, createVSwitch.getRegionId(), this.getProductCode());
        return super.convertedVSwitche(response.getSubnet().getSubnetId(), createVSwitch);
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        client.handleClient((abstractClient) -> {
            DeleteSubnetRequest req = new DeleteSubnetRequest();
            req.setSubnetId(vSwitchId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteSubnet(req);
        }, region, this.getProductCode());
    }

    @Override
    protected AbstractModel executeGetSecurityGroups(String region, QueryParameter queryParameter) {
        return  (DescribeSecurityGroupsResponse) client.handleClient((abstractClient)->{
            DescribeSecurityGroupsRequest req = new DescribeSecurityGroupsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSecurityGroups(req);
        },region, this.getProductCode());
    }

    @Override
    protected void paddingSecurityGroupPage(AbstractModel response, Page<SecurityGroupDTO> page) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) response;
        page.setTotal(resp.getTotalCount());
        List<SecurityGroupDTO> records = new ArrayList<>();

        Arrays.stream(resp.getSecurityGroupSet()).forEach(securityGroup ->{
            records.add(SecurityGroupDTO.builder()
                    .securityGroupId(securityGroup.getSecurityGroupId())
                    .securityGroupName(securityGroup.getSecurityGroupName())
                    .description(securityGroup.getSecurityGroupDesc())
                    .build());
        });
        page.setRecords(records);
    }
}
