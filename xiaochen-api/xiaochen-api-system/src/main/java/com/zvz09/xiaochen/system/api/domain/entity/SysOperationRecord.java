package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@TableName("sys_operation_record")
@Schema(name = "SysOperationRecord", description = "")
public class SysOperationRecord extends BaseEntity {

    private String ip;

    private String method;

    private String path;

    private Long status;

    private Long elapsedTime;

    private String agent;

    private String errorMessage;

    private String body;

    private String resp;

    private String username;
    private String nickName;
}
