package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

public interface TencentCloudBaseOperation extends BaseProductService {
    default CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.TENCENT_CLOUD;
    };
}
