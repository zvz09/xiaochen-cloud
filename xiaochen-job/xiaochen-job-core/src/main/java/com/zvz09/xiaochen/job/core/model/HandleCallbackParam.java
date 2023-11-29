package com.zvz09.xiaochen.job.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by xuxueli on 17/3/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandleCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private long logId;
    private LocalDateTime logDateTim;
    private int handleCode;
    private String handleMsg;
}
