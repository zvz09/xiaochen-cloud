package com.zvz09.xiaochen.message.api.constant;

import lombok.Getter;

/**
 * @author lizili-YF0033
 */

@Getter
public enum ActionEnums {
    FLOWABLE_TASK_CREATE("任务创建"),
    FLOWABLE_TASK_ASSIGNMENT("任务委派"),
    FLOWABLE_TASK_COMPLETE("任务完成"),
    FLOWABLE_TASK_DELETE("任务删除"),
    FLOWABLE_PROCESS_DELETE("流程完成"),
    ;

    private final String action;

    ActionEnums(String action) {
        this.action = action;
    }

}
