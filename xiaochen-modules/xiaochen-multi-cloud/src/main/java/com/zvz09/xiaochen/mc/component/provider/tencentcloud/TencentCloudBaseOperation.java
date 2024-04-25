package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TencentCloudBaseOperation implements BaseProductService {

    protected final TencentCloudClient tencentCloudClient;

    public CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.TENCENT_CLOUD;
    };
}
