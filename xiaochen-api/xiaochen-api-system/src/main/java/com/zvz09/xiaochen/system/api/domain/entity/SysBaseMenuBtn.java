package com.zvz09.xiaochen.system.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_base_menu_btn")
@Schema(name = "SysBaseMenuBtn", description = "")
public class SysBaseMenuBtn extends BaseEntity {

    private String code;

    private String name;

    private String description;

    private Long sysBaseMenuId;
}
