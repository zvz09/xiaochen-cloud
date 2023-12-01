package com.zvz09.xiaochen.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Data
@TableName("blog_article_tag")
@Schema(description ="BlogArticleTag对象")
public class ArticleTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description ="文章id")
    private Long articleId;

    @Schema(description ="标签id")
    private Long tagId;

    public ArticleTag(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }
}
