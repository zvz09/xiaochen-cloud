package com.zvz09.xiaochen.mc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * @TableName mc_vpc_instance
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value ="mc_vpc_instance")
public class VpcInstance extends BaseEntity {

    /**
     * 云厂商
     */
    private String provider;

    /**
     * 实例id
     */
    private String instanceId;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 状态
     */
    private String status;

    /**
     * 地域
     */
    private String region;

    /**
     * VPC 的 IPv6 网段
     */
    private String ipv6CidrBlock;

    /**
     * VPC 的 IPv4 网段
     */
    private String cidrBlock;

    /**
     * 详情
     */
    private String detail;

}
