package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse;
import com.zvz09.xiaochen.mc.component.provider.EcsOperation;
import com.zvz09.xiaochen.mc.component.provider.aliyun.util.AliyunClientUtil;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AliYunEcsOperationImpl implements EcsOperation, InitializingBean {

    private final static Integer PAGE_SIZE = 100;

    private final AliYunClient aliYunClient;

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();
        DescribeInstancesResponse response = (DescribeInstancesResponse) aliYunClient.handleClient((client)->{
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .pageSize(PAGE_SIZE)
                    .build();
                return asyncClient.describeInstances(describeInstancesRequest).get();
        },region, this.getProductCode());

        addInstances(instances,response);
        while (StringUtils.isNotBlank(response.getBody().getNextToken())){
            response = executeDescribeInstances(region,response.getBody().getNextToken());
            addInstances(instances,response);
        }

        return instances;
    }

    @Override
    public EcsInstance describeInstance(String region, String instanceId) {
        DescribeInstancesResponse response = (DescribeInstancesResponse) aliYunClient.handleClient((client)->{
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .instanceIds(String.format("[\"%s\"]",instanceId))
                    .pageSize(1)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        },region, this.getProductCode());
        if(response.getBody().getInstances().getInstance() != null && !response.getBody().getInstances().getInstance().isEmpty()){
            DescribeInstancesResponseBody.Instance instance = response.getBody().getInstances().getInstance().get(0);
            return convertedInstance(instance);
        }
        return null;
    }

    private EcsInstance convertedInstance(DescribeInstancesResponseBody.Instance instance) {
        return EcsInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(instance.getInstanceId())
                .instanceName(instance.getInstanceName())
                .status(instance.getStatus())
                .osType(instance.getOSName())
                .region(instance.getRegionId())
                .instanceSpec(instance.getCpu() + "C/" + instance.getMemory() + "MiB")
                .ipAddress(JSON.toJSONString(instance.getPublicIpAddress().getIpAddress()))
                .instanceChargeType(instance.getInstanceChargeType())
                .expiredTime(instance.getExpiredTime())
                .detail(JSON.toJSONString(instance))
                .build();
    }

    private void addInstances(List<EcsInstance> instances, DescribeInstancesResponse response) {
        response.getBody().getInstances().getInstance().forEach(instance -> {
            instances.add(convertedInstance(instance));
        });
    }

    private DescribeInstancesResponse executeDescribeInstances(String region, String nextToken) {
       return  (DescribeInstancesResponse) aliYunClient.handleClient((client)->{
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .nextToken(nextToken)
                    .pageSize(PAGE_SIZE)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        },region, this.getProductCode());
    }

    @Override
    public List<Region> listRegions() {
        DescribeRegionsResponse response = executeDescribeRegionsResponse();

        List<Region> regions = new ArrayList<>();

        response.getBody().getRegions().getRegion().forEach(region -> {
            regions.add(Region.builder()
                            .providerCode(this.getProviderCode().getValue())
                            .productCode(this.getProductCode().name())
                    .regionCode(region.getRegionId())
                    .regionName(region.getLocalName())
                    .build());
        });

        return regions;
    }

    private DescribeRegionsResponse executeDescribeRegionsResponse() {
        DescribeRegionsResponse response = (DescribeRegionsResponse) aliYunClient.handleClient((client)->{
            DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.builder()
                    .instanceChargeType("PostPaid")
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            // Asynchronously get the return value of the API request
            return asyncClient.describeRegions(describeRegionsRequest).get();
        },null, this.getProductCode());
        return response;
    }

    @Override
    public CloudProviderEnum getProviderCode() {
        return CloudProviderEnum.ALI_YUN;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,String> regionEndpointMap = new HashMap<>();
        DescribeRegionsResponse response = executeDescribeRegionsResponse();
        response.getBody().getRegions().getRegion().forEach(region -> {
            regionEndpointMap.put(region.getRegionId(),region.getRegionEndpoint());
        });
        AliyunClientUtil.setEcsEndpointMap(regionEndpointMap);
    }
}
