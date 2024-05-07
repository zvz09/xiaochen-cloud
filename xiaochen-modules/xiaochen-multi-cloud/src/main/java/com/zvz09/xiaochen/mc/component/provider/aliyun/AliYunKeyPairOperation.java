package com.zvz09.xiaochen.mc.component.provider.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.AttachKeyPairRequest;
import com.aliyun.sdk.service.ecs20140526.models.CreateKeyPairRequest;
import com.aliyun.sdk.service.ecs20140526.models.CreateKeyPairResponse;
import com.aliyun.sdk.service.ecs20140526.models.DeleteKeyPairsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeKeyPairsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeKeyPairsResponse;
import com.aliyun.sdk.service.ecs20140526.models.DetachKeyPairRequest;
import com.aliyun.sdk.service.ecs20140526.models.ImportKeyPairRequest;
import com.aliyun.sdk.service.ecs20140526.models.ImportKeyPairResponse;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.component.ApplicationContextProvider;
import com.zvz09.xiaochen.mc.component.MPage;
import com.zvz09.xiaochen.mc.component.product.ecs.AbstractKeyPairOperation;
import com.zvz09.xiaochen.mc.domain.dto.KeyPair;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AliYunKeyPairOperation extends AbstractKeyPairOperation implements AliYunBaseOperation {

    private AliYunClient client;
    public AliYunKeyPairOperation(String region) {
        super(region);
        this.client = ApplicationContextProvider.getApplicationContext().getBean(AliYunClient.class);
    }

    @Override
    public KeyPair createKeyPair(KeyPair keyPair) {
        CreateKeyPairResponse resp = (CreateKeyPairResponse)client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            CreateKeyPairRequest request = super.converter.convertM2P(keyPair, CreateKeyPairRequest.builder().regionId(this.region).build());

            return asyncClient.createKeyPair(request).get();
        }, region, this.getProductCode());

        return super.converter.convertP2M(resp.getBody(), keyPair);
    }

    @Override
    public void attachKeyPair(KeyPair keyPair, String... instanceIds) {
        client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            AttachKeyPairRequest request = AttachKeyPairRequest.builder()
                    .regionId(this.region)
                    .keyPairName(keyPair.getKeyPairName())
                    .instanceIds(JSON.toJSONString(instanceIds))
                    .build();
            return asyncClient.attachKeyPair(request).get();
        }, region, this.getProductCode());
    }

    @Override
    public void detachKeyPair(KeyPair keyPair, String... instanceIds) {
        client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            DetachKeyPairRequest request = DetachKeyPairRequest.builder()
                    .regionId(this.region)
                    .keyPairName(keyPair.getKeyPairName())
                    .instanceIds(JSON.toJSONString(instanceIds))
                    .build();
            return asyncClient.detachKeyPair(request).get();
        }, region, this.getProductCode());
    }

    @Override
    public KeyPair importKeyPair(KeyPair keyPair) {
        ImportKeyPairResponse resp = (ImportKeyPairResponse)client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;
            ImportKeyPairRequest request =
                    super.converter.convertM2P(keyPair,ImportKeyPairRequest.builder().regionId(this.region).build());

            return asyncClient.importKeyPair(request).get();
        }, region, this.getProductCode());
        return super.converter.convertP2M(resp.getBody(), keyPair);
    }

    @Override
    public void modifyKeyPairAttribute(KeyPair keyPair) {
        throw new BusinessException("Not support");
    }

    @Override
    public MPage<KeyPair> describeKeyPairs(KeyPair keyPair, QueryParameter queryParameter) {
        DescribeKeyPairsResponse resp = (DescribeKeyPairsResponse) client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;

            DescribeKeyPairsRequest request = DescribeKeyPairsRequest.builder()
                    .regionId(this.region)
                    .pageNumber(queryParameter.getPageNumber())
                    .pageSize(queryParameter.getPageSize())
                    .keyPairName(StringUtils.isNotBlank(keyPair.getKeyPairName())?String.format("*%s*", keyPair.getKeyPairName()):null)
                    .build();

            return asyncClient.describeKeyPairs(request).get();
        }, region, this.getProductCode());

        MPage<KeyPair> page = new MPage<>(queryParameter.getPageNumber(),queryParameter.getPageSize());
        page.setCurrent(resp.getBody().getTotalCount());
        page.setRecords(resp.getBody().getKeyPairs().getKeyPair().stream().map((k)-> super.converter.convertP2M(k,new KeyPair())).toList());
        return page;
    }

    @Override
    public void deleteKeyPairs(KeyPair... keyPairs) {
        client.handleClient((client) -> {
            AsyncClient asyncClient = (AsyncClient) client;

            DeleteKeyPairsRequest request = DeleteKeyPairsRequest.builder()
                    .regionId(this.region)
                    .keyPairNames(JSON.toJSONString(Arrays.stream(keyPairs).map(KeyPair::getKeyPairName).toArray()))
                    .build();

            return asyncClient.deleteKeyPairs(request).get();
        }, region, this.getProductCode());
    }
}
