package com.zvz09.xiaochen.log.server.service;


import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import com.rabbitmq.client.Channel;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import com.zvz09.xiaochen.common.log.config.RabbitmqConfig;
import com.zvz09.xiaochen.common.log.constants.LogConstant;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;


/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationLogReceiveHandler {

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    //监听队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_LOG})
    public void receiveLog(String msg, Message message, Channel channel){
        OperationLog operationLog = JacksonUtil.readValue(msg,OperationLog.class);
        elasticsearchAsyncClient.index(i -> i.index(LogConstant.OPERATION_LOG_INDEX)
                .id(operationLog.getTraceId())
                .document(operationLog));
    }

}
