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
 * 菜单表 实体类
 *
 * @author zvz09
 * @date 2023-11-16 15:40:03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_menu")
@Schema(name = "SysMenu", description = "菜单表")
public class SysMenu extends BaseEntity {

    @Schema(description = "父id")
    private Long parentId;
    @Schema(description = "路由菜单访问路径")
    private String path;
    @Schema(description = "路由名称")
    private String name;
    @Schema(description = "路由重定向地址")
    private String redirect;
    @Schema(description = "视图文件路径")
    private String component;
    @Schema(description = "排序")
    private Long sort;

    @Schema(description = "菜单和面包屑对应的图标")
    private String icon;
    @Schema(description = "路由标题")
    private String title;
    @Schema(description = "路由标题")
    private String link;
    @Schema(description = "是否在菜单中隐藏")
    private Boolean hide;
    @Schema(description = "菜单是否全屏")
    private Boolean fullScreen;
    @Schema(description = "菜单是否固定在标签页中")
    private Boolean affix;
    @Schema(description = "当前路由是否缓存")
    private Boolean keepAlive;
}
