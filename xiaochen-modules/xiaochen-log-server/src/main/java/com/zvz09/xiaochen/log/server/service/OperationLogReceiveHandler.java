package com.zvz09.xiaochen.log.server.service;


import com.rabbitmq.client.Channel;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.log.config.LogRabbitMqConfig;
import com.zvz09.xiaochen.common.log.constants.LogConstant;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationLogReceiveHandler {

    private final ElasticsearchService<OperationLog> elasticsearchService;

    //监听队列
    @RabbitListener(queues = {LogRabbitMqConfig.QUEUE_INFORM_LOG})
    public void receiveLog(String msg, Message message, Channel channel){
        OperationLog operationLog = JacksonUtil.readValue(msg,OperationLog.class);
        elasticsearchService.document
                .createOrUpdate(LogConstant.OPERATION_LOG_INDEX,operationLog.getTraceId(),operationLog);
    }

}
