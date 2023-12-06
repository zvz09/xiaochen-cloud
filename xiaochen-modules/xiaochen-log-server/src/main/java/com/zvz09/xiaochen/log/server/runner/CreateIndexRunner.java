package com.zvz09.xiaochen.log.server.runner;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.zvz09.xiaochen.common.log.constants.LogConstant;
import com.zvz09.xiaochen.log.server.service.OperationLogReceiveHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateIndexRunner implements ApplicationRunner {

    private final ElasticsearchClient elasticsearchClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
       try {
           // 请求对象
           ExistsRequest existsRequest =
                   new ExistsRequest.Builder().index(LogConstant.OPERATION_LOG_INDEX).build();
           BooleanResponse booleanResponse =
                   elasticsearchClient.indices().exists(existsRequest);
           if(!booleanResponse.value()){
               elasticsearchClient.indices().create(c -> c
                       .index(LogConstant.OPERATION_LOG_INDEX)
               );
           }
       }catch (Exception e){
           log.error("创建索引失败",e);
       }
    }
}
