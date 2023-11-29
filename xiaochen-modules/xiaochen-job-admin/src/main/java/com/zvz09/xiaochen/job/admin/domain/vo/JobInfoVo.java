package com.zvz09.xiaochen.job.admin.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.job.admin.domain.entity.JobInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobInfoVo extends BaseVo {

    @Schema(description ="微服务名")
    private String jobGroup;

    private String jobDesc;

    @Schema(description ="作者")
    private String author;

    @Schema(description ="报警邮件")
    private String alarmEmail;

    @Schema(description ="调度类型")
    private String scheduleType;

    @Schema(description ="调度配置，值含义取决于调度类型")
    private String scheduleConf;

    @Schema(description ="调度过期策略")
    private String misfireStrategy;

    @Schema(description ="执行器路由策略")
    private String executorRouteStrategy;

    @Schema(description ="执行器任务handler")
    private String executorHandler;

    @Schema(description ="执行器任务参数")
    private String executorParam;

    @Schema(description ="阻塞处理策略")
    private String executorBlockStrategy;

    @Schema(description ="任务执行超时时间，单位秒")
    private Integer executorTimeout;

    @Schema(description ="失败重试次数")
    private Integer executorFailRetryCount;

    @Schema(description ="GLUE类型")
    private String glueType;

    @Schema(description ="GLUE源代码")
    private String glueSource;

    @Schema(description ="GLUE备注")
    private String glueRemark;

    @Schema(description ="GLUE更新时间")
    private LocalDateTime glueUpdatetime;

    @Schema(description ="调度状态：0-停止，1-运行")
    private Boolean triggerStatus;

    public JobInfoVo(JobInfo jobInfo) {
        super(jobInfo.getId());
        this.jobGroup = jobInfo.getJobGroup();
        this.jobDesc = jobInfo.getJobDesc();
        this.author = jobInfo.getAuthor();
        this.alarmEmail = jobInfo.getAlarmEmail();
        this.scheduleConf = jobInfo.getScheduleConf();
        this.scheduleType = jobInfo.getScheduleType();
        this.misfireStrategy = jobInfo.getMisfireStrategy();
        this.executorRouteStrategy = jobInfo.getExecutorRouteStrategy();
        this.executorHandler = jobInfo.getExecutorHandler();
        this.executorParam = jobInfo.getExecutorParam();
        this.executorBlockStrategy = jobInfo.getExecutorBlockStrategy();
        this.executorTimeout = jobInfo.getExecutorTimeout();
        this.executorFailRetryCount = jobInfo.getExecutorFailRetryCount();
        this.glueType = jobInfo.getGlueType();
        this.glueSource = jobInfo.getGlueSource();
        this.glueRemark = jobInfo.getGlueRemark();
        this.glueUpdatetime = jobInfo.getGlueUpdatetime();
        this.triggerStatus = jobInfo.getTriggerStatus();
    }
}
