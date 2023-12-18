package com.zvz09.xiaochen.note.domain.dto;

import com.zvz09.xiaochen.note.domain.entity.Article;
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
    private String categoryName;

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
    private Boolean stick;

    @Schema(description = "是否原创  0：转载 1:原创")
    private Boolean original;

    @Schema(description = "转载地址")
    private String originalUrl;

    @Schema(description = "文章阅读量")
    private Long quantity;

    @Schema(description = "是否推荐")
    private Boolean recommend;

    private List<String> tags;

    public static Article convertedToPo(ArticleDTO item) {
        if (item == null) {
            return null;
        }
        Article result = new Article();
        result.setTitle(item.getTitle());
        result.setAvatar(item.getAvatar());
        result.setSummary(item.getSummary());
        result.setContent(item.getContent());
        result.setReadType(item.getReadType());
        result.setStick(item.getStick());
        result.setOriginal(item.getOriginal());
        result.setOriginalUrl(item.getOriginalUrl());
        result.setRecommend(item.getRecommend());
        if (item.getId() != null) {
            result.setId(Long.valueOf(item.getId()));
        }
        return result;
    }
}
