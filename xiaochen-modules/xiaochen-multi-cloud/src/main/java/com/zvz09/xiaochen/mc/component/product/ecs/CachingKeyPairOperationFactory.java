package com.zvz09.xiaochen.mc.component.product.ecs;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.component.provider.aliyun.AliYunKeyPairOperation;
import com.zvz09.xiaochen.mc.component.provider.tencentcloud.TencentCloudKeyPairOperation;
import com.zvz09.xiaochen.mc.component.provider.volcengine.VolcengineKeyPairOperation;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CachingKeyPairOperationFactory implements KeyPairOperationFactory{

    private Map<String, AbstractKeyPairOperation> cache = new HashMap<>();

    @Override
    public AbstractKeyPairOperation createKeyPairOperation(CloudProviderEnum cloudProviderEnum, String region) {

        String key = String.format("%s-%s", cloudProviderEnum.getValue(), region);

        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            AbstractKeyPairOperation keyPairOperationImpl = createKeyPairOperationImpl(cloudProviderEnum,region);
            if(keyPairOperationImpl == null){
                throw new BusinessException(String.format("云服务商[%s]不支持", cloudProviderEnum.getValue()));
            }
            cache.put(key, keyPairOperationImpl);
            return keyPairOperationImpl;
        }
    }

    private AbstractKeyPairOperation createKeyPairOperationImpl(CloudProviderEnum cloudProviderEnum, String region) {
        switch (cloudProviderEnum){
            case ALI_YUN -> {
                return new AliYunKeyPairOperation(region);
            }
            case TENCENT_CLOUD -> {
                return new TencentCloudKeyPairOperation(region);
            }
            case VOLCENGINE -> {
                return new VolcengineKeyPairOperation(region);
            }
        }
        return null;
    }

}
