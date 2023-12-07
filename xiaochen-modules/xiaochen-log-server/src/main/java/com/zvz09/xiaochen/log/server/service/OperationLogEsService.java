package com.zvz09.xiaochen.log.server.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonData;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.common.log.constants.LogConstant;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import com.zvz09.xiaochen.log.server.domain.OperationLogQueryBody;
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
public class OperationLogEsService {

    private final ElasticsearchService<OperationLog> elasticsearchService;

    public EsPage<OperationLog> page(OperationLogQueryBody  queryBody) {
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
        searchRequestBuilder.index(LogConstant.OPERATION_LOG_INDEX);
        searchRequestBuilder.from(Math.toIntExact((queryBody.getPageNum() - 1) * queryBody.getPageSize()));
        searchRequestBuilder.size(Math.toIntExact(queryBody.getPageSize()));
        List<Query> queryList = new ArrayList<>();
        Query.Builder queryBuilder = new Query.Builder();
        if(StringUtils.isNotBlank(queryBody.getOperatorName())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("operatorName").query(queryBody.getOperatorName())).build());
        }
        if (queryBody.getOperatorId() != null){
            queryList.add(new Query.Builder().match(t -> t.field("operatorId").query(queryBody.getOperatorId())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getServiceName())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("serviceName").query(queryBody.getServiceName())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getRequestUrl())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("requestUrl").query(queryBody.getRequestUrl())).build());
        }
        if(StringUtils.isNotBlank(queryBody.getBusinessType())){
            queryList.add(new Query.Builder().matchPhrase(t -> t.field("businessType").query(queryBody.getBusinessType())).build());
        }
        if (StringUtils.isNotBlank(queryBody.getBegin()) && StringUtils.isNotBlank(queryBody.getEnd())) {
            queryList.add(new Query.Builder().range(rangeQuery -> rangeQuery.field("operationTimeStart").gte(JsonData.of(DateUtils.convertStringToDate(queryBody.getBegin()).getTime()))).build());
            queryList.add(new Query.Builder().range(rangeQuery -> rangeQuery.field("operationTimeStart").lte(JsonData.of(DateUtils.convertStringToDate(queryBody.getEnd()).getTime()))).build());
        }
        if(!queryList.isEmpty()){
            searchRequestBuilder.query(q ->q.bool(b->b.must(queryList)));
        }

        return elasticsearchService.document.page(searchRequestBuilder.build(),OperationLog.class, t-> t);
    }

}
