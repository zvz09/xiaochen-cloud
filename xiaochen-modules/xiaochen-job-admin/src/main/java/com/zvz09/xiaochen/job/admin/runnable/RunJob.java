package com.zvz09.xiaochen.job.admin.runnable;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.core.util.Snowflake;
import com.zvz09.xiaochen.job.admin.config.JobHelper;
import com.zvz09.xiaochen.job.admin.entity.JobInfo;
import com.zvz09.xiaochen.job.admin.entity.JobLog;
import com.zvz09.xiaochen.job.core.ExecutorBizClient;
import com.zvz09.xiaochen.job.core.constant.FeignPath;
import com.zvz09.xiaochen.job.core.model.TriggerParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lizili-YF0033
 */
@Slf4j
public class RunJob implements Runnable {

    private final JobInfo jobInfo;

    public RunJob(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    @SneakyThrows
    @Override
    public void run() {
        // 1、save log-id
        String logTraceId = String.format("job-%s", Snowflake.getSnowflakeId());
        JobLog jobLog = new JobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(LocalDateTime.now());
        jobLog.setLogTraceId(logTraceId);
        JobHelper.getJobHelper().getJobLogService().save(jobLog);
        log.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setLogTraceId(logTraceId);
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(jobLog.getTriggerTime());
        triggerParam.setGlueType(jobInfo.getGlueType());
        triggerParam.setGlueSource(jobInfo.getGlueSource());
        triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime());

        // 3、init address
        String address = null;
        List<Instance> instances = JobHelper.getJobHelper().getServeInstanceService().getServiceInstances(jobInfo.getJobGroup());
        if(instances !=null && !instances.isEmpty()){
            Instance instance = instances.get(0);
            address = String.format("http://%s:%d"+ FeignPath.JOB_CLIENT,instance.getIp(),instance.getPort());
        }

        // 4、trigger remote executor
        ApiResult<String> triggerResult = null;
        if (address != null) {
            WebClient client = WebClient.builder().baseUrl(address).build();
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
            ExecutorBizClient service = factory.createClient(ExecutorBizClient.class);
            triggerResult = service.run(triggerParam);
        } else {
            triggerResult = ApiResult.fail("服务未注册或已下线。");
        }

        // 5、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerResult.getMsg());
        JobHelper.getJobHelper().getJobLogService().updateById(jobLog);
        log.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());
    }

}
