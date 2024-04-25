package com.zvz09.xiaochen.mc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VSwitcheDTO {

    private String vSwitchId;
    private String vSwitchName;
    private String zoneId;
    private String cidrBlock;
    private String ipv6CidrBlock;
    private Long availableIpAddressCount;

}
