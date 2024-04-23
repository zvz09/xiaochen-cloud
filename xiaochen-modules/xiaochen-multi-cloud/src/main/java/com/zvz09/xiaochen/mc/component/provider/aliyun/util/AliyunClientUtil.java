package com.zvz09.xiaochen.mc.component.provider.aliyun.util;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import darabonba.core.client.ClientOverrideConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AliyunClientUtil {


    private static final String DEFAULT_REGION = "cn-hangzhou";

    private static final String DEFAULT_Endpoint = "ecs.aliyuncs.com";

    private static Map<String, String> ecsEndpointMap = new HashMap<>();
    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.sdk.service.ecs20140526.AsyncClient createEcsClient(String accessKeyId, String accessKeySecret, String region) throws Exception {
        String endpoint;
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
            endpoint = DEFAULT_Endpoint;
        }else {
            endpoint = ecsEndpointMap.get(region);
        }

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


    public static void setEcsEndpointMap(Map<String, String> ecsEndpointMap) {
        AliyunClientUtil.ecsEndpointMap = ecsEndpointMap;
    }
}
