package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstanceTypesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstanceTypesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeZonesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeZonesResponse;
import com.zvz09.xiaochen.mc.component.provider.EcsOperation;
import com.zvz09.xiaochen.mc.component.provider.aliyun.util.AliyunClientUtil;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AliYunEcsOperationImpl extends AliYunBaseOperation implements EcsOperation, InitializingBean {

    private final static Integer PAGE_SIZE = 100;

    public AliYunEcsOperationImpl(AliYunClient aliYunClient) {
        super(aliYunClient);
    }

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();
        DescribeInstancesResponse response = (DescribeInstancesResponse) aliYunClient.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .pageSize(PAGE_SIZE)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        }, region, this.getProductCode());

        addInstances(instances, response);
        while (StringUtils.isNotBlank(response.getBody().getNextToken())) {
            response = executeDescribeInstances(region, response.getBody().getNextToken());
            addInstances(instances, response);
        }

        return instances;
    }

    @Override
    public EcsInstance describeInstance(String region, String instanceId) {
        DescribeInstancesResponse response = (DescribeInstancesResponse) aliYunClient.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .instanceIds(String.format("[\"%s\"]", instanceId))
                    .pageSize(1)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        }, region, this.getProductCode());
        if (response.getBody().getInstances().getInstance() != null && !response.getBody().getInstances().getInstance().isEmpty()) {
            DescribeInstancesResponseBody.Instance instance = response.getBody().getInstances().getInstance().get(0);
            return convertedInstance(instance);
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {
        DescribeRegionsResponse response = executeDescribeRegionsResponse();

        List<Region> regions = new ArrayList<>();

        response.getBody().getRegions().getRegion().forEach(region -> {
            regions.add(Region.builder()
                    .providerCode(this.getProviderCode().getValue())
                    .productCode(this.getProductCode().getValue())
                    .regionCode(region.getRegionId())
                    .endpoint(region.getRegionEndpoint())
                    .regionName(region.getLocalName())
                    .build());
        });

        return regions;
    }

    @Override
    public List<ZoneDTO> listZones(String region) {
        List<ZoneDTO> zones = new ArrayList<>();
        DescribeZonesResponse response = (DescribeZonesResponse) aliYunClient.handleClient((client) -> {
            DescribeZonesRequest request = DescribeZonesRequest.builder()
                    .regionId(region)
                    .acceptLanguage("zh-CN")
                    .instanceChargeType("PostPaid")
                    .verbose(false)
                    .spotStrategy("NoSpot")
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            // Asynchronously get the return value of the API request
            return asyncClient.describeZones(request).get();
        }, null, this.getProductCode());

        if (response.getBody().getZones() != null && response.getBody().getZones().getZone() != null) {
            response.getBody().getZones().getZone().forEach(zone -> {
                zones.add(new ZoneDTO(zone.getZoneId(), zone.getLocalName()));
            });
        }
        return zones;
    }

    @Override
    public List<EcsInstanceType> listAllInstanceTypes(String region) {
        List<EcsInstanceType> instanceTypes = new ArrayList<>();
        DescribeInstanceTypesResponse response = executeDescribeInstanceTypes(region, null);
        addInstanceTypes(region,response , instanceTypes);

        while (StringUtils.isNotBlank(response.getBody().getNextToken())){
            response = executeDescribeInstanceTypes(region, response.getBody().getNextToken());
            addInstanceTypes(region,response , instanceTypes);
        }

        return instanceTypes;
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

    private DescribeInstancesResponse executeDescribeInstances(String region, String nextToken) {
        return (DescribeInstancesResponse) aliYunClient.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .nextToken(nextToken)
                    .pageSize(PAGE_SIZE)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        }, region, this.getProductCode());
    }

    private void addInstances(List<EcsInstance> instances, DescribeInstancesResponse response) {
        response.getBody().getInstances().getInstance().forEach(instance -> {
            instances.add(convertedInstance(instance));
        });
    }

    private DescribeRegionsResponse executeDescribeRegionsResponse() {
        return (DescribeRegionsResponse) aliYunClient.handleClient((client) -> {
            DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.builder()
                    .instanceChargeType("PostPaid")
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeRegions(describeRegionsRequest).get();
        }, null, this.getProductCode());
    }

    private DescribeInstanceTypesResponse executeDescribeInstanceTypes(String region,String nextToken) {
        return (DescribeInstanceTypesResponse) aliYunClient.handleClient((client) -> {
            DescribeInstanceTypesRequest request = DescribeInstanceTypesRequest.builder()
                    .nextToken(StringUtils.isBlank(nextToken) ? null : nextToken)
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeInstanceTypes(request).get();
        }, region, this.getProductCode());
    }

    private void addInstanceTypes(String region, DescribeInstanceTypesResponse response, List<EcsInstanceType> ecsInstanceTypes) {
        response.getBody().getInstanceTypes().getInstanceType().forEach(output ->{
            ecsInstanceTypes.add(EcsInstanceType.builder()
                    .provider(this.getProviderCode().getValue())
                    .region(region)
                    .typeId(output.getInstanceTypeId())
                    .typeFamily(output.getInstanceTypeFamily())
                    .cpu(output.getCpuCoreCount())
                    .cpuModel(output.getPhysicalProcessorModel())
                    .cpuBaseFrequency(output.getCpuSpeedFrequency())
                    .cpuTurboFrequency(output.getCpuTurboFrequency())
                    .memory((int) (output.getMemorySize() * 1024))
                    .build());
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> regionEndpointMap = new HashMap<>();
        DescribeRegionsResponse response = executeDescribeRegionsResponse();
        response.getBody().getRegions().getRegion().forEach(region -> {
            regionEndpointMap.put(region.getRegionId(), region.getRegionEndpoint());
        });
        AliyunClientUtil.setEcsEndpointMap(regionEndpointMap);
    }
}
