package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.tencentcloudapi.common.AbstractClient;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.component.provider.AbstractProviderClient;
import com.zvz09.xiaochen.mc.component.provider.tencentcloud.function.TencentCloudApiFunction;
import com.zvz09.xiaochen.mc.domain.entity.Account;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TencentCloudClient extends AbstractProviderClient<AbstractModel, TencentCloudApiFunction<AbstractClient>> {
    public TencentCloudClient(AccountMapper accountMapper) {
        super(accountMapper);
    }

    @Override
    public CloudProviderEnum provider() {
            return CloudProviderEnum.TENCENT_CLOUD;
        }

    @Override
    public AbstractModel handleClient(TencentCloudApiFunction<AbstractClient> action, String region, ProductEnum productEnum) {
        switch (productEnum) {
            case ECS:
                return ecsHandleClient(action, region);
            case OSS:
                throw new BusinessException(ProductEnum.OSS.name()+"产品暂不支持");
            default:
                throw new BusinessException("产品暂不支持");
        }
    }

    private AbstractModel ecsHandleClient(TencentCloudApiFunction<AbstractClient> action, String region) {
        try {
            Account account = super.getProviderAccount();
            Credential credential = this.getCredential(account);
            CvmClient cvmClient = new CvmClient(credential, region);
            AbstractModel abstractResponse = action.apply(cvmClient);
            log.info("success response:{}",abstractResponse);
            return abstractResponse;
        } catch (TencentCloudSDKException e) {
            log.error("ApiException error",e);
            throw new BusinessException("业务异常");
        }catch (Exception e){
            log.error("error",e);
            throw new BusinessException("业务异常");
        }
    }

    @Override
    public AbstractModel handleClient(TencentCloudApiFunction<AbstractClient> action, String region) {
       throw new BusinessException("暂不支持");
    }

    Credential getCredential(Account account) {
        String accessKey = account.getAccessKey();
        String secretKey = account.getSecretKey();
        if (accessKey == null || secretKey == null) {
            throw new RuntimeException("未配置有效的密钥！");
        }
        return new Credential(accessKey, secretKey);
    }
}
