package com.zvz09.xiaochen.log.server.service;

import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.domain.QueryBody;
import com.zvz09.xiaochen.log.server.mapper.LogIndexMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryChainWrapper;
import org.dromara.easyes.core.core.EsWrappers;
import org.springframework.stereotype.Service;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EsService {

    private final LogIndexMapper logIndexMapper;

    public EsPageInfo<LogIndex> page(QueryBody queryBody) {
        LambdaEsQueryChainWrapper<LogIndex> lambdaChainQuery = buildWrapper(queryBody);

        return lambdaChainQuery.page(Math.toIntExact(queryBody.getPageNum()), Math.toIntExact(queryBody.getPageSize()));
    }

    private LambdaEsQueryChainWrapper<LogIndex> buildWrapper(QueryBody queryBody) {
        LambdaEsQueryChainWrapper<LogIndex> lambdaChainQuery =
                EsWrappers.lambdaChainQuery(logIndexMapper);

        lambdaChainQuery.eq(StringUtils.isNotBlank(queryBody.getLevel()), LogIndex::getLevel, queryBody.getLevel());
        lambdaChainQuery.eq(StringUtils.isNotBlank(queryBody.getApplicationName()), LogIndex::getApplicationName, queryBody.getApplicationName());
        lambdaChainQuery.eq(StringUtils.isNotBlank(queryBody.getTraceId()), LogIndex::getTraceId, queryBody.getTraceId());
        lambdaChainQuery.eq(StringUtils.isNotBlank(queryBody.getHost()), LogIndex::getHost, queryBody.getHost());
        lambdaChainQuery.like(StringUtils.isNotBlank(queryBody.getMessage()), LogIndex::getMessage, queryBody.getMessage());

        if (StringUtils.isNotBlank(queryBody.getBegin()) && StringUtils.isNotBlank(queryBody.getEnd())) {
            lambdaChainQuery.ge(LogIndex::getTimestamp, DateUtils.convertToUtc(DateUtils.convertStringToDate(queryBody.getBegin())));
            lambdaChainQuery.le(LogIndex::getTimestamp, DateUtils.convertToUtc(DateUtils.convertStringToDate(queryBody.getEnd())));
        }

        if (StringUtils.isNotBlank(queryBody.getApplicationName())) {
            lambdaChainQuery.index(Constants.LOG_INDEX_PREFIX + queryBody.getApplicationName() + "*");
        } else {
            lambdaChainQuery.index(Constants.LOG_INDEX_PREFIX + "*");
        }
        lambdaChainQuery.orderByDesc(LogIndex::getTimestamp);
        return lambdaChainQuery;
    }
}
