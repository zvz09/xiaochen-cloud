package com.zvz09.xiaochen.mc.domain.dto;

import lombok.Data;

@Data
public class CreateVSwitch {
    private String cidrBlock;
    private Integer ipv6CidrBlock;
    private String regionId;
    private String vSwitchName;
    private String vpcId;
    private String vpcIpv6CidrBlock;
    private String zoneId;
}
