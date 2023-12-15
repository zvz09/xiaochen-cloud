package com.zvz09.xiaochen.blog.domain.vo;

import com.zvz09.xiaochen.blog.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.common.web.vo.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReptileDocumentVo extends BaseVo{

    @Schema(description ="url")
    private String url;

    @Schema(description ="标题")
    private String title;

    @Schema(description ="是否正常解析")
    private Boolean status;

    public ReptileDocumentVo(@NotNull ReptileDocument reptileDocument) {
        super(reptileDocument.getId());
        this.url = reptileDocument.getUrl();
        this.title = reptileDocument.getTitle();
        this.status = reptileDocument.getStatus();
    }
}
