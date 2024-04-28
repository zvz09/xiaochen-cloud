package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeImagesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeImagesResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstanceTypeConfigsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstanceTypeConfigsResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Image;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.cvm.v20170312.models.RegionInfo;
import com.tencentcloudapi.cvm.v20170312.models.ZoneInfo;
import com.zvz09.xiaochen.mc.component.provider.EcsOperation;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TencentCloudEcsOperationImpl extends TencentCloudBaseOperation implements EcsOperation {

    public TencentCloudEcsOperationImpl(TencentCloudClient tencentCloudClient) {
        super(tencentCloudClient);
    }

    @Override
    public List<EcsInstance> listEcsInstances(String region) {
        List<EcsInstance> instances = new ArrayList<>();

        // 查询实例列表
        long offset = 0;
        while (true){
            DescribeInstancesResponse response = executeDescribeInstances(region,offset);

            for (Instance instance : response.getInstanceSet()){
                instances.add(convertedInstance(region, instance));
            }

            offset += 100L;
            if (instances.size() == response.getTotalCount()) {
                break;
            }
        }
        return instances;
    }

    @Override
    public EcsInstance describeInstance(String region, String instanceId) {
        DescribeInstancesResponse response =  (DescribeInstancesResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            req.setOffset(0L);
            req.setLimit(1L);
            req.setInstanceIds(new String[]{instanceId});
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstances(req);
        },region, this.getProductCode());
        if(response.getInstanceSet() !=null && response.getInstanceSet().length > 0){
            Instance instance = response.getInstanceSet()[0];
            return convertedInstance(region, instance);
        }
        return null;
    }

    @Override
    public List<Region> listRegions() {

        DescribeRegionsResponse resp = (DescribeRegionsResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeRegionsRequest req = new DescribeRegionsRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeRegions(req);
        },null, this.getProductCode());

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
    public List<ZoneDTO> listZones(String region) {
        List<ZoneDTO> zones = new ArrayList<>();
        DescribeZonesResponse resp = (DescribeZonesResponse) tencentCloudClient.handleClient((abstractClient) -> {
            DescribeZonesRequest req = new DescribeZonesRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeZones(req);
        }, null, this.getProductCode());
        if (resp.getZoneSet() != null) {
            for (ZoneInfo zone : resp.getZoneSet()) {
                zones.add(new ZoneDTO(zone.getZone(), zone.getZoneName()));
            }
        }

        return zones;
    }

    @Override
    public List<ImageDTO> listAllImages(String region) {
        List<ImageDTO> images = new ArrayList<>();

        // 查询实例列表
        long offset = 0;
        while (true){
            DescribeImagesResponse response = executeDescribeImages(region,offset);

            Arrays.stream(response.getImageSet()).filter(image -> "NORMAL".equals(image.getImageState())).forEach(image -> {
                images.add(ImageDTO.builder()
                        .region(region)
                        .architecture(image.getArchitecture())
                        .description(image.getImageDescription())
                        .imageId(image.getImageId())
                        .imageName(image.getImageName())
                        .isSupportCloudInit(image.getIsSupportCloudinit())
                        .osName(image.getOsName())
                        .platform(image.getPlatform())
                        .size(Math.toIntExact(image.getImageSize()))
                        .status(image.getImageState())
                        .build());
            });

            offset += 100L;
            if (images.size() == response.getTotalCount()) {
                break;
            }
        }
        return images;
    }


    @Override
    public List<EcsInstanceType> listAllInstanceTypes(String region) {
        List<EcsInstanceType> instanceTypes = new ArrayList<>();

        DescribeInstanceTypeConfigsResponse resp = (DescribeInstanceTypeConfigsResponse ) tencentCloudClient.handleClient((abstractClient) -> {
            DescribeInstanceTypeConfigsRequest req = new DescribeInstanceTypeConfigsRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstanceTypeConfigs(req);
        }, region, this.getProductCode());

        Arrays.stream(resp.getInstanceTypeConfigSet()).forEach(instanceType -> {
            instanceTypes.add(EcsInstanceType.builder()
                    .provider(this.getProviderCode().getValue())
                    .region(region)
                    .typeId(instanceType.getInstanceType())
                    .typeFamily(instanceType.getInstanceFamily())
                    .cpu(Math.toIntExact(instanceType.getCPU()))
                    .memory(Math.toIntExact(instanceType.getMemory()))
                    .build());
        });

        return instanceTypes;
    }

    private DescribeInstancesResponse executeDescribeInstances(String region,Long offset){
        return (DescribeInstancesResponse) tencentCloudClient.handleClient((abstractClient)->{
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            req.setOffset(offset);
            req.setLimit(100L);
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstances(req);
        },region, this.getProductCode());
    }

    private DescribeImagesResponse executeDescribeImages(String region, Long offset){
        return (DescribeImagesResponse ) tencentCloudClient.handleClient((abstractClient)->{
            DescribeImagesRequest req = new DescribeImagesRequest ();
            req.setOffset(offset);
            req.setLimit(100L);
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeImages(req);
        },region, this.getProductCode());
    }


    private EcsInstance convertedInstance(String region, Instance instance) {
        return EcsInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(instance.getInstanceId())
                .instanceName(instance.getInstanceName())
                .status(instance.getInstanceState())
                .osType(instance.getOsName())
                .region(region)
                .instanceSpec(instance.getCPU() + "C/" + instance.getMemory() + "GB")
                .ipAddress(JSON.toJSONString(instance.getPublicIpAddresses()))
                .instanceChargeType(instance.getInstanceChargeType())
                .expiredTime(instance.getExpiredTime())
                .detail(JSON.toJSONString(instance))
                .build();
    }
}