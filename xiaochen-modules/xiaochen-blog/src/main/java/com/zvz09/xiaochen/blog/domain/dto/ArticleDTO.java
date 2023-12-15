package com.zvz09.xiaochen.blog.domain.dto;

import com.zvz09.xiaochen.blog.domain.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author blue
 * @date 2022/1/25
 * @apiNote
 */
@Data
public class ArticleDTO {
    private String id;
    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "分类名")
    private String CategoryName;

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

    private List<String> tags;

    public static Article convertToArticle(ArticleDTO item) {
        if (item == null) {
            return null;
        }
        Article result = new Article();
        result.setTitle(item.getTitle());
        result.setAvatar(item.getAvatar());
        result.setSummary(item.getSummary());
        result.setContent(item.getContent());
        result.setReadType(item.getReadType());
        result.setIsStick(item.getIsStick());
        result.setIsPublish(item.getIsPublish());
        result.setIsOriginal(item.getIsOriginal());
        result.setOriginalUrl(item.getOriginalUrl());
        result.setIsCarousel(item.getIsCarousel());
        result.setIsRecommend(item.getIsRecommend());
        result.setKeywords(item.getKeywords());
        if (item.getId() != null) {
            result.setId(Long.valueOf(item.getId()));
        }
        return result;
    }
}
