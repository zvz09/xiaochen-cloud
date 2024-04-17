package com.zvz09.xiaochen.mcmp.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 主账号表
 * @TableName mcmp_primary_account
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value ="mcmp_account")
public class Account extends BaseEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 云厂商
     */
    private String provider;

    /**
     * 状态
     */
    private String status;

    /**
     * 账号ID
     */
    private String account;

    /**
     * ak
     */
    private String accessKey;

    /**
     * sk
     */
    private String secretKey;

    /**
     * 描述
     */
    private String description;

}
