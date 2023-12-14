package com.zvz09.xiaochen.blog.listener;

import com.rabbitmq.client.Channel;
import com.zvz09.xiaochen.blog.config.RabbitMQConfig;
import com.zvz09.xiaochen.blog.domain.entity.ReptileClass;
import com.zvz09.xiaochen.blog.strategy.ReptileService;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateReptileDataParser {

    private final ReptileService reptileService;
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_ADD})
    public void addDataParserStrategy(String msg, Message message, Channel channel){
        ReptileClass reptileClass = JacksonUtil.readValue(msg,ReptileClass.class);
        reptileService.loadClass(reptileClass);
    }

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_REMOVE})
    public void removeDataParserStrategy(String msg, Message message, Channel channel){
        reptileService.removeBean(msg);
    }

}
