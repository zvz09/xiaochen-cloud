package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcResponse;
import com.tencentcloudapi.vpc.v20170312.models.DeleteSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.DeleteVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsResponse;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TencentCloudVpcOperationImpl extends TencentCloudBaseOperation implements VpcOperation {

    private final IRegionService regionService;
    private final TencentCloudEcsOperationImpl ecsOperation;

    public TencentCloudVpcOperationImpl(TencentCloudClient tencentCloudClient, IRegionService regionService, TencentCloudEcsOperationImpl ecsOperation) {
        super(tencentCloudClient);
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) tencentCloudClient.handleClient((abstractClient) -> {
            CreateVpcRequest req = new CreateVpcRequest();
            req.setVpcName(vpcDTO.getVpcName());
            req.setCidrBlock(vpcDTO.getCidrBlock());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateVpc(req);
        }, vpcDTO.getRegion(), ProductEnum.VPC);
        return this.convertedInstance(response.getVpc().getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        tencentCloudClient.handleClient((abstractClient) -> {
            DeleteVpcRequest req = new DeleteVpcRequest();
            req.setVpcId(vpcId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteVpc(req);
        }, region, ProductEnum.VPC);
    }

    @Override
    public Page<VSwitcheDTO> listVSwitches(String region, String vpcId, Integer pageNumber, Integer pageSize) {
        DescribeSubnetsResponse response = (DescribeSubnetsResponse) tencentCloudClient.handleClient((abstractClient) -> {
            DescribeSubnetsRequest req = new DescribeSubnetsRequest();
            req.setOffset(String.valueOf(pageNumber <= 1 ? (pageNumber - 1) * pageSize : 0));
            req.setLimit(String.valueOf(pageSize));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSubnets(req);
        }, region, ProductEnum.VPC);
        Page<VSwitcheDTO> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setTotal(response.getTotalCount());
        page.setSize(pageSize);
        page.setRecords(Arrays.stream(response.getSubnetSet()).map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getSubnetId())
                .vSwitchName(vSwitch.getSubnetName())
                .zoneId(vSwitch.getZone())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(vSwitch.getAvailableIpAddressCount())
                .build()).toList());
        return page;
    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) tencentCloudClient.handleClient((abstractClient) -> {
            CreateSubnetRequest req = new CreateSubnetRequest();
            req.setVpcId(createVSwitch.getVpcId());
            req.setSubnetName(createVSwitch.getVSwitchName());
            req.setCidrBlock(createVSwitch.getCidrBlock());
            req.setZone(createVSwitch.getZoneId());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateSubnet(req);
        }, createVSwitch.getRegionId(), ProductEnum.VPC);
        return this.convertedVSwitche(response.getSubnet().getSubnetId(), createVSwitch);
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        tencentCloudClient.handleClient((abstractClient) -> {
            DeleteSubnetRequest req = new DeleteSubnetRequest();
            req.setSubnetId(vSwitchId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteSubnet(req);
        }, region, ProductEnum.VPC);
    }

    @Override
    public List<VpcInstance> listVpcInstances(String region) {
        List<VpcInstance> instances = new ArrayList<>();

        // 查询实例列表
        long offset = 0;
        while (true){
            DescribeVpcsResponse response = executeDescribeVpcsResponse(region,offset);

            for (Vpc vpc : response.getVpcSet()){
                instances.add(convertedInstance(region, vpc));
            }

            offset += 100L;
            if (instances.size() == response.getTotalCount()) {
                break;
            }
        }
        return instances;
    }

    @Override
    public List<ZoneDTO> listZones(String region) {
        return ecsOperation.listZones(region);
    }

    @Override
    public VpcInstance describeInstance(String region, String instanceId) {
        DescribeVpcsResponse response =  (DescribeVpcsResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setVpcIds(new String[]{instanceId});
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        },region, ProductEnum.VPC);
        if(response.getVpcSet() !=null && response.getVpcSet().length > 0){
            Vpc vpc = response.getVpcSet()[0];
            return convertedInstance(region, vpc);
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {
        return regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue()));
    }

    private VpcInstance convertedInstance(String region, Vpc vpc) {
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

    private DescribeVpcsResponse executeDescribeVpcsResponse(String region, long offset) {
        return (DescribeVpcsResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setOffset(String.valueOf(offset));
            req.setLimit("100");
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        },region, ProductEnum.VPC);
    }
}
