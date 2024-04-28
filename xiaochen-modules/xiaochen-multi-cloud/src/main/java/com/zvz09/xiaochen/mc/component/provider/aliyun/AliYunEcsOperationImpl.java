package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.DescribeImagesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeImagesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeImagesResponseBody;
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
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
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
import java.util.concurrent.CopyOnWriteArrayList;

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
            return this.convertedEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),
                    instance.getOSName(),instance.getRegionId(),instance.getCpu() + "C/" + instance.getMemory() + "MiB",
                    JSON.toJSONString(instance.getPublicIpAddress().getIpAddress()),instance.getInstanceChargeType(),
                    instance.getExpiredTime(),JSON.toJSONString(instance));
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {
        DescribeRegionsResponse response = executeDescribeRegionsResponse();

        List<Region> regions = new ArrayList<>();

        response.getBody().getRegions().getRegion().forEach(region -> {
            regions.add(this.convertedRegion(region.getRegionId(),region.getLocalName(),region.getRegionEndpoint()));
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
                zones.add( this.convertedZoneDTO(zone.getZoneId(), zone.getLocalName()));
            });
        }
        return zones;
    }

    @Override
    public List<ImageDTO> listAllImages(String region) {
        List<ImageDTO> images = new CopyOnWriteArrayList<>();

        DescribeImagesResponse response = executeDescribeImages(region, 1, PAGE_SIZE);

        addImages(region,images, response.getBody().getImages().getImage());

        List<Integer> pages = new ArrayList<>();
        for (int i= 2;i<=response.getBody().getTotalCount()/PAGE_SIZE+1;i++){
            pages.add(i);
        }

        pages.parallelStream().forEach(page -> {
            DescribeImagesResponse describeImages = executeDescribeImages(region, page, PAGE_SIZE);
            addImages(region,images, describeImages.getBody().getImages().getImage());
        });

        return images;
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
            instances.add(this.convertedEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),
                    instance.getOSName(),instance.getRegionId(),instance.getCpu() + "C/" + instance.getMemory() + "MiB",
                    JSON.toJSONString(instance.getPublicIpAddress().getIpAddress()),instance.getInstanceChargeType(),
                    instance.getExpiredTime(),JSON.toJSONString(instance)));
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

    private DescribeImagesResponse executeDescribeImages(String region,Integer pageNumber,Integer pageSize) {
        return (DescribeImagesResponse) aliYunClient.handleClient((client) -> {
            DescribeImagesRequest request = DescribeImagesRequest.builder()
                    .regionId(region)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .build();

            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeImages(request).get();
        }, region, this.getProductCode());
    }
    private void addImages(String region,List<ImageDTO> images, List<DescribeImagesResponseBody.Image> response) {
        response.forEach(image -> {
            images.add(this.convertedImageDTO(region,image.getArchitecture(),null,image.getDescription(),image.getImageId(),
                    image.getImageName(),image.getIsSupportCloudinit(),image.getOSName(),null,image.getPlatform(),
                    null,Math.toIntExact(image.getSize()),
                    image.getStatus()));
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
