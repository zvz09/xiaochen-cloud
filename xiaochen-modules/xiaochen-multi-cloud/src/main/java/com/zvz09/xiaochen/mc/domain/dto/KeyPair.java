package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
import lombok.Data;


@Data
public class KeyPair {

    @FieldMapping(aliyun = @BaseFieldMapping(value = ""),
            tencentcloud = @BaseFieldMapping(value = "Description"),
            volcengine = @BaseFieldMapping(value = "description"))
    private String description;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "keyPairName"),
            tencentcloud = @BaseFieldMapping(value = "KeyName"),
            volcengine = @BaseFieldMapping(value = "keyPairName"))
    private String keyPairName;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "KeyPairId"),
            tencentcloud = @BaseFieldMapping(value = "KeyId"),
            volcengine = @BaseFieldMapping(value = "keyPairId"))
    private String keyPairId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "publicKeyBody"),
            tencentcloud = @BaseFieldMapping(value = "PublicKey"),
            volcengine = @BaseFieldMapping(value = "publicKey"))
    private String publicKey;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "privateKeyBody"),
            tencentcloud = @BaseFieldMapping(value = "PrivateKey"),
            volcengine = @BaseFieldMapping(value = "privateKey"))
    private String privateKey;
}
