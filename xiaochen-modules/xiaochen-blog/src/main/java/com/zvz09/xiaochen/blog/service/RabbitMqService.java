package com.zvz09.xiaochen.blog.service;

import com.zvz09.xiaochen.blog.config.RabbitMQConfig;
import com.zvz09.xiaochen.blog.domain.entity.ReptileClass;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
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
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    @Async
    public void addDataParserStrategy(ReptileClass reptileClass) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS, RabbitMQConfig.ROUTING_KEY_ADD, JacksonUtil.writeValueAsString(reptileClass));
    }

    @Async
    public void removeDataParserStrategy(String beanName){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS,RabbitMQConfig.ROUTING_KEY_REMOVE, beanName);
    }


}
