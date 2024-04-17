package com.zvz09.xiaochen.mcmp.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 地域表
 * @TableName mcmp_region
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value ="mcmp_region")
public class Region extends BaseEntity {


    /**
     * 云厂商code
     */
    private String providerCode;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 地域编码
     */
    private String regionCode;

    /**
     * 地域名称
     */
    private String regionName;

}
