package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.DescribeVpcAttributesRequest;
import com.volcengine.vpc.model.DescribeVpcAttributesResponse;
import com.volcengine.vpc.model.DescribeVpcsRequest;
import com.volcengine.vpc.model.DescribeVpcsResponse;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
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

    public VolcengineVpcOperationImpl(VolcengineClient volcengineClient, IRegionService regionService) {
        super(volcengineClient);
        this.regionService = regionService;
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
