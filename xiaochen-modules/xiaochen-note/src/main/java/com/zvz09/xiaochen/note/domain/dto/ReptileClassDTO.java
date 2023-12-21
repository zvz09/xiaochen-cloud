package com.zvz09.xiaochen.note.domain.dto;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.note.domain.entity.ReptileParseClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
public class ReptileClassDTO extends BaseDto<ReptileParseClass> {

    @Schema(description ="类内容")
    private String content;
    @Override
    public ReptileParseClass convertedToPo() {
        return ReptileParseClass.builder()
                .id(this.getId())
                .content(this.content)
                .build();
    }
}
