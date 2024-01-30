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
 * @since 2024-01-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_micro")
@Schema(name = "SysMicro", description = "微前端配置表")
public class SysMicro extends BaseEntity {

    @Schema(description = "微前端访问路径")
    private String path;

    @Schema(description = "微前端名称")
    private String name;

    @Schema(description = "微前端地址")
    private String url;

    @Schema(description = "排序")
    private Long sort;

    @Schema(description = "菜单和面包屑对应的图标")
    private String icon;

    @Schema(description = "微前端标题")
    private String title;
    @Schema(description = "微前端主页路由地址")
    private String redirect;
}
