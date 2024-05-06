package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
import lombok.Data;

@Data
public class VpcDTO {

    @FieldMapping(aliyun = @BaseFieldMapping(value = "regionId"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "cidrBlock")
    )
    private String region;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcName"),
            tencentcloud = @BaseFieldMapping(value = "VpcName"),
            volcengine = @BaseFieldMapping(value = "vpcName")
    )
    private String vpcName;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "cidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "CidrBlock"),
            volcengine = @BaseFieldMapping(value = "cidrBlock")
    )
    private String cidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "ipv6CidrBlock"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "ipv6CidrBlock")
    )
    private String ipv6CidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "enableIpv6"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "enableIpv6")
    )
    private Boolean enableIpv6;
}
