package com.zvz09.xiaochen.message.api.constant;

import lombok.Getter;

/**
 * @author zvz09
 */

@Getter
public enum NoticeSourceType {
    SYSTEM("system", "系统消息"),
    FLOWABLE("flowable", "工作流");

    private final String type;
    private final String paraphrase;

    NoticeSourceType(String type, String paraphrase) {
        this.type = type;
        this.paraphrase = paraphrase;
    }

}
