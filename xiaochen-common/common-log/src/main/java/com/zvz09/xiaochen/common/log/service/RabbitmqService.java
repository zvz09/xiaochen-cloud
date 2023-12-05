package com.zvz09.xiaochen.common.log.service;

import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.log.config.RabbitmqConfig;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zvz09
 */
@Service
@RequiredArgsConstructor
public class RabbitmqService {

    private final RabbitTemplate rabbitTemplate;

    @Async
    public void sendLog(OperationLog operationLog) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS, RabbitmqConfig.ROUTING_KEY_LOG, JacksonUtil.writeValueAsString(operationLog));
    }


}
