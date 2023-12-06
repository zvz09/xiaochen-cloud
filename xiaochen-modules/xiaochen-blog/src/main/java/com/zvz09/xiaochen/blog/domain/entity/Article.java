package com.zvz09.xiaochen.blog.domain.entity;

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
@TableName("blog_article")
@Schema(description = "博客文章表")
public class Article extends BaseEntity {


    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章封面地址")
    private String avatar;

    @Schema(description = "文章简介")
    private String summary;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(description = "是否置顶 0否 1是")
    private Boolean isStick;

    @Schema(description = "是否发布 0：下架 1：发布")
    private Boolean isPublish;

    @Schema(description = "是否原创  0：转载 1:原创")
    private Boolean isOriginal;

    @Schema(description = "转载地址")
    private String originalUrl;

    @Schema(description = "文章阅读量")
    private Long quantity;

    @Schema(description = "是否首页轮播")
    private Boolean isCarousel;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "关键词")
    private String keywords;

}
