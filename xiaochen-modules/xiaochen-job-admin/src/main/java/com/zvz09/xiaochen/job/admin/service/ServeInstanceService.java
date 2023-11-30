package com.zvz09.xiaochen.job.admin.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizili-YF0033
 */
@Service
@RequiredArgsConstructor
public class ServeInstanceService {

    private final DiscoveryClient discoveryClient;
    private final Map<String, List<Instance>> serviceInstances = new HashMap<>();

    public void update(String serviceName, List<Instance> hosts) {
        this.serviceInstances.put(serviceName,hosts);
    }

    public Integer getServiceSize(String serviceName){
        if(this.serviceInstances.get(serviceName)== null){
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            if(serviceInstances == null){
                return 0;
            }else {
                completion(serviceName, serviceInstances);
                return serviceInstances.size();
            }
        }else {
            return this.serviceInstances.get(serviceName).size();
        }
    }

    public List<Instance> getServiceInstances(String serviceName){
        List<Instance> instances = this.serviceInstances.get(serviceName);
        if(instances == null){
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            if(serviceInstances == null){
                return null;
            }else {
                completion(serviceName, serviceInstances);
            }
        }
        return this.serviceInstances.get(serviceName);
    }

    private void completion(String serviceName, List<ServiceInstance> serviceInstances) {
        List<Instance> list = new ArrayList<>(serviceInstances.size());
        for (ServiceInstance serviceInstance :serviceInstances){
            Map<String, String> metadata = serviceInstance.getMetadata();
            Instance instance = new Instance();
            instance.setIp(serviceInstance.getHost());
            instance.setPort(serviceInstance.getPort());
            instance.setWeight(Double.parseDouble(metadata.get("nacos.weight")));
            instance.setHealthy(Boolean.parseBoolean(metadata.get("nacos.healthy")));
            list.add(instance);
        }
        this.serviceInstances.put(serviceName,list);
    }

    public List<String> getAllServiceName(){
        return discoveryClient.getServices();
    }
}
