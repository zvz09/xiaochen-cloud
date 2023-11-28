package com.zvz09.xiaochen.job.admin.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizili-YF0033
 */
@Service
public class ServeInstanceService {

    private final Map<String, List<Instance>> serviceInstances = new HashMap<>();

    public void update(String serviceName, List<Instance> hosts) {
        this.serviceInstances.put(serviceName,hosts);
    }

    public Integer getServiceSize(String serviceName){
        return this.serviceInstances.get(serviceName) == null? 0:this.serviceInstances.get(serviceName).size();
    }
}
