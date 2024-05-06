package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.gateway.pop.models.Response;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.DescribeImagesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeImagesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstanceTypesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstanceTypesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse;
import com.aliyun.sdk.service.ecs20140526.models.DescribeZonesRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeZonesResponse;
import com.zvz09.xiaochen.mc.component.product.ecs.AbstractEcsOperation;
import com.zvz09.xiaochen.mc.component.provider.aliyun.util.AliyunClientUtil;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AliYunEcsOperation extends AbstractEcsOperation<AliYunClient, Response> implements AliYunBaseOperation, InitializingBean {


    protected AliYunEcsOperation(AliYunClient client) {
        super(client, Integer.valueOf(50));
    }

    @Override
    protected Response executeDescribeInstances(String region, QueryParameter queryParameter) {
        return client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .pageSize(queryParameter.getPageSize())
                    .nextToken(StringUtils.isNotBlank(queryParameter.getNextToken()) ? queryParameter.getNextToken() : null)
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        }, region, this.getProductCode());
    }

    @Override
    protected Response executeDescribeInstance(String region, String instanceId) {
        return client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .regionId(region)
                    .instanceIds(String.format("[\"%s\"]", instanceId))
                    .pageSize(Integer.valueOf(1))
                    .build();
            return asyncClient.describeInstances(describeInstancesRequest).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter ecsInstancesHasNext(QueryParameter queryParameter, Response response) {
        DescribeInstancesResponse describeInstancesResponse = (DescribeInstancesResponse) response;
        if(StringUtils.isNotBlank(describeInstancesResponse.getBody().getNextToken())){
            queryParameter.setHaveNext(true);
            queryParameter.setNextToken(describeInstancesResponse.getBody().getNextToken());
        }else {
            queryParameter.setHaveNext(false);
        }
        return queryParameter;
    }

    @Override
    protected void addEcsInstances(String region, Response response, List<EcsInstance> instances) {
        DescribeInstancesResponse describeInstancesResponse = (DescribeInstancesResponse) response;
        describeInstancesResponse.getBody().getInstances().getInstance().forEach(instance -> {
            instances.add(super.buildEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),
                    instance.getOSName(),instance.getRegionId(),instance.getCpu() + "C/" + instance.getMemory() + "MiB",
                    JSON.toJSONString(instance.getPublicIpAddress().getIpAddress()),instance.getInstanceChargeType(),
                    instance.getExpiredTime(),JSON.toJSONString(instance)));
        });
    }


    @Override
    protected EcsInstance buildEcsInstance(String region, Response response) {
        DescribeInstancesResponse describeInstancesResponse = (DescribeInstancesResponse) response;
        if (describeInstancesResponse.getBody().getInstances().getInstance() != null
                && !describeInstancesResponse.getBody().getInstances().getInstance().isEmpty()) {
            DescribeInstancesResponseBody.Instance instance = describeInstancesResponse.getBody().getInstances().getInstance().get(0);
            return super.buildEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),
                    instance.getOSName(),instance.getRegionId(),instance.getCpu() + "C/" + instance.getMemory() + "MiB",
                    JSON.toJSONString(instance.getPublicIpAddress().getIpAddress()),instance.getInstanceChargeType(),
                    instance.getExpiredTime(),JSON.toJSONString(instance));
        }
        return null;
    }

    @Override
    protected Response executeGetRegions(QueryParameter queryParameter) {
        return (DescribeRegionsResponse) client.handleClient((client) -> {
            DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.builder()
                    .instanceChargeType("PostPaid")
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeRegions(describeRegionsRequest).get();
        }, null, this.getProductCode());
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, Response response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(Response response, List<Region> regions) {
        DescribeRegionsResponse describeRegionsResponse = (DescribeRegionsResponse) response;
        describeRegionsResponse.getBody().getRegions().getRegion().forEach(region -> {
            regions.add(this.convertedRegion(region.getRegionId(),region.getLocalName(),region.getRegionEndpoint()));
        });
    }


    @Override
    protected Response executeGetZones(String region, QueryParameter queryParameter) {
        return (DescribeZonesResponse) client.handleClient((client) -> {
            DescribeZonesRequest request = DescribeZonesRequest.builder()
                    .regionId(region)
                    .acceptLanguage("zh-CN")
                    .instanceChargeType("PostPaid")
                    .verbose(Boolean.valueOf(false))
                    .spotStrategy("NoSpot")
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            // Asynchronously get the return value of the API request
            return asyncClient.describeZones(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, Response response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, Response response, List<ZoneDTO> zones) {
        DescribeZonesResponse describeZonesResponse = (DescribeZonesResponse) response;
        if (describeZonesResponse.getBody().getZones() != null && describeZonesResponse.getBody().getZones().getZone() != null) {
            describeZonesResponse.getBody().getZones().getZone().forEach(zone -> {
                zones.add(new ZoneDTO(zone.getZoneId(), zone.getLocalName()));
            });
        }
    }

    @Override
    protected Response executeGetEcsInstanceTypes(String region, QueryParameter queryParameter) {
        return  (DescribeInstanceTypesResponse) client.handleClient((client) -> {
            DescribeInstanceTypesRequest request = DescribeInstanceTypesRequest.builder()
                    .nextToken(StringUtils.isBlank(queryParameter.getNextToken()) ? null : queryParameter.getNextToken())
                    .build();
            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeInstanceTypes(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter ecsInstanceTypesHasNext(QueryParameter queryParameter, Response response) {
        DescribeInstanceTypesResponse resp = (DescribeInstanceTypesResponse) response;

        return super.haveNext(queryParameter,resp.getBody().getNextToken());
    }

    @Override
    protected void addEcsInstanceTypes(String region, Response response, List<EcsInstanceType> ecsInstanceTypes) {
        DescribeInstanceTypesResponse describeInstanceTypesResponse = (DescribeInstanceTypesResponse) response;
        describeInstanceTypesResponse.getBody().getInstanceTypes().getInstanceType().forEach(output ->{
            ecsInstanceTypes.add(EcsInstanceType.builder()
                    .provider(this.getProviderCode().getValue())
                    .region(region)
                    .typeId(output.getInstanceTypeId())
                    .typeFamily(output.getInstanceTypeFamily())
                    .cpu(output.getCpuCoreCount())
                    .cpuModel(output.getPhysicalProcessorModel())
                    .cpuBaseFrequency(output.getCpuSpeedFrequency())
                    .cpuTurboFrequency(output.getCpuTurboFrequency())
                    .memory(Integer.valueOf((int) (output.getMemorySize() * 1024)))
                    .build());
        });
    }


    @Override
    protected Response executeGetImages(String region, QueryParameter queryParameter) {
        return (DescribeImagesResponse) client.handleClient((client) -> {
            DescribeImagesRequest request = DescribeImagesRequest.builder()
                    .regionId(region)
                    .pageNumber(queryParameter.getPageNumber())
                    .pageSize(queryParameter.getPageSize())
                    .build();

            AsyncClient asyncClient = (AsyncClient) client;
            return asyncClient.describeImages(request).get();
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter imagesHasNext(QueryParameter queryParameter, Response response) {
        DescribeImagesResponse resp = (DescribeImagesResponse) response;

        return super.haveNext(queryParameter,Long.valueOf(resp.getBody().getTotalCount()));
    }

    @Override
    protected void addImages(String region, Response response, List<ImageDTO> images) {
        DescribeImagesResponse describeImagesResponse = (DescribeImagesResponse) response;
        describeImagesResponse.getBody().getImages().getImage().forEach(image ->{
            ImageDTO imageDTO = this.converter.convertP2M(image,new ImageDTO());
            imageDTO.setRegion(region);
            images.add(imageDTO);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> regionEndpointMap = new HashMap<>();
        List<Region> regions = super.listRegions();
        regions.forEach(region -> {
            regionEndpointMap.put(region.getRegionCode(), region.getEndpoint());
        });
        AliyunClientUtil.setEcsEndpointMap(regionEndpointMap);
    }
}
