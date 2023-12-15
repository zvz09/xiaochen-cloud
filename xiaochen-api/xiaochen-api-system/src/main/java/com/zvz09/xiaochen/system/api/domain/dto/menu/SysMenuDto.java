package com.zvz09.xiaochen.system.api.domain.dto.menu;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zvz09
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(name = "菜单新建/修改请求体")
public class SysMenuDto extends BaseDto<SysMenu> {

    @Schema(description = "父id")
    private String parentId;
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
    @Schema(description = "路由外链时填写的访问地址")
    private String isLink;
    @Schema(description = "是否在菜单中隐藏 (通常列表详情页需要隐藏)")
    private Boolean isHide;
    @Schema(description = "菜单是否全屏 (示例：数据大屏页面)")
    private Boolean isFull;
    @Schema(description = "菜单是否固定在标签页中 (首页通常是固定项)")
    private Boolean isAffix;
    @Schema(description = "当前路由是否缓存")
    private Boolean isKeepAlive;

    public SysMenu convertedToPo() {
        return SysMenu.builder().id(this.getId())
                .parentId(Long.valueOf(this.getParentId()))
                .path(this.path)
                .name(this.name)
                .redirect(this.redirect)
                .component(this.component)
                .sort(this.sort)
                .icon(this.icon)
                .title(this.title)
                .link(this.isLink)
                .hide(this.isHide)
                .fullScreen(this.isFull)
                .affix(this.isAffix)
                .keepAlive(this.isKeepAlive)
                .build();
    }
}
