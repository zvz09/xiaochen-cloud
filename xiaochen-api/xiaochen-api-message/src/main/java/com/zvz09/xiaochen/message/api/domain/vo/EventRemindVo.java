package com.zvz09.xiaochen.message.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.message.api.domain.entity.EventRemind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author lizili-YF0033
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRemindVo extends BaseVo {

    @Schema(description = "动作类型")
    private String action;
    @Schema(description = "事件源ID")
    private String sourceId;
    @Schema(description = "事件源类型")
    private String sourceType;
    @Schema(description = "事件源的内容")
    private String sourceContent;

    public EventRemindVo(EventRemind eventRemind) {
        super(eventRemind.getId());
        this.action = eventRemind.getAction();
        this.sourceId = eventRemind.getSourceId();
        this.sourceType = eventRemind.getSourceType();
        this.sourceContent = eventRemind.getSourceContent();
    }
}

