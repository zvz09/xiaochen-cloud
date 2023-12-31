package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * default description 实体类
 *
 * @author Default Author
 * @date 2023-11-14 10:48:39
 */
@Data
@TableName("casbin_rule")
public class CasbinRule {

    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String ptype;
    private String v0;
    private String v1;
    private String v2;
    private String v3;
    private String v4;
    private String v5;
}

