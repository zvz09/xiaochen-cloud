package com.zvz09.xiaochen.blog.domain.dto;

import com.zvz09.xiaochen.blog.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.common.web.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReptileDocumentDto extends BaseDto<ReptileDocument> {

    @Schema(description ="url")
    private String url;

    @Schema(description ="标题")
    private String title;

    @Schema(description ="文章内容")
    private String content;

    @Schema(description ="是否正常解析")
    private Boolean status;

    @Override
    public ReptileDocument convertedToPo() {
        return ReptileDocument.builder()
                .id(this.getId())
                .url(this.url)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .build();
    }
}
