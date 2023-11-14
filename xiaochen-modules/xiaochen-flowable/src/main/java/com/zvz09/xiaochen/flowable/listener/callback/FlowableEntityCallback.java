package com.zvz09.xiaochen.flowable.listener.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowableEntityCallback {


    @Async
    public void initializedCall(FlowableEngineEntityEvent event) {
        log.info("entityInitialized:{}", event.getEntity().toString());
        if (event.getEntity() instanceof IdentityLinkEntity identityLinkEntity) {
            String id = identityLinkEntity.getProcessInstanceId();
            switch (identityLinkEntity.getType()) {
                case IdentityLinkType.STARTER:
                    break;
                case IdentityLinkType.CANDIDATE:

                    break;
                case IdentityLinkType.PARTICIPANT:

                    break;
                default:
                    break;
            }

        }
    }
}
