package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

public interface AliYunBaseOperation extends BaseProductService {

    default CloudProviderEnum getProviderCode(){
        return CloudProviderEnum.ALI_YUN;
    }

}
