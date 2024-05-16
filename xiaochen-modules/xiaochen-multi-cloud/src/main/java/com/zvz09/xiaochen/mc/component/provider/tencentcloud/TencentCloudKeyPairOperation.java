package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.AssociateInstancesKeyPairsRequest;
import com.tencentcloudapi.cvm.v20170312.models.CreateKeyPairRequest;
import com.tencentcloudapi.cvm.v20170312.models.CreateKeyPairResponse;
import com.tencentcloudapi.cvm.v20170312.models.DeleteKeyPairsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeKeyPairsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeKeyPairsResponse;
import com.tencentcloudapi.cvm.v20170312.models.DisassociateInstancesKeyPairsRequest;
import com.tencentcloudapi.cvm.v20170312.models.ImportKeyPairRequest;
import com.tencentcloudapi.cvm.v20170312.models.ImportKeyPairResponse;
import com.tencentcloudapi.cvm.v20170312.models.ModifyKeyPairAttributeRequest;
import com.zvz09.xiaochen.mc.component.ApplicationContextProvider;
import com.zvz09.xiaochen.mc.component.MPage;
import com.zvz09.xiaochen.mc.component.product.ecs.AbstractKeyPairOperation;
import com.zvz09.xiaochen.mc.domain.dto.KeyPair;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;

import java.util.Arrays;

public class TencentCloudKeyPairOperation extends AbstractKeyPairOperation implements TencentCloudBaseOperation {

    private final TencentCloudClient client;

    public TencentCloudKeyPairOperation(String region) {
        super(region);
        this.client = ApplicationContextProvider.getApplicationContext().getBean(TencentCloudClient.class);
    }

    @Override
    public KeyPair createKeyPair(KeyPair keyPair) {
        CreateKeyPairResponse  resp = (CreateKeyPairResponse)client.handleClient((abstractClient)->{
            CreateKeyPairRequest req = super.converter.convertM2P(keyPair,new CreateKeyPairRequest());
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.CreateKeyPair(req);
        },region, this.getProductCode());
        return super.converter.convertP2M(resp.getKeyPair(),keyPair);
    }

    @Override
    public void attachKeyPair(KeyPair keyPair, String... instanceIds) {
        client.handleClient((abstractClient)->{

            AssociateInstancesKeyPairsRequest req = new AssociateInstancesKeyPairsRequest();
            req.setInstanceIds(instanceIds);
            req.setKeyIds(new String[]{keyPair.getKeyPairId()});

            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.AssociateInstancesKeyPairs(req);
        },region, this.getProductCode());
    }

    @Override
    public void detachKeyPair(KeyPair keyPair, String... instanceIds) {
        client.handleClient((abstractClient)->{

            DisassociateInstancesKeyPairsRequest req = new DisassociateInstancesKeyPairsRequest();
            req.setInstanceIds(instanceIds);
            req.setKeyIds(new String[]{keyPair.getKeyPairId()});

            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DisassociateInstancesKeyPairs(req);
        },region, this.getProductCode());
    }

    @Override
    public KeyPair importKeyPair(KeyPair keyPair) {

        ImportKeyPairResponse resp = (ImportKeyPairResponse)client.handleClient((abstractClient)->{

            ImportKeyPairRequest req = new ImportKeyPairRequest();
            req.setKeyName(keyPair.getKeyPairName());
            req.setPublicKey(keyPair.getPublicKey());

            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.ImportKeyPair(req);
        },region, this.getProductCode());

        return super.converter.convertP2M(resp,keyPair);
    }

    @Override
    public void modifyKeyPairAttribute(KeyPair keyPair) {
        client.handleClient((abstractClient)->{
            ModifyKeyPairAttributeRequest req = super.converter.convertM2P(keyPair,new ModifyKeyPairAttributeRequest()) ;
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.ModifyKeyPairAttribute(req);
        },region, this.getProductCode());
    }

    @Override
    public MPage<KeyPair> describeKeyPairs(KeyPair keyPair, QueryParameter queryParameter) {
        DescribeKeyPairsResponse resp = (DescribeKeyPairsResponse )client.handleClient((abstractClient)->{
            DescribeKeyPairsRequest req = new DescribeKeyPairsRequest();
            req.setOffset(Long.valueOf(queryParameter.getOffset()));
            req.setLimit(Long.valueOf(queryParameter.getPageSize()));

            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeKeyPairs(req);
        },region, this.getProductCode());

        MPage<KeyPair> page = new MPage<>(queryParameter.getPageNumber(),queryParameter.getPageSize());
        page.setTotal(resp.getTotalCount());
        page.setRecords(Arrays.stream(resp.getKeyPairSet()).map((k)-> super.converter.convertP2M(k,new KeyPair())).toList());
        return page;
    }

    @Override
    public void deleteKeyPairs(KeyPair... keyPairs) {
        client.handleClient((abstractClient)->{
            DeleteKeyPairsRequest req = new DeleteKeyPairsRequest();
            req.setKeyIds(Arrays.stream(keyPairs).map(KeyPair::getKeyPairId).toArray(String[]::new));
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DeleteKeyPairs(req);
        },region, this.getProductCode());
    }


}
