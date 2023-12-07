package com.zvz09.xiaochen.log.server.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonData;
import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.common.log.constants.LogConstant;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.domain.LogQueryBody;
import com.zvz09.xiaochen.log.server.domain.dto.EsPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogEsService {

    private final ElasticsearchService<LogIndex> elasticsearchService;

    public EsPage<LogIndex> page(LogQueryBody queryBody) {
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
        searchRequestBuilder.index(LogConstant.LOG_INDEX);
        searchRequestBuilder.from(Math.toIntExact((queryBody.getPageNum() - 1) * queryBody.getPageSize()));
        searchRequestBuilder.size(Math.toIntExact(queryBody.getPageSize()));
        List<Query> queryList = new ArrayList<>();
        Query.Builder queryBuilder = new Query.Builder();
        if(StringUtils.isNotBlank(queryBody.getLevel())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("level").query(queryBody.getLevel())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getApplicationName())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("applicationName").query(queryBody.getApplicationName())).build());
            searchRequestBuilder.index(Constants.LOG_INDEX_PREFIX + queryBody.getApplicationName() + "*");
        }
        if(StringUtils.isNotBlank(queryBody.getTraceId())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("traceId").query(queryBody.getTraceId())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getHost())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("host").query(queryBody.getHost())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getMessage())){
            queryList.add(new Query.Builder().match(t -> t.field("message").query(queryBody.getMessage())).build());
        }
        if (StringUtils.isNotBlank(queryBody.getBegin()) && StringUtils.isNotBlank(queryBody.getEnd())) {
            queryList.add(new Query.Builder().range(rangeQuery -> rangeQuery.field("@timestamp").gte(JsonData.of(DateUtils.convertStringToDate(queryBody.getBegin()).getTime()))).build());
            queryList.add(new Query.Builder().range(rangeQuery -> rangeQuery.field("@timestamp").lte(JsonData.of(DateUtils.convertStringToDate(queryBody.getEnd()).getTime()))).build());
        }

        if(!queryList.isEmpty()){
            searchRequestBuilder.query(q ->q.bool(b->b.must(queryList)));
        }

        return elasticsearchService.document.page(searchRequestBuilder.build(), LogIndex.class, t-> t);
    }

}
