package com.zvz09.xiaochen.mc.component.provider;

import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface EcsOperation extends BaseProductService {

    default List<EcsInstance> listEcsInstances(){
        List<EcsInstance> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    List<EcsInstance> listEcsInstances(String region);

    EcsInstance describeInstance(String region, String instanceId);

    default EcsInstance convertedEcsInstance(String instanceId, String instanceName, String status, String osType, String region,
                                     String instanceSpec, String ipAddress, String instanceChargeType, String expiredTime,
                                             String detail) {
        return EcsInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(instanceId)
                .instanceName(instanceName)
                .status(status)
                .osType(osType)
                .region(region)
                .instanceSpec(instanceSpec)
                .ipAddress(ipAddress)
                .instanceChargeType(instanceChargeType)
                .expiredTime(expiredTime)
                .detail(detail)
                .build();
    }

    List<Region> listRegions();

    default Region convertedRegion(String regionCode, String regionName, String endpoint) {
        return Region.builder()
                .providerCode(this.getProviderCode().getValue())
                .productCode(this.getProductCode().name())
                .regionCode(regionCode)
                .regionName(regionName)
                .endpoint(endpoint)
                .build();
    }

    List<ZoneDTO> listZones(String region);

    default ZoneDTO convertedZoneDTO(String zoneId, String zoneName){
        return new ZoneDTO(zoneId, zoneName);
    }
    default List<EcsInstanceType> listAllInstanceTypes(){
        List<EcsInstanceType> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listAllInstanceTypes(region.getRegionCode()));
        });
        return instances;
    }

    List<EcsInstanceType> listAllInstanceTypes(String region);

    default EcsInstanceType convertedEcsInstanceType(String region, String typeId, String typeFamily, Integer cpu, String cpuModel,
                                                     Float cpuBaseFrequency, Float cpuTurboFrequency, Integer memory,
                                                     String localVolumes, String volume) {
        return EcsInstanceType.builder()
                .provider(this.getProviderCode().getValue())
                .region(region)
                .typeId(typeId)
                .typeFamily(typeFamily)
                .cpu(cpu)
                .cpuModel(cpuModel)
                .cpuBaseFrequency(cpuBaseFrequency)
                .cpuTurboFrequency(cpuTurboFrequency)
                .memory(memory)
                .localVolumes(localVolumes)
                .volume(volume)
                .build();
    }

    default List<ImageDTO> listAllImages(){
        List<ImageDTO> images = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            images.addAll(this.listAllImages(region.getRegionCode()));
        });
        return images;
    }

    List<ImageDTO> listAllImages(String region);

    default ImageDTO convertedImageDTO(String region, String architecture, String bootMode, String description, String imageId,
                                       String imageName, Boolean isSupportCloudInit, String osName, String osType, String platform,
                                       String platformVersion, Integer size, String status) {
        return ImageDTO.builder()
                .region(region)
                .architecture(architecture)
                .bootMode(bootMode)
                .description(description)
                .imageId(imageId)
                .imageName(imageName)
                .isSupportCloudInit(isSupportCloudInit)
                .osName(osName)
                .osType(osType)
                .platform(platform)
                .platformVersion(platformVersion)
                .size(size)
                .status(status)
                .build();
    }

    default ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };
}
