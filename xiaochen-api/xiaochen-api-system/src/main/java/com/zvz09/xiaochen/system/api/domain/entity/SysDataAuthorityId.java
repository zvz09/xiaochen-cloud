package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

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
@TableName("sys_data_authority_id")
@Schema(name = "SysDataAuthorityId", description = "")
public class SysDataAuthorityId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sysAuthorityAuthorityId;

    private Long dataAuthorityIdAuthorityId;
}
