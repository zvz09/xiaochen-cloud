package com.zvz09.xiaochen.flowable.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lizili-YF0033
 */

@Getter
@AllArgsConstructor
public enum ProcessStatus {
    /**
     * 进行中（审批中）
     */
    RUNNING("running"),
    /**
     * 已终止
     */
    TERMINATED("terminated"),
    /**
     * 已完成
     */
    COMPLETED("completed"),
    /**
     * 已取消
     */
    CANCELED("canceled");

    private final String status;

    public static ProcessStatus getProcessStatus(String str) {
        if (StringUtils.isNotBlank(str)) {
            for (ProcessStatus value : values()) {
                if (StringUtils.equalsIgnoreCase(str, value.getStatus())) {
                    return value;
                }
            }
        }
        return null;
    }
}
