package com.zvz09.xiaochen.note.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author blue
 * @date 2022/1/25
 * @apiNote
 */
@Data
public class ArticleVO {

    @JsonSerialize(using = ToStringSerializer.class) //雪花算法id 返给前端转为 string类型
    private Long id;

    private Long userId;

    @Schema(description = "用户名")
    private String userName;

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

    @JsonIgnore
    private List<Long> tagIds;

    private List<String> tags;

    private String createdAt;

    private String updatedAt;
}
