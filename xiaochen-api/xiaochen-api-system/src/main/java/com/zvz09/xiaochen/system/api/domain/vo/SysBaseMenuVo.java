/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.vo
 * @className com.zvz09.xiaochen.system.vo.SysBaseMenuVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenu;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuBtn;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SysBaseMenuVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/31 13:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "菜单")
public class SysBaseMenuVo extends BaseVo {

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

    @Schema(description = "父菜单ID")
    private String parentId;

    private Meta meta;

    @Schema(description = "菜单参数")
    List<SysBaseMenuParameterVo> menuParameter;

    @Schema(description = "可控按钮")
    List<SysBaseMenuBtnVo> menuBtn;

    @Schema(description = "子菜单")
    private List<SysBaseMenuVo> children;


    public SysBaseMenuVo(SysBaseMenu sysBaseMenu) {
        super(sysBaseMenu.getId());
        this.parentId = sysBaseMenu.getParentId();
        this.path = sysBaseMenu.getPath();
        this.name = sysBaseMenu.getName();
        this.hidden = sysBaseMenu.getHidden();
        this.component = sysBaseMenu.getComponent();
        this.sort = sysBaseMenu.getSort();
        this.meta = new Meta(sysBaseMenu);
    }

    public SysBaseMenuVo(SysBaseMenu sysBaseMenu,
                         List<SysBaseMenuBtn> sysBaseMenuBtns) {
        super(sysBaseMenu.getId());
        this.parentId = sysBaseMenu.getParentId();
        this.path = sysBaseMenu.getPath();
        this.name = sysBaseMenu.getName();
        this.hidden = sysBaseMenu.getHidden();
        this.component = sysBaseMenu.getComponent();
        this.sort = sysBaseMenu.getSort();
        this.meta = new Meta(sysBaseMenu);
        if (sysBaseMenuBtns != null && !sysBaseMenuBtns.isEmpty()) {
            this.menuBtn =
                    sysBaseMenuBtns.stream().map(SysBaseMenuBtnVo::new).collect(Collectors.toList());
        } else {
            this.menuBtn = new ArrayList<>();
        }
    }

    public SysBaseMenuVo(SysBaseMenu sysBaseMenu,
                         List<SysBaseMenuParameter> sysBaseMenuParameters,
                         List<SysBaseMenuBtn> sysBaseMenuBtns) {
        super(sysBaseMenu.getId());
        this.parentId = sysBaseMenu.getParentId();
        this.path = sysBaseMenu.getPath();
        this.name = sysBaseMenu.getName();
        this.hidden = sysBaseMenu.getHidden();
        this.component = sysBaseMenu.getComponent();
        this.sort = sysBaseMenu.getSort();
        this.meta = new Meta(sysBaseMenu);
        if (sysBaseMenuParameters != null && !sysBaseMenuParameters.isEmpty()) {
            this.menuParameter =
                    sysBaseMenuParameters.stream().map(SysBaseMenuParameterVo::new).collect(Collectors.toList());
        } else {
            this.menuParameter = new ArrayList<>();
        }
        if (sysBaseMenuBtns != null && !sysBaseMenuBtns.isEmpty()) {
            this.menuBtn =
                    sysBaseMenuBtns.stream().map(SysBaseMenuBtnVo::new).collect(Collectors.toList());
        } else {
            this.menuBtn = new ArrayList<>();
        }
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

    public Meta(SysBaseMenu sysBaseMenu) {
        this.activeName = sysBaseMenu.getActiveName();
        this.keepAlive = sysBaseMenu.getKeepAlive();
        this.defaultMenu = sysBaseMenu.getDefaultMenu();
        this.title = sysBaseMenu.getTitle();
        this.icon = sysBaseMenu.getIcon();
        this.closeTab = sysBaseMenu.getCloseTab();
    }
}

@Getter
@Setter
class SysBaseMenuParameterVo {
    @Schema(description = "地址栏携带参数为params还是query")
    private String type;

    @Schema(description = "地址栏携带参数的key")
    private String key;

    @Schema(description = "地址栏携带参数值")
    private String value;

    public SysBaseMenuParameterVo(SysBaseMenuParameter sysBaseMenuParameter) {
        this.type = sysBaseMenuParameter.getType();
        this.key = sysBaseMenuParameter.getParameterKey();
        this.value = sysBaseMenuParameter.getParameterValue();
    }
}

@Getter
@Setter
class SysBaseMenuBtnVo {
    private String name;

    private String description;

    public SysBaseMenuBtnVo(SysBaseMenuBtn sysBaseMenuBtn) {
        this.name = sysBaseMenuBtn.getName();
        this.description = sysBaseMenuBtn.getDescription();
    }
}