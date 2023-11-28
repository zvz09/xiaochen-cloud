package com.zvz09.xiaochen.job.admin.runnable;

import com.zvz09.xiaochen.job.admin.listener.InstancesChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
public class SubscriberServe implements DisposableBean,Runnable {

    private Thread thread;
    private boolean isStop = false;

    private final DiscoveryClient discoveryClient;

    public SubscriberServe(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.thread = new Thread(this);
        this.thread.start();
        log.info("订阅同命名空间下的所有服务 线程启动");
    }


    @Override
    public void run() {
        while (!isStop) {
            //订阅同命名空间下的所有服务
            for (String service:discoveryClient.getServices()){
                discoveryClient.getInstances(service);
            }
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void destroy() throws Exception {
        isStop = true;
    }
}
