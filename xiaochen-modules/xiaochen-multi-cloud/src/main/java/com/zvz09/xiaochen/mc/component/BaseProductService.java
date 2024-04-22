package com.zvz09.xiaochen.mc.component;

import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

public interface BaseProductService {

    public CloudProviderEnum getProviderCode();

    public ProductEnum getProductCode();
}
