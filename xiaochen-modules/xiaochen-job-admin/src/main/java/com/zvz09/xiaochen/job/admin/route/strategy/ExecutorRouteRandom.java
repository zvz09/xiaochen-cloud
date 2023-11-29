package com.zvz09.xiaochen.job.admin.route.strategy;


import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.job.admin.route.ExecutorRouter;
import com.zvz09.xiaochen.job.core.model.TriggerParam;

import java.util.List;
import java.util.Random;


/**
 * @author lizili-YF0033
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static final Random LOCAL_RANDOM = new Random();

    @Override
    public Instance route(TriggerParam triggerParam, List<Instance> instances) {
        return instances.get(LOCAL_RANDOM.nextInt(instances.size()));
    }
}
