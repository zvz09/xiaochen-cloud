package com.zvz09.xiaochen.system.api.constant;

import lombok.Getter;

/**
 * @author zvz09
 */

@Getter
public enum PermCodeType {

    MENU("0"),
    BUTTON("1"),
    ;

    private final String type;

    PermCodeType(String type) {
        this.type = type;
    }
}
