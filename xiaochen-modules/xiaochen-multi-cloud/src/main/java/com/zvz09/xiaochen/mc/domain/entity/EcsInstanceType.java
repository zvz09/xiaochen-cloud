package com.zvz09.xiaochen.mc.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 云主机实例规格
 * mc_ecs_instance_type
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value ="mc_ecs_instance_type")
public class EcsInstanceType extends BaseEntity {
    /**
     * 云厂商
     */
    private String provider;

    /**
     * 区域
     */
    private String region;

    /**
     * 实例规格ID
     */
    private String typeId;

    /**
     * 实例规格族
     */
    private String typeFamily;

    /**
     * CPU数量
     */
    private Integer cpu;

    /**
     * CPU型号
     */
    private String cpuModel;

    /**
     * CPU主频，单位：GHz
     */
    private Float cpuBaseFrequency;

    /**
     * CPU睿频，单位：GHz
     */
    private Float cpuTurboFrequency;

    /**
     * 内存大小，单位：MiB。
     */
    private Integer memory;

    /**
     * 实例规格对应的本地盘配置信息
     */
    private String localVolumes;

    /**
     * 实例规格的云盘信息
     */
    private String volume;

}
