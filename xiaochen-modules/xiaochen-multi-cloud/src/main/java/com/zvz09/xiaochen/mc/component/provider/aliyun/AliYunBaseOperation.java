package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AliYunBaseOperation implements BaseProductService {

    protected final AliYunClient aliYunClient;
    public CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.ALI_YUN;
    }
}
