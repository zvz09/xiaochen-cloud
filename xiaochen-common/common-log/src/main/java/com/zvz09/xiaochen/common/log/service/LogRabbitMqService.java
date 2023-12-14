package com.zvz09.xiaochen.common.log.service;

import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.log.config.LogRabbitMqConfig;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogRabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    @Async
    public void sendLog(OperationLog operationLog) {
        rabbitTemplate.convertAndSend(LogRabbitMqConfig.EXCHANGE_TOPICS, LogRabbitMqConfig.ROUTING_KEY_LOG, JacksonUtil.writeValueAsString(operationLog));
    }


}
