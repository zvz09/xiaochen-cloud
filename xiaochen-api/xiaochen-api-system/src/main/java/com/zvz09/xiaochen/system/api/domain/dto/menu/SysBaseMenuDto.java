/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.menu
 * @className com.zvz09.xiaochen.system.dto.menu.SysBaseMenuDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.menu;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * SysBaseMenuDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/6 9:24
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysBaseMenuDto")
public class SysBaseMenuDto extends BaseDto {

    @Schema(description = "父菜单ID")
    @NotNull(message = "父菜单ID不能为空")
    private String parentId;

    @Schema(description = "路由path")
    @NotNull(message = "路由不能为空")
    private String path;

    @Schema(description = "路由name")
    private String name;

    @Schema(description = "是否在列表隐藏")
    private Boolean hidden;

    @Schema(description = "对应前端文件路径")
    private String component;

    @Schema(description = "排序")
    private Long sort;

    private Meta meta;

    @Schema(description = "菜单参数")
    List<SysBaseMenuParameterDto> menuParameter;

    @Schema(description = "可控按钮")
    List<SysBaseMenuBtnDto> menuBtn;

    /**
     * 转化为po对象
     *
     * @return SysBaseMenu
     */
    public SysBaseMenu convertedToPo() {
        return SysBaseMenu.builder().id(this.getId())
                .menuLevel(0L)
                .parentId(this.getParentId())
                .path(this.getPath())
                .name(this.getName())
                .hidden(this.getHidden())
                .component(this.getComponent())
                .sort(this.getSort())
                .activeName(this.meta.getActiveName())
                .keepAlive(this.meta.getKeepAlive())
                .defaultMenu(this.meta.getDefaultMenu())
                .title(this.meta.getTitle())
                .icon(this.meta.getIcon())
                .closeTab(this.meta.getCloseTab())
                .build();
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Meta {
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

