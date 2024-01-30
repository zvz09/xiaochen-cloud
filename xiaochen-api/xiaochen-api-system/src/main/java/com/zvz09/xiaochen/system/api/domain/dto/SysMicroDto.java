package com.zvz09.xiaochen.system.api.domain.dto;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysMicro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zvz09
 * @since 2024-01-29
 */
@Getter
@Setter
public class SysMicroDto extends BaseDto<SysMicro> {
    @Schema(description ="微前端访问路径")
    private String path;

    @Schema(description ="微前端名称")
    private String name;

    @Schema(description ="微前端地址")
    private String url;

    @Schema(description ="排序")
    private Long sort;

    @Schema(description ="菜单和面包屑对应的图标")
    private String icon;

    @Schema(description ="微前端标题")
    private String title;

    @Schema(description = "微前端主页路由地址")
    private String redirect;

    @Override
    public SysMicro convertedToPo() {
        return SysMicro.builder()
                .id(this.getId())
                .path(this.path)
                .name(this.name)
                .url(this.url)
                .sort(this.sort)
                .icon(this.icon)
                .title(this.title)
                .redirect(this.redirect)
                .build();
    }
}
