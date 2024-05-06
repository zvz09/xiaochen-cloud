package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
import lombok.Data;

@Data
public class CreateVSwitch {
    @FieldMapping(aliyun = @BaseFieldMapping(value = "cidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "CidrBlock"),
            volcengine = @BaseFieldMapping(value = "cidrBlock")
    )
    private String cidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "ipv6CidrBlock"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "ipv6CidrBlock")
    )
    private Integer ipv6CidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "regionId"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "")
    )
    private String regionId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vSwitchName"),
            tencentcloud = @BaseFieldMapping(value = "SubnetName"),
            volcengine = @BaseFieldMapping(value = "subnetName")
    )
    private String vSwitchName;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcId"),
            tencentcloud = @BaseFieldMapping(value = "VpcId"),
            volcengine = @BaseFieldMapping(value = "vpcId")
    )
    private String vpcId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcIpv6CidrBlock"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "")
    )
    private String vpcIpv6CidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "zoneId"),
            tencentcloud = @BaseFieldMapping(value = "Zone"),
            volcengine = @BaseFieldMapping(value = "zoneId")
    )
    private String zoneId;
}
