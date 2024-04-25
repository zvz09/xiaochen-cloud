package com.zvz09.xiaochen.mc.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import com.zvz09.xiaochen.mc.component.service.IEcsService;
import com.zvz09.xiaochen.mc.component.service.IVpcService;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.service.IEcsInstanceService;
import com.zvz09.xiaochen.mc.service.IRegionService;
import com.zvz09.xiaochen.mc.service.IVpcInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncInstanceJob {

    private final IEcsInstanceService ecsInstanceService;
    private final IVpcInstanceService vpcInstanceService;
    private final IRegionService regionService;
    private final IEcsService ecsService;
    private final IVpcService vpcService;

    @XiaoChenJob("syncEcsInstanceJob")
    public void syncEcsInstanceJob() {
        Arrays.stream(CloudProviderEnum.getAllEnums()).parallel().forEach(cloudProviderEnum -> {
            List<String> dbInstanceIds = ecsInstanceService.listObjs(new QueryWrapper<EcsInstance>()
                    .lambda().eq(EcsInstance::getProvider, cloudProviderEnum.getValue())
                    .select(EcsInstance::getInstanceId), Object::toString);
            List<EcsInstance> instances = ecsService.listAllEcsInstances(cloudProviderEnum.getValue());
            List<String> instanceIds = new ArrayList<>();
            List<EcsInstance> updateInstances = new ArrayList<>();
            List<EcsInstance> saveInstances = new ArrayList<>();
            instances.stream().parallel().forEach(instance -> {
                if (dbInstanceIds.contains(instance.getInstanceId())) {
                    updateInstances.add(instance);
                } else {
                    saveInstances.add(instance);
                }
                instanceIds.add(instance.getInstanceId());
            });

            updateInstances.forEach(instance -> {
                ecsInstanceService.update(new LambdaUpdateWrapper<EcsInstance>()
                        .set(EcsInstance::getInstanceId, instance.getInstanceId())
                        .set(EcsInstance::getStatus, instance.getStatus())
                        .set(EcsInstance::getOsType, instance.getOsType())
                        .set(EcsInstance::getRegion, instance.getRegion())
                        .set(EcsInstance::getInstanceSpec, instance.getInstanceSpec())
                        .set(EcsInstance::getIpAddress, instance.getIpAddress())
                        .set(EcsInstance::getInstanceChargeType, instance.getInstanceChargeType())
                        .set(EcsInstance::getExpiredTime, instance.getExpiredTime())
                        .set(EcsInstance::getDetail, instance.getDetail())
                        .eq(EcsInstance::getInstanceId, instance.getInstanceId())
                );
            });

            saveInstances.forEach(ecsInstanceService::save);

            dbInstanceIds.removeAll(instanceIds);
            if (!dbInstanceIds.isEmpty()) {
                List<String> deleteInstances = new ArrayList<>();
                List<EcsInstance> finalUpdateInstances = new ArrayList<>();
                dbInstanceIds.stream().parallel().forEach(instanceId -> {
                    EcsInstance ecsInstance = this.ecsInstanceService.getOne(new LambdaQueryWrapper<EcsInstance>().eq(EcsInstance::getInstanceId, instanceId));
                    if (ecsInstance != null) {
                        EcsInstance providerInstance = ecsService.describeInstance(ecsInstance.getProvider(), ecsInstance.getRegion(), ecsInstance.getInstanceId());
                        if (providerInstance == null) {
                            deleteInstances.add(instanceId);
                        } else {
                            finalUpdateInstances.add(providerInstance);
                        }
                    }
                });

                deleteInstances.forEach(instanceId -> {
                    this.ecsInstanceService.update(new LambdaUpdateWrapper<EcsInstance>().set(EcsInstance::getDeleted, true)
                            .eq(EcsInstance::getInstanceId, instanceId));
                });

                finalUpdateInstances.forEach(providerInstance -> {
                    ecsInstanceService.update(new LambdaUpdateWrapper<EcsInstance>()
                            .set(EcsInstance::getInstanceId, providerInstance.getInstanceId())
                            .set(EcsInstance::getStatus, providerInstance.getStatus())
                            .set(EcsInstance::getOsType, providerInstance.getOsType())
                            .set(EcsInstance::getRegion, providerInstance.getRegion())
                            .set(EcsInstance::getInstanceSpec, providerInstance.getInstanceSpec())
                            .set(EcsInstance::getIpAddress, providerInstance.getIpAddress())
                            .set(EcsInstance::getInstanceChargeType, providerInstance.getInstanceChargeType())
                            .set(EcsInstance::getExpiredTime, providerInstance.getExpiredTime())
                            .set(EcsInstance::getDetail, providerInstance.getDetail())
                            .eq(EcsInstance::getInstanceId, providerInstance.getInstanceId())
                    );
                });
            }
        });

    }

    @XiaoChenJob("syncRegionInfo")
    public void syncRegionInfo() {
        Arrays.stream(CloudProviderEnum.getAllEnums()).parallel().forEach(cloudProviderEnum -> {
            List<String> dbRegionCodes = regionService.listObjs(new QueryWrapper<Region>()
                    .lambda().eq(Region::getProviderCode, cloudProviderEnum.getValue())
                    .select(Region::getRegionCode), Object::toString);
            List<Region> regions = ecsService.listAllRegions(cloudProviderEnum.getValue());
            List<String> regionCodes = new ArrayList<>();
            List<Region> updateRegions = new ArrayList<>();
            List<Region> saveRegions = new ArrayList<>();
            regions.stream().parallel().forEach(region -> {
                if (dbRegionCodes.contains(region.getRegionCode())) {
                    updateRegions.add(region);
                } else {
                    saveRegions.add(region);
                }
                regionCodes.add(region.getRegionCode());
            });

            updateRegions.forEach(region -> {
                regionService.update(new LambdaUpdateWrapper<Region>()
                        .set(Region::getEndpoint, region.getEndpoint())
                        .set(Region::getRegionName, region.getRegionName())
                        .set(Region::getDeleted, false)
                        .eq(Region::getRegionCode, region.getRegionCode())
                );
            });
            saveRegions.forEach(regionService::save);

            dbRegionCodes.removeAll(regionCodes);
            if (!dbRegionCodes.isEmpty()) {
                regionService.update(new LambdaUpdateWrapper<Region>()
                        .set(Region::getDeleted, true)
                        .in(Region::getRegionCode, regionCodes));
            }
        });
    }

    @XiaoChenJob("syncVpcInstanceJob")
    public void syncVpcInstanceJob() {
        Arrays.stream(CloudProviderEnum.getAllEnums()).parallel().forEach(cloudProviderEnum -> {
            List<String> dbInstanceIds = vpcInstanceService.listObjs(new QueryWrapper<VpcInstance>()
                    .lambda().eq(VpcInstance::getProvider, cloudProviderEnum.getValue())
                    .select(VpcInstance::getInstanceId), Object::toString);
            List<VpcInstance> instances = vpcService.listAllVpcInstances(cloudProviderEnum.getValue());
            List<String> instanceIds = new ArrayList<>();
            List<VpcInstance> updateInstances = new ArrayList<>();
            List<VpcInstance> saveInstances = new ArrayList<>();
            instances.stream().parallel().forEach(instance -> {
                if (dbInstanceIds.contains(instance.getInstanceId())) {
                    updateInstances.add(instance);
                } else {
                    saveInstances.add(instance);
                }
                instanceIds.add(instance.getInstanceId());
            });

            updateInstances.forEach(instance -> {
                vpcInstanceService.update(new LambdaUpdateWrapper<VpcInstance>()
                        .set(VpcInstance::getInstanceName, instance.getInstanceName())
                        .set(VpcInstance::getStatus, instance.getStatus())
                        .set(VpcInstance::getCidrBlock, instance.getCidrBlock())
                        .set(VpcInstance::getIpv6CidrBlock, instance.getIpv6CidrBlock())
                        .set(VpcInstance::getDetail, instance.getDetail())
                        .eq(VpcInstance::getInstanceId, instance.getInstanceId())
                );
            });

            saveInstances.forEach(vpcInstanceService::save);

            dbInstanceIds.removeAll(instanceIds);
            if (!dbInstanceIds.isEmpty()) {
                List<String> deleteInstances = new ArrayList<>();
                List<VpcInstance> finalUpdateInstances = new ArrayList<>();
                dbInstanceIds.stream().parallel().forEach(instanceId -> {
                    VpcInstance vpcInstance = this.vpcInstanceService.getOne(new LambdaQueryWrapper<VpcInstance>().eq(VpcInstance::getInstanceId, instanceId));
                    if (vpcInstance != null) {
                        VpcInstance providerInstance = vpcService.describeInstance(vpcInstance.getProvider(), vpcInstance.getRegion(), vpcInstance.getInstanceId());
                        if (providerInstance == null) {
                            deleteInstances.add(instanceId);
                        } else {
                            finalUpdateInstances.add(providerInstance);
                        }
                    }
                });

                deleteInstances.forEach(instanceId ->{
                    this.vpcInstanceService.update(new LambdaUpdateWrapper<VpcInstance>().set(VpcInstance::getDeleted, true)
                            .eq(VpcInstance::getInstanceId, instanceId));
                });
                finalUpdateInstances.forEach(vpcInstance ->{
                    vpcInstanceService.update(new LambdaUpdateWrapper<VpcInstance>()
                            .set(VpcInstance::getInstanceName, vpcInstance.getInstanceName())
                            .set(VpcInstance::getStatus, vpcInstance.getStatus())
                            .set(VpcInstance::getCidrBlock, vpcInstance.getCidrBlock())
                            .set(VpcInstance::getIpv6CidrBlock, vpcInstance.getIpv6CidrBlock())
                            .set(VpcInstance::getDetail, vpcInstance.getDetail())
                            .eq(VpcInstance::getInstanceId, vpcInstance.getInstanceId()));
                });
            }
        });
    }
}
