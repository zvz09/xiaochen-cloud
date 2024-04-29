package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

public interface VolcengineBaseOperation extends BaseProductService {
    default CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.VOLCENGINE;
    };
}
