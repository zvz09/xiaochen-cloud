package com.zvz09.xiaochen.message.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 事件提醒 实体类
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("event_remind")
@Schema(name = "EventRemind", description = "事件提醒")
public class EventRemind extends BaseEntity {

    @Schema(description = "动作类型")
    private String action;
    @Schema(description = "事件源ID")
    private String sourceId;
    @Schema(description = "事件源类型")
    private String sourceType;
    @Schema(description = "事件源的内容")
    private String sourceContent;
    @Schema(description = "是否已读")
    private Boolean state;
    @Schema(description = "操作者的ID")
    private Long senderId;
    @Schema(description = "接受通知的用户的ID")
    private Long recipientId;
    @Schema(description = "提醒的时间")
    private LocalDateTime remindTime;
}
