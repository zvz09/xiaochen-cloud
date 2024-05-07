package com.zvz09.xiaochen.mc.component.product.ecs;

import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;

public interface KeyPairOperationFactory {
    AbstractKeyPairOperation createKeyPairOperation(CloudProviderEnum cloudProviderEnum, String region);
}
