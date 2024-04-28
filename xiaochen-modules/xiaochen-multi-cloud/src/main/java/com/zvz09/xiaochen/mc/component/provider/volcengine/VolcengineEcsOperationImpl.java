package com.zvz09.xiaochen.mc.component.provider.volcengine;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.DescribeImagesRequest;
import com.volcengine.ecs.model.DescribeImagesResponse;
import com.volcengine.ecs.model.DescribeInstanceTypesRequest;
import com.volcengine.ecs.model.DescribeInstanceTypesResponse;
import com.volcengine.ecs.model.DescribeInstancesRequest;
import com.volcengine.ecs.model.DescribeInstancesResponse;
import com.volcengine.ecs.model.DescribeRegionsRequest;
import com.volcengine.ecs.model.DescribeRegionsResponse;
import com.volcengine.ecs.model.DescribeZonesRequest;
import com.volcengine.ecs.model.DescribeZonesResponse;
import com.volcengine.ecs.model.InstanceForDescribeInstancesOutput;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.DescribeEipAddressAttributesRequest;
import com.volcengine.vpc.model.DescribeEipAddressAttributesResponse;
import com.zvz09.xiaochen.mc.component.provider.EcsOperation;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class VolcengineEcsOperationImpl extends VolcengineBaseOperation implements EcsOperation {

    public VolcengineEcsOperationImpl(VolcengineClient volcengineClient) {
        super(volcengineClient);
    }

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();

        DescribeInstancesResponse response = executeDescribeInstances(region, null);
        addInstance(region, instances, response);
        while (StringUtils.isNotBlank(response.getNextToken())) {
            response = executeDescribeInstances(region, null);
            addInstance(region, instances, response);
        }

        return instances;
    }

    @Override
    public EcsInstance describeInstance(String region, String instanceId) {
        DescribeInstancesResponse response = (DescribeInstancesResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(1);
            describeInstancesRequest.setInstanceIds(List.of(instanceId));
            return api.describeInstances(describeInstancesRequest);
        }, region);
        if (response.getInstances() != null && !response.getInstances().isEmpty()) {
            return convertedInstance(region, response.getInstances().get(0));
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {
        List<Region> regions = new ArrayList<>();
        DescribeRegionsResponse response = executeGetRegions(null);

        addRegion(response, regions);

        while (StringUtils.isNotBlank(response.getNextToken())) {
            response = executeGetRegions(response.getNextToken());
            addRegion(response, regions);
        }

        return regions;
    }

    @Override
    public List<ZoneDTO> listZones(String region) {
        List<ZoneDTO> zones = new ArrayList<>();
        DescribeZonesResponse response = (DescribeZonesResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeZonesRequest request = new DescribeZonesRequest();
            return api.describeZones(request);
        }, region);

        response.getZones().forEach(zone -> {
            new ZoneDTO(zone.getZoneId(), zone.getZoneId());
        });

        return zones;
    }

    @Override
    public List<ImageDTO> listAllImages(String region) {
        List<ImageDTO> images = new ArrayList<>();
        DescribeImagesResponse response = executeDescribeImages(region, null);

        addImages(region, response, images);
        while (StringUtils.isNotBlank(response.getNextToken())) {
            response = executeDescribeImages(region, response.getNextToken());
            addImages(region, response, images);
        }

        return images;
    }

    @Override
    public List<EcsInstanceType> listAllInstanceTypes(String region) {
        List<EcsInstanceType> instanceTypes = new ArrayList<>();
        DescribeInstanceTypesResponse response = executeDescribeInstanceTypes(region, null);

        addInstanceTypes(region, response, instanceTypes);
        while (StringUtils.isNotBlank(response.getNextToken())) {
            response = executeDescribeInstanceTypes(region, response.getNextToken());
            addInstanceTypes(region, response, instanceTypes);
        }

        return instanceTypes;
    }

    private void addInstance(String region, List<EcsInstance> instances, DescribeInstancesResponse response) {
        response.getInstances().forEach(instance -> {
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

    private void addRegion(DescribeRegionsResponse response, List<Region> regions) {
        response.getRegions().forEach(region -> {
            regions.add(Region.builder()
                    .providerCode(this.getProviderCode().getValue())
                    .productCode(this.getProductCode().name())
                    .regionCode(region.getRegionId())
                    .regionName(region.getRegionId())
                    .build());
        });
    }

    private DescribeRegionsResponse executeGetRegions(String nextToken) {
        return (DescribeRegionsResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
            describeRegionsRequest.setMaxResults(20);
            if (StringUtils.isNotBlank(nextToken)) {
                describeRegionsRequest.setNextToken(nextToken);
            }
            return api.describeRegions(describeRegionsRequest);
        }, null);

    }

    private DescribeInstancesResponse executeDescribeInstances(String region, String nextToken) {
        return (DescribeInstancesResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(100);

            if (StringUtils.isNotBlank(nextToken)) {
                describeInstancesRequest.setNextToken(nextToken);
            }
            return api.describeInstances(describeInstancesRequest);
        }, region);
    }

    private DescribeEipAddressAttributesResponse executeDescribeEipAddressAttributes(String region, String ipId) {
        if (StringUtils.isBlank(ipId)) {
            return null;
        }
        return (DescribeEipAddressAttributesResponse) volcengineClient.handleClient((client) -> {
            VpcApi api = new VpcApi(client);
            DescribeEipAddressAttributesRequest describeEipAddressAttributesRequest = new DescribeEipAddressAttributesRequest();
            describeEipAddressAttributesRequest.setAllocationId(ipId);
            return api.describeEipAddressAttributes(describeEipAddressAttributesRequest);
        }, region);
    }

    private DescribeInstanceTypesResponse executeDescribeInstanceTypes(String region, String nextToken) {
        return (DescribeInstanceTypesResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstanceTypesRequest request = new DescribeInstanceTypesRequest();
            request.setMaxResults(500);

            if (StringUtils.isNotBlank(nextToken)) {
                request.setNextToken(nextToken);
            }
            return api.describeInstanceTypes(request);
        }, region);
    }

    private void addInstanceTypes(String region, DescribeInstanceTypesResponse response, List<EcsInstanceType> ecsInstanceTypes) {
        response.getInstanceTypes().forEach(output -> {
            ecsInstanceTypes.add(EcsInstanceType.builder()
                    .provider(this.getProviderCode().getValue())
                    .region(region)
                    .typeId(output.getInstanceTypeId())
                    .typeFamily(output.getInstanceTypeFamily())
                    .cpu(output.getProcessor().getCpus())
                    .cpuModel(output.getProcessor().getModel())
                    .cpuBaseFrequency(output.getProcessor().getBaseFrequency())
                    .cpuTurboFrequency(output.getProcessor().getTurboFrequency())
                    .memory(output.getMemory().getSize())
                    .localVolumes(JSON.toJSONString(output.getLocalVolumes()))
                    .volume(JSON.toJSONString(output.getVolume()))
                    .build());
        });
    }


    private DescribeImagesResponse executeDescribeImages(String region, String nextToken) {
        return (DescribeImagesResponse) volcengineClient.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeImagesRequest request = new DescribeImagesRequest();
            request.setMaxResults(100);

            if (StringUtils.isNotBlank(nextToken)) {
                request.setNextToken(nextToken);
            }
            return api.describeImages(request);
        }, region);
    }

    private void addImages(String region, DescribeImagesResponse response, List<ImageDTO> images) {
        response.getImages().forEach(output -> {
            images.add(ImageDTO.builder()
                    .region(region)
                    .architecture(output.getArchitecture())
                    .bootMode(output.getBootMode())
                    .description(output.getDescription())
                    .imageId(output.getImageId())
                    .imageName(output.getImageName())
                    .isSupportCloudInit(output.isIsSupportCloudInit())
                    .osName(output.getOsName())
                    .osType(output.getOsType())
                    .platform(output.getPlatform())
                    .platformVersion(output.getPlatformVersion())
                    .size(output.getSize())
                    .status(output.getStatus())
                    .build());
        });
    }
}
