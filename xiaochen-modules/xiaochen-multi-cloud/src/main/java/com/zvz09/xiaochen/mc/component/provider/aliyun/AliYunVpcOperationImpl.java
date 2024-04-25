package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.vpc20160428.AsyncClient;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeResponse;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcAttributeResponseBody;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcsRequest;
import com.aliyun.sdk.service.vpc20160428.models.DescribeVpcsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
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
