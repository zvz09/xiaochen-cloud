package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Getter
@Setter
@TableName("sys_auto_code_historiy")
@Schema(name = "SysAutoCodeHistoriy", description = "")
public class SysAutoCodeHistoriy extends BaseEntity {

    private String packageName;

    private String businessDb;

    private String tableName;

    private String requestMeta;

    private String autoCodePath;

    private String injectionMeta;

    private String structName;

    private String structCnName;

    private String apiIds;

    private Long flag;
}
