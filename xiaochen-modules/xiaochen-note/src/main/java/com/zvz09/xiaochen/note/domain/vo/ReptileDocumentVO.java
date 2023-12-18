package com.zvz09.xiaochen.note.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.note.domain.entity.ReptileDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReptileDocumentVO extends BaseVo{

    @Schema(description ="url")
    private String url;

    @Schema(description ="标题")
    private String title;

    @Schema(description ="是否正常解析")
    private Boolean status;

    public ReptileDocumentVO(@NotNull ReptileDocument reptileDocument) {
        super(reptileDocument.getId());
        this.url = reptileDocument.getUrl();
        this.title = reptileDocument.getTitle();
        this.status = reptileDocument.getStatus();
    }
}
