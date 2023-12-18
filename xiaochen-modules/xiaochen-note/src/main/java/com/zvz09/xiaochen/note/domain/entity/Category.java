package com.zvz09.xiaochen.note.domain.entity;

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
 * 博客分类表
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("note_category")
@Schema(description = "博客分类表")
public class Category extends BaseEntity {

    @Schema(description = "分类名称")
    private String name;

    private Integer clickVolume;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "图标")
    private String icon;

}
