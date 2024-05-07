package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.AttachKeyPairRequest;
import com.volcengine.ecs.model.CreateKeyPairRequest;
import com.volcengine.ecs.model.CreateKeyPairResponse;
import com.volcengine.ecs.model.DeleteKeyPairsRequest;
import com.volcengine.ecs.model.DescribeKeyPairsRequest;
import com.volcengine.ecs.model.DescribeKeyPairsResponse;
import com.volcengine.ecs.model.DetachKeyPairRequest;
import com.volcengine.ecs.model.ImportKeyPairRequest;
import com.volcengine.ecs.model.ModifyKeyPairAttributeRequest;
import com.zvz09.xiaochen.mc.component.ApplicationContextProvider;
import com.zvz09.xiaochen.mc.component.MPage;
import com.zvz09.xiaochen.mc.component.product.ecs.AbstractKeyPairOperation;
import com.zvz09.xiaochen.mc.domain.dto.KeyPair;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class VolcengineKeyPairOperation extends AbstractKeyPairOperation implements VolcengineBaseOperation {

    private VolcengineClient client;

    public VolcengineKeyPairOperation(String region) {
        super(region);
        this.client = ApplicationContextProvider.getApplicationContext().getBean(VolcengineClient.class);
    }

    @Override
    public KeyPair createKeyPair(KeyPair keyPair) {
        CreateKeyPairResponse resp = (CreateKeyPairResponse) this.client.handleClient((client)->{
            CreateKeyPairRequest request = super.converter.convertM2P(keyPair,new CreateKeyPairRequest());
            EcsApi api = new EcsApi(client);
            return api.createKeyPair(request);
        },super.region);
        return super.converter.convertP2M(resp,keyPair);
    }

    @Override
    public void attachKeyPair(KeyPair keyPair, String... instanceIds) {
        this.client.handleClient((client)->{
            AttachKeyPairRequest request = new AttachKeyPairRequest();
            request.setInstanceIds(Arrays.asList(instanceIds));
            request.setKeyPairName(keyPair.getKeyPairName());
            EcsApi api = new EcsApi(client);
            return  api.attachKeyPair(request);
        },super.region);
    }

    @Override
    public void detachKeyPair(KeyPair keyPair, String... instanceIds) {
        this.client.handleClient((client)->{
            DetachKeyPairRequest request = new DetachKeyPairRequest();
            request.setInstanceIds(Arrays.asList(instanceIds));
            request.setKeyPairName(keyPair.getKeyPairName());
            EcsApi api = new EcsApi(client);
            return  api.detachKeyPair(request);
        },super.region);
    }

    @Override
    public KeyPair importKeyPair(KeyPair keyPair) {
        CreateKeyPairResponse resp = (CreateKeyPairResponse) this.client.handleClient((client)->{
            ImportKeyPairRequest request = super.converter.convertM2P(keyPair,new ImportKeyPairRequest());
            EcsApi api = new EcsApi(client);
            return api.importKeyPair(request);
        },super.region);
        return super.converter.convertP2M(resp,keyPair);
    }

    @Override
    public void modifyKeyPairAttribute(KeyPair keyPair) {
        this.client.handleClient((client)->{
            ModifyKeyPairAttributeRequest request = super.converter.convertM2P(keyPair,new ModifyKeyPairAttributeRequest());
            EcsApi api = new EcsApi(client);
            return  api.modifyKeyPairAttribute(request);
        },super.region);
    }

    @Override
    public MPage<KeyPair> describeKeyPairs(KeyPair keyPair, QueryParameter queryParameter) {
        DescribeKeyPairsResponse resp = (DescribeKeyPairsResponse)this.client.handleClient((client)->{
            DescribeKeyPairsRequest request = new DescribeKeyPairsRequest();
            request.setKeyPairName(StringUtils.isNotBlank(keyPair.getKeyPairName())?keyPair.getKeyPairName():null);
            request.setMaxResults(queryParameter.getPageSize());
            request.setNextToken(queryParameter.getNextToken());
            EcsApi api = new EcsApi(client);
            return  api.describeKeyPairs(request);
        },super.region);

        MPage<KeyPair> page = new MPage<>(queryParameter.getPageNumber(),queryParameter.getPageSize());
        page.setNextToken(resp.getNextToken());
        page.setRecords(resp.getKeyPairs().stream().map((k)-> super.converter.convertP2M(k,new KeyPair())).toList());
        return page;
    }

    @Override
    public void deleteKeyPairs(KeyPair... keyPairs) {

        this.client.handleClient((client)->{
            DeleteKeyPairsRequest request = new DeleteKeyPairsRequest();
            request.setKeyPairNames(Arrays.stream(keyPairs).map(KeyPair::getKeyPairName).toList());
            EcsApi api = new EcsApi(client);
            return  api.deleteKeyPairs(request);
        },super.region);
    }


}
