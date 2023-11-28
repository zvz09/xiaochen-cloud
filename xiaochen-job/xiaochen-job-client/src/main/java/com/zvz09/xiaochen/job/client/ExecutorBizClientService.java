package com.zvz09.xiaochen.job.client;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.client.executor.JobExecutor;
import com.zvz09.xiaochen.job.client.handler.IJobHandler;
import com.zvz09.xiaochen.job.client.thread.JobThread;
import com.zvz09.xiaochen.job.core.ExecutorBizClient;
import com.zvz09.xiaochen.job.core.constant.FeignPath;
import com.zvz09.xiaochen.job.core.enums.ExecutorBlockStrategyEnum;
import com.zvz09.xiaochen.job.core.model.IdleBeatParam;
import com.zvz09.xiaochen.job.core.model.KillParam;
import com.zvz09.xiaochen.job.core.model.TriggerParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author lizili-YF0033
 */
@Slf4j
@RestController
@RequestMapping(FeignPath.JOB_CLIENT)
@RequiredArgsConstructor
public class ExecutorBizClientService implements ExecutorBizClient {

    private final JobExecutor jobExecutor;

    @Override
    public ApiResult beat() {
        return ApiResult.success(jobExecutor.getAllHandlerNames());
    }

    @Override
    public ApiResult<String> idleBeat(IdleBeatParam idleBeatParam) {
        // isRunningOrHasQueue
        boolean isRunningOrHasQueue = false;
        JobThread jobThread = jobExecutor.loadJobThread(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }

        if (isRunningOrHasQueue) {
            return ApiResult.fail("job thread is running or has trigger queue.");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult<String> run(TriggerParam triggerParam) {
        // load old：jobHandler + jobThread
        JobThread jobThread = jobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread!=null?jobThread.getHandler():null;
        String removeOldReason = null;

        // new jobhandler
        IJobHandler newJobHandler = jobExecutor.loadJobHandler(triggerParam.getExecutorHandler());

        // valid old jobThread
        if (jobThread!=null && jobHandler != newJobHandler) {
            // change handler, need kill old thread
            removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

            jobThread = null;
            jobHandler = null;
        }

        // valid handler
        if (jobHandler == null) {
            jobHandler = newJobHandler;
            if (jobHandler == null) {
                return ApiResult.fail("job handler [" + triggerParam.getExecutorHandler() + "] not found.");
            }
        }


        // executor block strategy
        if (jobThread != null) {
            ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(triggerParam.getExecutorBlockStrategy(), null);
            if (ExecutorBlockStrategyEnum.DISCARD_LATER == blockStrategy) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return ApiResult.fail("block strategy effect：" + ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                }
            } else if (ExecutorBlockStrategyEnum.COVER_EARLY == blockStrategy) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason = "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();

                    jobThread = null;
                }
            } else {
                // just queue trigger
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = jobExecutor.registJobThread(triggerParam.getJobId(), jobHandler, removeOldReason);
        }

        // push data to queue
        return jobThread.pushTriggerQueue(triggerParam);
    }

    @Override
    public ApiResult<String> kill(KillParam killParam) {
        // kill handlerThread, and create new one
        JobThread jobThread = jobExecutor.loadJobThread(killParam.getJobId());
        if (jobThread != null) {
            jobExecutor.removeJobThread(killParam.getJobId(), "scheduling center kill job.");
            return ApiResult.success();
        }

        return ApiResult.success( "job thread already killed.");
    }

}
