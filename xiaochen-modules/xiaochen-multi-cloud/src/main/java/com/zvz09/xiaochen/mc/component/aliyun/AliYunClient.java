package com.zvz09.xiaochen.mc.component.aliyun;

import com.aliyun.core.utils.SdkAutoCloseable;
import com.aliyun.sdk.gateway.pop.models.Response;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.component.AbstractProviderClient;
import com.zvz09.xiaochen.mc.component.aliyun.function.AliYunApiFunction;
import com.zvz09.xiaochen.mc.component.aliyun.util.AliyunClientUtil;
import com.zvz09.xiaochen.mc.domain.entity.Account;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@Service
public class AliYunClient extends AbstractProviderClient<CompletableFuture<Response>, AliYunApiFunction<SdkAutoCloseable>> {

    public AliYunClient(AccountMapper accountMapper) {
        super(accountMapper);
    }

    @Override
    public CloudProviderEnum provider() {
        return CloudProviderEnum.ALI_YUN;
    }

    @Override
    public CompletableFuture<Response> handleClient(AliYunApiFunction<SdkAutoCloseable> action, String region, ProductEnum productEnum) {
        switch (productEnum) {
            case ECS:
                return ecsHandleClient(action, region);
            case OSS:
                throw new BusinessException(ProductEnum.OSS.name()+"产品暂不支持");
            default:
                throw new BusinessException("产品暂不支持");
        }
    }

    @Override
    public CompletableFuture<Response> handleClient(AliYunApiFunction<SdkAutoCloseable> action, String region) {
        throw new BusinessException("暂不支持");
    }

    public CompletableFuture<Response> ecsHandleClient(AliYunApiFunction<SdkAutoCloseable> action, String region) {
        Account account = super.getProviderAccount();
        try (com.aliyun.sdk.service.ecs20140526.AsyncClient client = AliyunClientUtil.createEcsClient(account.getAccessKey(), account.getSecretKey(), region))  {
            return action.apply(client);
        } catch (InterruptedException ie) {
            log.warn("触发强制中断：", ie);
            Thread.currentThread().interrupt();
        }catch (ExecutionException ee){
            log.warn("执行异常：", ee);
        }catch (Exception e) {
            log.warn("阿里云调用异常", e);
            throw new BusinessException("阿里云调用异常");
        }
        return null;
    }

}
