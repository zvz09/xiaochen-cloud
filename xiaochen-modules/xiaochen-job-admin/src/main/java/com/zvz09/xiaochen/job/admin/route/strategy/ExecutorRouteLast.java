package com.zvz09.xiaochen.job.admin.route.strategy;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.job.admin.route.ExecutorRouter;
import com.zvz09.xiaochen.job.core.model.TriggerParam;

import java.util.List;


/**
 * @author zvz09
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public Instance route(TriggerParam triggerParam, List<Instance> instances) {
        return instances.get(instances.size() -1);
    }
}
