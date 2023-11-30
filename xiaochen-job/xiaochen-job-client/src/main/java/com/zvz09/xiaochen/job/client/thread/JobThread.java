package com.zvz09.xiaochen.job.client.thread;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.client.config.ClientHelper;
import com.zvz09.xiaochen.job.client.executor.JobExecutor;
import com.zvz09.xiaochen.job.client.handler.IJobHandler;
import com.zvz09.xiaochen.job.core.context.XiaoChenJobContext;
import com.zvz09.xiaochen.job.core.context.XiaoChenJobHelper;
import com.zvz09.xiaochen.job.core.model.HandleCallbackParam;
import com.zvz09.xiaochen.job.core.model.TriggerParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.zvz09.xiaochen.common.core.constant.CommonConstant.TRACE_ID;


/**
 * handler thread
 * @author xuxueli 2016-1-16 19:52:47
 */
@Slf4j
public class JobThread extends Thread{

	private final Long jobId;
	@Getter
	private IJobHandler handler;
	private final LinkedBlockingQueue<TriggerParam> triggerQueue;
	private final Set<Long> triggerLogIdSet;		// avoid repeat trigger for the same TRIGGER_LOG_ID

	private volatile boolean toStop = false;
	private String stopReason;

    private boolean running = false;    // if running job
	private int idleTimes = 0;			// idel times
	private final JobExecutor jobExecutor;
	public JobThread(Long jobId, IJobHandler handler,JobExecutor jobExecutor) {
		this.jobId = jobId;
		this.handler = handler;
		this.jobExecutor = jobExecutor;
		this.triggerQueue = new LinkedBlockingQueue<TriggerParam>();
		this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<Long>());

		// assign job thread name
		this.setName("xiaochen-job, JobThread-"+jobId+"-"+System.currentTimeMillis());
	}

	/**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
	public ApiResult<String> pushTriggerQueue(TriggerParam triggerParam) {
		// avoid repeat
		if (triggerLogIdSet.contains(triggerParam.getLogId())) {
			log.info(">>>>>>>>>>> repeate trigger job, logId:{}", triggerParam.getLogId());
			return ApiResult.fail("repeate trigger job, logId:" + triggerParam.getLogId());
		}

		triggerLogIdSet.add(triggerParam.getLogId());
		triggerQueue.add(triggerParam);
        return ApiResult.success();
	}

    /**
     * kill job thread
     *
     * @param stopReason
     */
	public void toStop(String stopReason) {
		/**
		 * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
		 * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
		 * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
		 */
		this.toStop = true;
		this.stopReason = stopReason;
	}

    /**
     * is running job
     * @return
     */
    public boolean isRunningOrHasQueue() {
        return running || triggerQueue.size()>0;
    }

    @Override
	public void run() {

    	// init
    	try {
			handler.init();
		} catch (Throwable e) {
    		log.error(e.getMessage(), e);
		}

		// execute
		while(!toStop){
			running = false;
			idleTimes++;

            TriggerParam triggerParam = null;
            try {
				// to check toStop signal, we need cycle, so wo cannot use queue.take(), instand of poll(timeout)
				triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
				if (triggerParam!=null) {
					MDC.put(TRACE_ID, triggerParam.getLogTraceId());
					running = true;
					idleTimes = 0;
					triggerLogIdSet.remove(triggerParam.getLogId());

					XiaoChenJobContext xiaoChenJobContext = new XiaoChenJobContext(
							triggerParam.getJobId(),
							triggerParam.getExecutorParams(),
							triggerParam.getBroadcastIndex(),
							triggerParam.getBroadcastTotal());

					// init job context
					XiaoChenJobContext.setXxlJobContext(xiaoChenJobContext);

					// execute
					XiaoChenJobHelper.log("<br>----------- xiaochen-job job execute start -----------<br>----------- Param:" + xiaoChenJobContext.getJobParam());

					if (triggerParam.getExecutorTimeout() > 0) {
						// limit timeout
						Thread futureThread = null;
						try {
							FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
								@Override
								public Boolean call() throws Exception {

									// init job context
									XiaoChenJobContext.setXxlJobContext(xiaoChenJobContext);

									handler.execute();
									return true;
								}
							});
							futureThread = new Thread(futureTask);
							futureThread.start();

							Boolean tempResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
						} catch (TimeoutException e) {

							XiaoChenJobHelper.log("<br>----------- xiaochen-job job execute timeout");
							XiaoChenJobHelper.log(e);

							// handle result
							XiaoChenJobHelper.handleTimeout("job execute timeout ");
						} finally {
							futureThread.interrupt();
						}
					} else {
						// just execute
						handler.execute();
					}

					// valid execute handle data
					if (XiaoChenJobContext.getXxlJobContext().getHandleCode() <= 0) {
						XiaoChenJobHelper.handleFail("job handle result lost.");
					} else {
						String tempHandleMsg = XiaoChenJobContext.getXxlJobContext().getHandleMsg();
						tempHandleMsg = (tempHandleMsg!=null&&tempHandleMsg.length()>50000)
								?tempHandleMsg.substring(0, 50000).concat("...")
								:tempHandleMsg;
						XiaoChenJobContext.getXxlJobContext().setHandleMsg(tempHandleMsg);
					}
					XiaoChenJobHelper.log("<br>----------- xiaochen-job job execute end(finish) -----------<br>----------- Result: handleCode="
							+ XiaoChenJobContext.getXxlJobContext().getHandleCode()
							+ ", handleMsg = "
							+ XiaoChenJobContext.getXxlJobContext().getHandleMsg()
					);

				} else {
					if (idleTimes > 30) {
						if(triggerQueue.isEmpty()) {	// avoid concurrent trigger causes jobId-lost
							jobExecutor.removeJobThread(jobId, "excutor idel times over limit.");
						}
					}
				}
			} catch (Throwable e) {
				if (toStop) {
					XiaoChenJobHelper.log("<br>----------- JobThread toStop, stopReason:" + stopReason);
				}

				// handle result
				StringWriter stringWriter = new StringWriter();
				e.printStackTrace(new PrintWriter(stringWriter));
				String errorMsg = stringWriter.toString();

				XiaoChenJobHelper.handleFail(errorMsg);

				XiaoChenJobHelper.log("<br>----------- JobThread Exception:" + errorMsg + "<br>----------- xiaochen-job job execute end(error) -----------");
			} finally {
                if(triggerParam != null) {
					MDC.remove(TRACE_ID);
                    // callback handler info
                    if (!toStop) {
                        // commonm
						ApiResult<String> result = ClientHelper.getClientHelper().getExecutorBizAdmin().callback(new HandleCallbackParam(
                        		triggerParam.getLogId(),
								triggerParam.getLogDateTime(),
								XiaoChenJobContext.getXxlJobContext().getHandleCode(),
								XiaoChenJobContext.getXxlJobContext().getHandleMsg() )

						);
						log.info("**********result:{}",result);
                    } else {
                        // is killed
						ApiResult<String> result = ClientHelper.getClientHelper().getExecutorBizAdmin().callback(new HandleCallbackParam(
                        		triggerParam.getLogId(),
								triggerParam.getLogDateTime(),
								XiaoChenJobContext.HANDLE_CODE_FAIL,
								stopReason + " [job running, killed]" )
						);
						log.info("**********result:{}",result);
                    }
                }
            }
        }

		// callback trigger request in queue
		while(triggerQueue !=null && !triggerQueue.isEmpty()){
			TriggerParam triggerParam = triggerQueue.poll();
			if (triggerParam!=null) {
				// is killed
				ApiResult<String> result = ClientHelper.getClientHelper().getExecutorBizAdmin().callback(new HandleCallbackParam(
						triggerParam.getLogId(),
						triggerParam.getLogDateTime(),
						XiaoChenJobContext.HANDLE_CODE_FAIL,
						stopReason + " [job not executed, in the job queue, killed.]")
				);
				log.info("**********result:{}",result);
			}
		}

		// destroy
		try {
			handler.destroy();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}

		log.info(">>>>>>>>>>> xiaochen-job JobThread stoped, hashCode:{}", Thread.currentThread());
	}
}
