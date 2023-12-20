package com.zvz09.xiaochen.note.constant;

import lombok.Getter;

/**
 * @author lizili-YF0033
 */
@Getter
public enum OperateAction {
    /**
     * 绑定
     */
    enable("enable",true),
    /**
     * 解绑
     */
    disable("disable",false),
    ;

    private final String description;

    private final boolean status;

    OperateAction(String description, boolean status) {
        this.description = description;
        this.status = status;
    }

}
