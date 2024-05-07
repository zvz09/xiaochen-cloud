package com.zvz09.xiaochen.mc.component.product.ecs;

import com.zvz09.xiaochen.mc.annotation.Converter;
import com.zvz09.xiaochen.mc.component.MPage;
import com.zvz09.xiaochen.mc.component.provider.BaseProductService;
import com.zvz09.xiaochen.mc.domain.dto.KeyPair;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

public abstract class AbstractKeyPairOperation implements BaseProductService {


    protected final String region;

    protected final Converter converter;

    protected AbstractKeyPairOperation(String region) {
        this.region = region;
        this.converter = new Converter(this.getProviderCode());
    }

    public ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };

    /**
     * 创建密钥对
     * @param keyPair
     * @return
     */
    public abstract KeyPair createKeyPair(KeyPair keyPair);

    /**
     * 绑定密钥对
     * @param keyPair
     * @param instanceIds
     */
    public abstract void  attachKeyPair(KeyPair keyPair,String... instanceIds);

    /**
     * 解绑密钥对
     * @param keyPair
     * @param instanceId
     */
    public abstract void  detachKeyPair(KeyPair keyPair,String... instanceIds);

    /**
     * 导入密钥对
     * @param keyPair
     * @return
     */
    public abstract KeyPair importKeyPair(KeyPair keyPair);

    /**
     * 修改密钥对信息
     * @param keyPair
     */
    public abstract void modifyKeyPairAttribute(KeyPair keyPair);

    /**
     * 查询密钥对列表
     * @param keyPair
     * @param queryParameter
     * @return
     */
    public abstract MPage<KeyPair> describeKeyPairs(KeyPair keyPair, QueryParameter queryParameter);

    /**
     * 删除密钥对
     * @param keyPairs
     */
    public abstract void deleteKeyPairs(KeyPair... keyPairs);
}
