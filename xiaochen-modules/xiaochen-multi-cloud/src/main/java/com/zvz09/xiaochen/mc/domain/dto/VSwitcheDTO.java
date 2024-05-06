package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VSwitcheDTO {

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vSwitchId"),
            tencentcloud = @BaseFieldMapping(value = "SubnetId"),
            volcengine = @BaseFieldMapping(value = "subnetId")
    )
    private String vSwitchId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vSwitchName"),
            tencentcloud = @BaseFieldMapping(value = "SubnetName"),
            volcengine = @BaseFieldMapping(value = "subnetName")
    )
    private String vSwitchName;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "zoneId"),
            tencentcloud = @BaseFieldMapping(value = "Zone"),
            volcengine = @BaseFieldMapping(value = "zoneId")
    )
    private String zoneId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "cidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "CidrBlock"),
            volcengine = @BaseFieldMapping(value = "cidrBlock")
    )
    private String cidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "ipv6CidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "Ipv6CidrBlock"),
            volcengine = @BaseFieldMapping(value = "ipv6CidrBlock")
    )
    private String ipv6CidrBlock;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "availableIpAddressCount"),
            tencentcloud = @BaseFieldMapping(value = "AvailableIpAddressCount"),
            volcengine = @BaseFieldMapping(value = "availableIpAddressCount")
    )
    private Long availableIpAddressCount;

}
