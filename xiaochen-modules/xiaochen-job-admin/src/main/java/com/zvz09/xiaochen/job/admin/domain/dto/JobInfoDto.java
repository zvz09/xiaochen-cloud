package com.zvz09.xiaochen.job.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.job.admin.domain.entity.JobInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zvz09
 * @since 2023-11-28
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("job_info")
@Schema(description = "")
public class JobInfoDto extends BaseDto {


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

    public JobInfo convertToJobInfo() {
        JobInfo result = new JobInfo();
        result.setJobGroup(this.getJobGroup());
        result.setJobDesc(this.getJobDesc());
        result.setAuthor(this.getAuthor());
        result.setAlarmEmail(this.getAlarmEmail());
        result.setScheduleType(this.getScheduleType());
        result.setScheduleConf(this.getScheduleConf());
        result.setMisfireStrategy(this.getMisfireStrategy());
        result.setExecutorRouteStrategy(this.getExecutorRouteStrategy());
        result.setExecutorHandler(this.getExecutorHandler());
        result.setExecutorParam(this.getExecutorParam());
        result.setExecutorBlockStrategy(this.getExecutorBlockStrategy());
        result.setExecutorTimeout(this.getExecutorTimeout());
        result.setExecutorFailRetryCount(this.getExecutorFailRetryCount());
        result.setGlueType(this.getGlueType());
        result.setGlueSource(this.getGlueSource());
        result.setGlueRemark(this.getGlueRemark());
        result.setGlueUpdatetime(this.getGlueUpdatetime());
        result.setTriggerStatus(this.getTriggerStatus());
        return result;
    }
}
