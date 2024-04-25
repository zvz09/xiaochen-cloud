package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class VolcengineBaseOperation implements BaseProductService {

    protected final VolcengineClient volcengineClient;


    public CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.VOLCENGINE;
    };
}
