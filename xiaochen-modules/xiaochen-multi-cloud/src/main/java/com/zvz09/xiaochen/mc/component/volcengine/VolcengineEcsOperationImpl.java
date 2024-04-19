package com.zvz09.xiaochen.mc.component.volcengine;

import com.zvz09.xiaochen.mc.component.EcsOperation;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolcengineEcsOperationImpl implements EcsOperation {

    private final VolcengineClient volcengineClient;

    @Override
    public List<EcsInstance> listEcsInstances() {
        return List.of();
    }
}
