package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 系统权限字和api接口关联表
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_perm_code_api")
@Schema(description = "系统权限字和api接口关联表")
public class SysPermCodeApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限字Id")
    private Long permCodeId;

    @Schema(description = "权限id")
    private Long apiId;

}
