package com.zvz09.xiaochen.mc.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import com.zvz09.xiaochen.mc.component.service.IEcsService;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.service.IEcsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncInstanceJob {

    private final IEcsInstanceService ecsInstanceService;
    private final IEcsService ecsService;

    @Transactional
    @XiaoChenJob("syncEcsInstanceJob")
    public void syncEcsInstanceJob() {
        Arrays.stream(CloudProviderEnum.getAllEnums()).parallel().forEach(cloudProviderEnum -> {
            List<String> dbInstanceIds = ecsInstanceService.listObjs(new QueryWrapper<EcsInstance>()
                    .lambda().eq(EcsInstance::getProvider,cloudProviderEnum.getValue())
                    .select(EcsInstance::getInstanceId), Object::toString);
            List<EcsInstance> instances = ecsService.listAllEcsInstances(cloudProviderEnum.getValue());
            List<String> instanceIds = new ArrayList<>();
            instances.stream().parallel().forEach(instance -> {
                if (dbInstanceIds.contains(instance.getInstanceId())) {
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
                }else {
                    ecsInstanceService.save(instance);
                }
                instanceIds.add(instance.getInstanceId());
            });

            dbInstanceIds.removeAll(instanceIds);
            if(!dbInstanceIds.isEmpty()){
                dbInstanceIds.stream().parallel().forEach(instanceId -> {
                    EcsInstance ecsInstance = this.ecsInstanceService.getOne(new LambdaQueryWrapper<EcsInstance>().eq(EcsInstance::getInstanceId,instanceId));
                    if(ecsInstance != null){
                        EcsInstance providerInstance = ecsService.describeInstance(ecsInstance.getProvider(), ecsInstance.getRegion(),ecsInstance.getInstanceId());
                        if(providerInstance == null){
                            this.ecsInstanceService.removeById(ecsInstance.getId());
                        }else {
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
                        }
                    }
                });
            }
        });

    }
}
