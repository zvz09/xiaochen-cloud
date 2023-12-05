package com.zvz09.xiaochen.job.admin.listener;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.zvz09.xiaochen.job.admin.service.ServeInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InstancesChangeListener extends Subscriber<InstancesChangeEvent> {

     private final ServeInstanceService serveInstanceService;
    @Override
    public void onEvent(InstancesChangeEvent event) {
        log.info(event.toString());
        serveInstanceService.update(event.getServiceName(),event.getHosts());
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }

}
