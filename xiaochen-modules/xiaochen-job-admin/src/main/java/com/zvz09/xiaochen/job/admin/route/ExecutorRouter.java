package com.zvz09.xiaochen.job.admin.route;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zvz09.xiaochen.job.core.model.TriggerParam;

import java.util.List;


/**
 * @author lizili-YF0033
 */
public abstract class ExecutorRouter {

    /**
     * route address
     * @return  ReturnT.content=address
     */
    public abstract Instance route(TriggerParam triggerParam, List<Instance> instances);

}
