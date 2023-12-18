package com.zvz09.xiaochen.note.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 博客文章表
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
@TableName("note_article")
public class Article extends BaseEntity {


    private String userId;

    private Long categoryId;

    private String title;

    private String avatar;

    private String summary;

    private String content;

    private Integer readType;

    private Boolean stick;

    private Boolean original;

    private String originalUrl;

    private Long quantity;

    private Boolean recommend;

}
