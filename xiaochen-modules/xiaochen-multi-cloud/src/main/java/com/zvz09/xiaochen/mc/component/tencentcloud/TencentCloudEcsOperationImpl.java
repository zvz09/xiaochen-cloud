package com.zvz09.xiaochen.mc.component.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.cvm.v20170312.models.RegionInfo;
import com.zvz09.xiaochen.mc.component.EcsOperation;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TencentCloudEcsOperationImpl implements EcsOperation {

    private final TencentCloudClient tencentCloudClient;

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();

        // 查询实例列表
        long offset = 0;
        while (true){
            DescribeInstancesResponse response = executeDescribeInstances(region,offset);

            for (Instance instance : response.getInstanceSet()){
                instances.add(EcsInstance.builder()
                        .provider(this.getProviderCode().getValue())
                        .instanceId(instance.getInstanceId())
                        .instanceName(instance.getInstanceName())
                        .status(instance.getInstanceState())
                        .osType(instance.getOsName())
                        .region(region)
                        .instanceSpec(instance.getCPU()+"C/"+instance.getMemory()+"GB")
                        .ipAddress(JSON.toJSONString(instance.getPublicIpAddresses()))
                        .instanceChargeType(instance.getInstanceChargeType())
                        .expiredTime(instance.getExpiredTime())
                        .detail(JSON.toJSONString(instance))
                        .build());
            }

            offset += 100L;
            if (instances.size() == response.getTotalCount()) {
                break;
            }
        }
        return instances;
    }

    private DescribeInstancesResponse executeDescribeInstances(String region,Long offset){
        return (DescribeInstancesResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            req.setOffset(offset);
            req.setLimit(100L);
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstances(req);
        },region, ProductEnum.ECS);
    }

    @Override
    public List<Region> listRegions() {

        DescribeRegionsResponse resp = (DescribeRegionsResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeRegionsRequest req = new DescribeRegionsRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeRegions(req);
        },null, ProductEnum.ECS);

        List<Region> regions = new ArrayList<>();

        for (RegionInfo regionInfo : resp.getRegionSet()){
            regions.add(Region.builder()
                    .providerCode(this.getProviderCode().getValue())
                    .productCode(this.getProductCode().name())
                    .regionCode(regionInfo.getRegion())
                    .regionName(regionInfo.getRegionName())
                    .build());
        }

        return regions;
    }

    @Override
    public CloudProviderEnum getProviderCode() {
        return CloudProviderEnum.TENCENT_CLOUD;
    }
}
