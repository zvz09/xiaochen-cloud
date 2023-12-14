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
 * 爬取的原始数据
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("blog_reptile_document")
@Schema(description ="爬取的原始数据")
public class ReptileDocument extends BaseEntity {


    @Schema(description ="url")
    private String url;

    @Schema(description ="文章内容")
    private String content;

    private Boolean status;


}
