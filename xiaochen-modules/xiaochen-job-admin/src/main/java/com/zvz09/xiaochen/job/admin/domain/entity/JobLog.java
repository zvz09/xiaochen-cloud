package com.zvz09.xiaochen.job.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
@Data
@TableName("job_log")
@Schema(description = "")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="执行器主键ID")
    private String jobGroup;

    @Schema(description ="任务，主键ID")
    private Long jobId;

    @Schema(description ="执行器地址，本次执行的地址")
    private String executorAddress;

    @Schema(description ="执行器任务handler")
    private String executorHandler;

    @Schema(description ="执行器任务参数")
    private String executorParam;

    @Schema(description ="执行器任务分片参数，格式如 1/2")
    private String executorShardingParam;

    @Schema(description ="失败重试次数")
    private Integer executorFailRetryCount;

    @Schema(description ="调度-时间")
    private LocalDateTime triggerTime;

    @Schema(description ="调度-结果")
    private Integer triggerCode;

    @Schema(description ="调度-日志")
    private String triggerMsg;

    @Schema(description ="执行-时间")
    private LocalDateTime handleTime;

    @Schema(description ="执行-状态")
    private Integer handleCode;

    @Schema(description ="执行-日志")
    private String handleMsg;

    @Schema(description ="告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败")
    private Integer alarmStatus;

    private String logTraceId;

}
