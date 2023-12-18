package com.zvz09.xiaochen.note.domain.dto;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.note.domain.entity.ReptileClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReptileClassDTO extends BaseDto<ReptileClass> {

    @Schema(description ="类名")
    private String className;

    @Schema(description ="类内容")
    private String content;
    @Override
    public ReptileClass convertedToPo() {
        return ReptileClass.builder()
                .id(this.getId())
                .className(this.className)
                .content(this.content)
                .build();
    }
}
