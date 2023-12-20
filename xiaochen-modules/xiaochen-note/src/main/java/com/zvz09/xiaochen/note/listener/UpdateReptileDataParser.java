package com.zvz09.xiaochen.note.listener;

import com.rabbitmq.client.Channel;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.note.config.RabbitMQConfig;
import com.zvz09.xiaochen.note.domain.entity.ReptileClass;
import com.zvz09.xiaochen.note.strategy.ReptileService;
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
       try {
           ReptileClass reptileClass = JacksonUtil.readValue(msg,ReptileClass.class);
           reptileService.loadClassAndRegisterBean(reptileClass);
       }catch (Exception e){
           log.error("addDataParserStrategy error:{}",e.getMessage(),e);
       }
    }

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_REMOVE})
    public void removeDataParserStrategy(String msg, Message message, Channel channel){
        try {
            reptileService.removeBean(msg);
        }catch (Exception e){
            log.error("removeDataParserStrategy error:{}",e.getMessage(),e);
        }
    }

}
