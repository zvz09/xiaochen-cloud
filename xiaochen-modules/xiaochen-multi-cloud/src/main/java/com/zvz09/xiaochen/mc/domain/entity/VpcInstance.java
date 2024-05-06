package com.zvz09.xiaochen.mc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
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
    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcId"),
            tencentcloud = @BaseFieldMapping(value = "VpcId"),
            volcengine = @BaseFieldMapping(value = "vpcId")
    )
    private String instanceId;

    /**
     * 实例名称
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcName"),
            tencentcloud = @BaseFieldMapping(value = "VpcName"),
            volcengine = @BaseFieldMapping(value = "vpcName")
    )
    private String instanceName;

    /**
     * 状态
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "status"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "status")
    )
    private String status;

    /**
     * 地域
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "regionId"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "")
    )
    private String region;

    /**
     * VPC 的 IPv6 网段
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "ipv6CidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "Ipv6CidrBlock"),
            volcengine = @BaseFieldMapping(value = "")
    )
    private String ipv6CidrBlock;

    /**
     * VPC 的 IPv4 网段
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "cidrBlock"),
            tencentcloud = @BaseFieldMapping(value = "CidrBlock"),
            volcengine = @BaseFieldMapping(value = "cidrBlock")
    )
    private String cidrBlock;

    /**
     * 详情
     */
    private String detail;

}
