package com.zvz09.xiaochen.mc.component.volcengine;

import com.volcengine.ApiClient;
import com.volcengine.ApiException;
import com.volcengine.model.AbstractResponse;
import com.volcengine.sign.Credentials;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.component.AbstractProviderClient;
import com.zvz09.xiaochen.mc.component.volcengine.function.VolcengineApiFunction;
import com.zvz09.xiaochen.mc.domain.entity.Account;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class VolcengineClient extends AbstractProviderClient<AbstractResponse, VolcengineApiFunction<ApiClient>> {

    private static final String DEFAULT_REGION = "cn-beijing";


    public VolcengineClient(AccountMapper accountMapper) {
        super(accountMapper);
    }

    @Override
    public CloudProviderEnum provider() {
        return CloudProviderEnum.VOLCENGINE;
    }

    @Override
    public AbstractResponse handleClient(VolcengineApiFunction<ApiClient> action, String region, ProductEnum productEnum) {
        return this.handleClient(action,region);
    }

    @Override
    public AbstractResponse handleClient(VolcengineApiFunction<ApiClient> action, String region) {
        try {
            if(StringUtils.isBlank(region)){
                region = DEFAULT_REGION;
            }
            Account account = super.getProviderAccount();
            Credentials credential = this.getCredential(account);
            ApiClient apiClient = new ApiClient()
                    .setCredentials(credential)
                    .setRegion(region);
            AbstractResponse abstractResponse = action.apply(apiClient);
            log.info("success response:{}",abstractResponse);
            return abstractResponse;
        } catch (ApiException e) {
            log.error("ApiException error",e);
            throw new BusinessException("业务异常");
        }catch (Exception e){
            log.error("error",e);
            throw new BusinessException("业务异常");
        }
    }

    Credentials getCredential(Account account) {
        String accessKey = account.getAccessKey();
        String secretKey = account.getSecretKey();
        if (accessKey == null || secretKey == null) {
            throw new RuntimeException("未配置有效的密钥！");
        }
        return Credentials.getCredentials(accessKey, secretKey);
    }
}
