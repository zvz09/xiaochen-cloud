package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsResponse;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
import com.zvz09.xiaochen.mc.component.provider.VpcOperation;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TencentCloudVpcOperationImpl extends TencentCloudBaseOperation implements VpcOperation {

    private final IRegionService regionService;

    public TencentCloudVpcOperationImpl(TencentCloudClient tencentCloudClient, IRegionService regionService) {
        super(tencentCloudClient);
        this.regionService = regionService;
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
