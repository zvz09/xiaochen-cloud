package com.zvz09.xiaochen.note.runner;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.note.domain.entity.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
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
                   new ExistsRequest.Builder().index(Article.class.getAnnotation(TableName.class).value()).build();
           BooleanResponse booleanResponse =
                   elasticsearchClient.indices().exists(existsRequest);
           if(!booleanResponse.value()){
               elasticsearchClient.indices().create(c -> c
                       .index(Article.class.getAnnotation(TableName.class).value())
               );
           }
       }catch (Exception e){
           log.error("创建索引失败",e);
       }
    }
}
