package com.zvz09.xiaochen.mc.component.aliyun;

import com.zvz09.xiaochen.mc.component.EcsOperation;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AliYunEcsOperationImpl implements EcsOperation {

    private final AliYunClient aliYunClient;
    @Override
    public List<EcsInstance> listEcsInstances() {

        aliYunClient.handleClient((client)->{

            return null;
        },"", ProductEnum.ECS);

        return List.of();
    }
}
