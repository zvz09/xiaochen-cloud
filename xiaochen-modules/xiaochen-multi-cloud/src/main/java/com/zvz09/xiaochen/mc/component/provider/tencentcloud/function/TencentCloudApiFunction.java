package com.zvz09.xiaochen.mc.component.provider.tencentcloud.function;

import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

@FunctionalInterface
public interface TencentCloudApiFunction<AbstractClient> {
    AbstractModel apply(AbstractClient client) throws TencentCloudSDKException;
}
