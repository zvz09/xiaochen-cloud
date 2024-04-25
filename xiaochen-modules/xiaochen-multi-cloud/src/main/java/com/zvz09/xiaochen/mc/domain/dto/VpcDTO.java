package com.zvz09.xiaochen.mc.domain.dto;

import lombok.Data;

@Data
public class VpcDTO {
    private String region;
    private String vpcName;
    private String cidrBlock;
    private String ipv6CidrBlock;
    private boolean enableIpv6;
}
