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
 * @TableName mc_ecs_instance
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value ="mc_ecs_instance")
public class EcsInstance extends BaseEntity {

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
     * 操作系统
     */
    private String osType;

    /**
     * 地域
     */
    private String region;

    /**
     * 实例规格
     */
    private String instanceSpec;

    /**
     * 公网IP
     */
    private String ipAddress;

    /**
     * 实例的计费方式
     */
    private String instanceChargeType;

    /**
     * 过期时间
     */
    private String expiredTime;

    /**
     * 详情
     */
    private String detail;

}
