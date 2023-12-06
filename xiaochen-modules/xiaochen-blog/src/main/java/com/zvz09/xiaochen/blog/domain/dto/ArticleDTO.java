package com.zvz09.xiaochen.blog.domain.dto;

import com.zvz09.xiaochen.blog.domain.entity.Article;
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
    private String title;
    private String avatar;
    private String summary;
    private String content;

    private String keywords;
    private Integer readType;
    private Boolean isStick;
    private Boolean isOriginal;
    private String originalUrl;
    private String categoryName;
    private Boolean isPublish;

    private Boolean isCarousel;

    private Boolean isRecommend;

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
