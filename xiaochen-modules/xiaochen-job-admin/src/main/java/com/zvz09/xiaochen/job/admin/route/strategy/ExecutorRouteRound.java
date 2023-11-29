package com.zvz09.xiaochen.job.admin.route.strategy;


import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.job.admin.route.ExecutorRouter;
import com.zvz09.xiaochen.job.core.model.TriggerParam;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author lizili-YF0033
 */
public class ExecutorRouteRound extends ExecutorRouter {

    private static final ConcurrentMap<Long, AtomicInteger> ROUTE_COUNT_EACH_JOB = new ConcurrentHashMap<>();
    private static long CACHE_VALID_TIME = 0;

    private static int count(Long jobId) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            ROUTE_COUNT_EACH_JOB.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        AtomicInteger count = ROUTE_COUNT_EACH_JOB.get(jobId);
        if (count == null || count.get() > 1000000) {
            // 初始化时主动Random一次，缓解首次压力
            count = new AtomicInteger(new Random().nextInt(100));
        } else {
            // count++
            count.addAndGet(1);
        }
        ROUTE_COUNT_EACH_JOB.put(jobId, count);
        return count.get();
    }


    @Override
    public Instance route(TriggerParam triggerParam, List<Instance> instances) {
        return instances.get(count(triggerParam.getJobId())%instances.size());
    }
}
