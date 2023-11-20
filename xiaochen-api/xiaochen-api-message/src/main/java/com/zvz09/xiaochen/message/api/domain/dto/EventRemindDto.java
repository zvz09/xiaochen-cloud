package com.zvz09.xiaochen.message.api.domain.dto;

import com.zvz09.xiaochen.message.api.constant.ActionEnums;
import com.zvz09.xiaochen.message.api.constant.NoticeSourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 事件提醒 实体类
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */
@Getter
@Setter
@Builder
public class EventRemindDto {

    @Schema(description = "动作类型")
    private ActionEnums action;
    @Schema(description = "事件源ID")
    private String sourceId;
    @Schema(description = "事件源类型")
    private NoticeSourceType sourceType;
    @Schema(description = "事件源的内容")
    private String sourceContent;
    @Schema(description = "操作者的ID")
    private Long senderId;
    @Schema(description = "接受通知的用户的ID")
    private String recipientId;

}
