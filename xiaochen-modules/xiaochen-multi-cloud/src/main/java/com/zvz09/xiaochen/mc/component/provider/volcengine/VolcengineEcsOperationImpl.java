package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.DescribeInstancesRequest;
import com.volcengine.ecs.model.DescribeInstancesResponse;
import com.volcengine.ecs.model.DescribeRegionsRequest;
import com.volcengine.ecs.model.DescribeRegionsResponse;
import com.volcengine.ecs.model.InstanceForDescribeInstancesOutput;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.DescribeEipAddressAttributesRequest;
import com.volcengine.vpc.model.DescribeEipAddressAttributesResponse;
import com.zvz09.xiaochen.mc.component.provider.EcsOperation;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VolcengineEcsOperationImpl implements EcsOperation {

    private final VolcengineClient volcengineClient;

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();


        DescribeInstancesResponse response = executeDescribeInstances(region,null);
        addInstance(region, instances, response);
        while (StringUtils.isNotBlank(response.getNextToken())){
            response = executeDescribeInstances(region,null);
            addInstance(region, instances, response);
        }

        return instances;
    }

    @Override
    public EcsInstance describeInstance(String region, String instanceId) {
        DescribeInstancesResponse response = (DescribeInstancesResponse) volcengineClient.handleClient((client)->{
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(1);
            describeInstancesRequest.setInstanceIds(List.of(instanceId));
            return api.describeInstances(describeInstancesRequest);
        },region);
        if(response.getInstances()!=null && !response.getInstances().isEmpty()){
            return convertedInstance(region,response.getInstances().get(0));
        }
        return null;
    }

    private void addInstance(String region, List<EcsInstance> instances, DescribeInstancesResponse response) {
        response.getInstances().forEach(instance ->{
            instances.add(convertedInstance(region, instance));
        });
    }

    private EcsInstance convertedInstance(String region, InstanceForDescribeInstancesOutput instance) {
        return EcsInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(instance.getInstanceId())
                .instanceName(instance.getInstanceName())
                .status(instance.getStatus())
                .osType(instance.getOsName())
                .region(region)
                .instanceSpec(instance.getCpus() + "C/" + instance.getMemorySize() + "MiB")
                .ipAddress(Objects.requireNonNull(executeDescribeEipAddressAttributes(region, instance.getEipAddress().getAllocationId())).getEipAddress())
                .instanceChargeType(instance.getInstanceChargeType())
                .expiredTime(instance.getExpiredAt())
                .detail(JSON.toJSONString(instance))
                .build();
    }

    @Override
    public List<Region> listRegions() {
        List<Region> regions = new ArrayList<>();
        DescribeRegionsResponse response = executeGetRegions(null);

        addRegion(response, regions);

        while (StringUtils.isNotBlank(response.getNextToken())){
            response = executeGetRegions(response.getNextToken());
            addRegion(response, regions);
        }

        return regions;
    }

    private void addRegion(DescribeRegionsResponse response, List<Region> regions) {
        response.getRegions().forEach(region ->{
            regions.add(Region.builder()
                    .providerCode(this.getProviderCode().getValue())
                    .productCode(this.getProductCode().name())
                    .regionCode(region.getRegionId())
                    .regionName(region.getRegionId())
                    .build());
        });
    }

    private DescribeRegionsResponse executeGetRegions(String nextToken) {
        return  (DescribeRegionsResponse) volcengineClient.handleClient((client)->{
            EcsApi api = new EcsApi(client);
            DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
            describeRegionsRequest.setMaxResults(20);
            if(StringUtils.isNotBlank(nextToken)){
                describeRegionsRequest.setNextToken(nextToken);
            }
            return api.describeRegions(describeRegionsRequest);
        },null);

    }

    private DescribeInstancesResponse executeDescribeInstances(String region,String nextToken){
        return (DescribeInstancesResponse) volcengineClient.handleClient((client)->{
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(100);

            if(StringUtils.isNotBlank(nextToken)){
                describeInstancesRequest.setNextToken(nextToken);
            }
            return api.describeInstances(describeInstancesRequest);
        },region);
    }

    private DescribeEipAddressAttributesResponse executeDescribeEipAddressAttributes(String region,String ipId){
        if(StringUtils.isBlank(ipId)){
            return null;
        }
        return (DescribeEipAddressAttributesResponse) volcengineClient.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeEipAddressAttributesRequest describeEipAddressAttributesRequest = new DescribeEipAddressAttributesRequest();
            describeEipAddressAttributesRequest.setAllocationId(ipId);
            return   api.describeEipAddressAttributes(describeEipAddressAttributesRequest);
        },region);
    }

    @Override
    public CloudProviderEnum getProviderCode() {
        return CloudProviderEnum.VOLCENGINE;
    }
}
