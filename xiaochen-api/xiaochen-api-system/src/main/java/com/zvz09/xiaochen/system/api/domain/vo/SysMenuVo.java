package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author zvz09
 */
@Getter
@Setter
public class SysMenuVo extends BaseVo {

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

    private Meta meta;

    @Schema(description = "子菜单")
    private List<SysMenuVo> children;

    public SysMenuVo(SysMenu sysMenu) {
        super(sysMenu.getId());
        this.parentId = String.valueOf(sysMenu.getParentId());
        this.path = sysMenu.getPath();
        this.name = sysMenu.getName();
        this.redirect = sysMenu.getRedirect();
        this.component = sysMenu.getComponent();
        this.sort = sysMenu.getSort();
        this.meta = new Meta(sysMenu);
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Meta {
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

    public Meta(SysMenu sysMenu) {
        this.icon = sysMenu.getIcon();
        this.title = sysMenu.getTitle();
        this.isLink = sysMenu.getLink();
        this.isHide = sysMenu.getHide();
        this.isFull = sysMenu.getFullScreen();
        this.isAffix = sysMenu.getAffix();
        this.isKeepAlive = sysMenu.getKeepAlive();
    }
}