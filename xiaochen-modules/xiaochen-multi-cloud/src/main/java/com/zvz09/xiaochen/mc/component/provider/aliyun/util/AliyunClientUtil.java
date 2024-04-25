package com.zvz09.xiaochen.mc.component.provider.aliyun.util;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import darabonba.core.client.ClientOverrideConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AliyunClientUtil {


    private static final String DEFAULT_REGION = "cn-hangzhou";

    private static final String DEFAULT_Endpoint = "%s.%s.aliyuncs.com";

    private static final Map<ProductEnum, Map<String, String>> productEndpointMap = new HashMap<>();
    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.sdk.service.ecs20140526.AsyncClient createEcsClient(String accessKeyId, String accessKeySecret, String region) throws Exception {
        region = StringUtils.isBlank(region) ? DEFAULT_REGION : region;

        String endpoint = getEndpoint(ProductEnum.ECS, region);

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
    public static com.aliyun.sdk.service.vpc20160428.AsyncClient createVpcClient(String accessKeyId, String accessKeySecret, String region) throws Exception {
        region = StringUtils.isBlank(region) ? DEFAULT_REGION : region;
        String endpoint = getEndpoint(ProductEnum.VPC, region);

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());


        return com.aliyun.sdk.service.vpc20160428.AsyncClient.builder()
                .region(region) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                )
                .build();
    }

    private static String getEndpoint(ProductEnum productEnum, String region) {
        String endpoint;
        if(StringUtils.isBlank(region) || region.equals(DEFAULT_REGION)){
            endpoint = String.format(DEFAULT_Endpoint,productEnum.getValue(),DEFAULT_REGION);
        }else {
            endpoint = Objects.requireNonNull(productEndpointMap.get(productEnum)).get(region);
        }

        if (endpoint == null) {
            throw new BusinessException("当前区域暂不支持");
        }
        return endpoint;
    }

    public static void setEcsEndpointMap(Map<String, String> ecsEndpointMap) {
        productEndpointMap.put(ProductEnum.ECS, ecsEndpointMap);
        Map<String, String> vpcEndpointMap = new HashMap<>();
        ecsEndpointMap.keySet().forEach(region -> {
            vpcEndpointMap.put(region,String.format("%s.%s.aliyuncs.com",ProductEnum.VPC.getValue(),region));
        });
        productEndpointMap.put(ProductEnum.VPC, vpcEndpointMap);
    }
}
