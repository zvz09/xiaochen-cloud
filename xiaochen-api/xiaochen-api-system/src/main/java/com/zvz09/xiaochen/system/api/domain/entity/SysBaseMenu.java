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
@TableName("sys_base_menu")
@Schema(name = "SysBaseMenu", description = "")
public class SysBaseMenu extends BaseEntity {

    private Long menuLevel;

    @Schema(description = "父菜单Id")
    private String parentId;

    @Schema(description = "路由path")
    private String path;

    @Schema(description = "路由name")
    private String name;

    @Schema(description = "是否在列表隐藏")
    private Boolean hidden;

    @Schema(description = "对应前端文件路径")
    private String component;

    @Schema(description = "排序")
    private Long sort;

    @Schema(description = "高亮菜单")
    private String activeName;

    @Schema(description = "是否缓存")
    private Boolean keepAlive;

    @Schema(description = "是否是基础路由")
    private Boolean defaultMenu;

    @Schema(description = "菜单名")
    private String title;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "自动关闭tab")
    private Boolean closeTab;
}
