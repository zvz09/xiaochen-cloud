package com.zvz09.xiaochen.system.api.constant;

import lombok.Getter;

/**
 * @author lizili-YF0033
 */

@Getter
public enum PermCodeType {

    MENU(0),
    BUTTON(1),
    ;

    private final Integer type;

    PermCodeType(Integer type) {
        this.type = type;
    }
}
