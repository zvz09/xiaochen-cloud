package com.zvz09.xiaochen.job.admin.route.strategy;


import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.job.admin.route.ExecutorRouter;
import com.zvz09.xiaochen.job.core.model.TriggerParam;

import java.util.List;


/**
 * @author lizili-YF0033
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public Instance route(TriggerParam triggerParam, List<Instance> instances){
        return instances.get(0);
    }

}
