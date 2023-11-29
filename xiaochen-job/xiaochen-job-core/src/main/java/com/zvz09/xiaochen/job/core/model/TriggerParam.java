package com.zvz09.xiaochen.job.core.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by xuxueli on 16/7/22.
 */
@Data
public class TriggerParam implements Serializable{
    private static final long serialVersionUID = 42L;

    private Long jobId;
    private String logTraceId;
    private String executorHandler;
    private String executorParams;
    private String executorBlockStrategy;
    private int executorTimeout;

    private long logId;
    private LocalDateTime logDateTime;

    private String glueType;
    private String glueSource;
    private LocalDateTime glueUpdatetime;

    private int broadcastIndex;
    private int broadcastTotal;
}
