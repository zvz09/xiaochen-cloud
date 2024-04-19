package com.zvz09.xiaochen.mc.component.aliyun.util;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import darabonba.core.TeaPair;
import darabonba.core.client.ClientOverrideConfiguration;
import darabonba.core.utils.CommonUtil;

import java.util.Map;

public class AliyunClientUtil {

    private static final Map<String, String> ecsEndpointMap = CommonUtil.buildMap(new TeaPair[]{new TeaPair("cn-hangzhou", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shanghai-finance-1", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shenzhen-finance-1", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-north-2-gov-1", "ecs.aliyuncs.com"), new TeaPair("ap-northeast-2-pop", "ecs.aliyuncs.com"), new TeaPair("cn-beijing-finance-pop", "ecs.aliyuncs.com"), new TeaPair("cn-beijing-gov-1", "ecs.aliyuncs.com"), new TeaPair("cn-beijing-nu16-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-edge-1", "ecs.cn-qingdao-nebula.aliyuncs.com"), new TeaPair("cn-fujian", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-haidian-cm12-c01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-bj-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-finance", "ecs.aliyuncs.com"), new TeaPair("cn-hangzhou-internal-prod-1", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-internal-test-1", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-internal-test-2", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-internal-test-3", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hangzhou-test-306", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-hongkong-finance-pop", "ecs.aliyuncs.com"), new TeaPair("cn-huhehaote-nebula-1", "ecs.cn-qingdao-nebula.aliyuncs.com"), new TeaPair("cn-shanghai-et15-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shanghai-et2-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shanghai-inner", "ecs.aliyuncs.com"), new TeaPair("cn-shanghai-internal-test-1", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shenzhen-inner", "ecs.aliyuncs.com"), new TeaPair("cn-shenzhen-st4-d01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-shenzhen-su18-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-wuhan", "ecs.aliyuncs.com"), new TeaPair("cn-yushanfang", "ecs.aliyuncs.com"), new TeaPair("cn-zhangbei", "ecs.aliyuncs.com"), new TeaPair("cn-zhangbei-na61-b01", "ecs-cn-hangzhou.aliyuncs.com"), new TeaPair("cn-zhangjiakou-na62-a01", "ecs.cn-zhangjiakou.aliyuncs.com"), new TeaPair("cn-zhengzhou-nebula-1", "ecs.cn-qingdao-nebula.aliyuncs.com"), new TeaPair("eu-west-1-oxs", "ecs.cn-shenzhen-cloudstone.aliyuncs.com"), new TeaPair("rus-west-1-pop", "ecs.aliyuncs.com")});

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.sdk.service.ecs20140526.AsyncClient createEcsClient(String accessKeyId, String accessKeySecret, String region) throws Exception {
        String endpoint = ecsEndpointMap.get(region);
        if (endpoint == null) {
           throw new BusinessException("当前区域暂不支持");
        }
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());


        return com.aliyun.sdk.service.ecs20140526.AsyncClient.builder()
                .region(region) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                )
                .build();
    }


}
