package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.CreateSubnetRequest;
import com.volcengine.vpc.model.CreateSubnetResponse;
import com.volcengine.vpc.model.CreateVpcRequest;
import com.volcengine.vpc.model.CreateVpcResponse;
import com.volcengine.vpc.model.DeleteSubnetRequest;
import com.volcengine.vpc.model.DeleteVpcRequest;
import com.volcengine.vpc.model.DescribeSubnetsRequest;
import com.volcengine.vpc.model.DescribeSubnetsResponse;
import com.volcengine.vpc.model.DescribeVpcAttributesRequest;
import com.volcengine.vpc.model.DescribeVpcAttributesResponse;
import com.volcengine.vpc.model.DescribeVpcsRequest;
import com.volcengine.vpc.model.DescribeVpcsResponse;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VolcengineVpcOperationImpl extends VolcengineBaseOperation implements VpcOperation {

    private final IRegionService regionService;
    private final VolcengineEcsOperationImpl ecsOperation;

    public VolcengineVpcOperationImpl(VolcengineClient volcengineClient, IRegionService regionService, VolcengineEcsOperationImpl ecsOperation) {
        super(volcengineClient);
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) volcengineClient.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            CreateVpcRequest request = new CreateVpcRequest();
            request.setCidrBlock(vpcDTO.getCidrBlock());
            request.setEnableIpv6(vpcDTO.isEnableIpv6());
            request.setVpcName(vpcDTO.getVpcName());
            request.setIpv6CidrBlock(vpcDTO.getIpv6CidrBlock());

            return api.createVpc(request);
        }, vpcDTO.getRegion());
        return this.convertedInstance(response.getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        volcengineClient.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteVpcRequest request = new DeleteVpcRequest();
            request.setVpcId(vpcId);

            return api.deleteVpc(request);
        }, region);
    }

    @Override
    public Page<VSwitcheDTO> listVSwitches(String region, String vpcId, Integer pageNumber, Integer pageSize) {
        DescribeSubnetsResponse response = (DescribeSubnetsResponse) volcengineClient.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DescribeSubnetsRequest request = new DescribeSubnetsRequest();
            request.setPageNumber(pageNumber);
            request.setPageSize(pageSize);
            request.setVpcId(vpcId);
            return api.describeSubnets(request);
        }, region);

        Page<VSwitcheDTO> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setTotal(response.getTotalCount());
        page.setSize(pageSize);
        page.setRecords(response.getSubnets().stream().map(vSwitch -> VSwitcheDTO.builder()
                .vSwitchId(vSwitch.getSubnetId())
                .vSwitchName(vSwitch.getSubnetName())
                .zoneId(vSwitch.getZoneId())
                .cidrBlock(vSwitch.getCidrBlock())
                .ipv6CidrBlock(vSwitch.getIpv6CidrBlock())
                .availableIpAddressCount(Long.valueOf(vSwitch.getAvailableIpAddressCount()))
                .build()).toList());
        return page;
    }

    @Override
    public VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) volcengineClient.handleClient((client) -> {
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
        volcengineClient.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteSubnetRequest request = new DeleteSubnetRequest();
            request.setSubnetId(vSwitchId);

            return api.deleteSubnet(request);
        }, region);
    }

    @Override
    public List<VpcInstance> listVpcInstances(String region) {
        List<VpcInstance> instances = new ArrayList<>();

        DescribeVpcsResponse response = executeDescribeInstances(region,null);
        addInstance(region, instances, response);
        while (StringUtils.isNotBlank(response.getNextToken())){
            response = executeDescribeInstances(region,null);
            addInstance(region, instances, response);
        }

        return instances;
    }

    @Override
    public List<ZoneDTO> listZones(String region) {
        return ecsOperation.listZones(region);
    }

    @Override
    public VpcInstance describeInstance(String region, String instanceId) {
        DescribeVpcAttributesResponse response =  executeDescribeVpcAttributesResponse(region,instanceId);
        if(response != null && StringUtils.isNotBlank(response.getVpcId())){
            return convertedInstance(region,response);
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {
        return regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue()));
    }

    private DescribeVpcsResponse executeDescribeInstances(String region,String nextToken){
        return (DescribeVpcsResponse) volcengineClient.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcsRequest request = new DescribeVpcsRequest();
            request.setMaxResults(100);

            if(StringUtils.isNotBlank(nextToken)){
                request.setNextToken(nextToken);
            }
            return api.describeVpcs(request);
        },region);
    }

    private DescribeVpcAttributesResponse executeDescribeVpcAttributesResponse(String region,String instanceId){
        return (DescribeVpcAttributesResponse) volcengineClient.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcAttributesRequest request = new DescribeVpcAttributesRequest();
            request.setVpcId(instanceId);

            return api.describeVpcAttributes(request);
        },region);
    }

    private void addInstance(String region, List<VpcInstance> instances, DescribeVpcsResponse response) {
        response.getVpcs().stream().parallel().forEach(instance ->{
            instances.add(convertedInstance(region, executeDescribeVpcAttributesResponse(region,instance.getVpcId())));
        });
    }
    private VpcInstance convertedInstance(String region, DescribeVpcAttributesResponse vpc) {
        return VpcInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(vpc.getVpcId())
                .instanceName(vpc.getVpcName())
                .status(vpc.getStatus())
                .region(region)
                .cidrBlock(vpc.getCidrBlock())
                .detail(JSON.toJSONString(vpc))
                .build();
    }
}
