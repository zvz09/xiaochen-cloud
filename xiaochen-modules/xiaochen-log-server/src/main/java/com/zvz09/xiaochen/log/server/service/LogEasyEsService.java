package com.zvz09.xiaochen.log.server.service;

import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.domain.LogQueryBody;
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
public class LogEasyEsService {

    private final LogIndexMapper logIndexMapper;

    public EsPageInfo<LogIndex> page(LogQueryBody logQueryBody) {
        LambdaEsQueryChainWrapper<LogIndex> lambdaChainQuery = buildWrapper(logQueryBody);

        return lambdaChainQuery.page(Math.toIntExact(logQueryBody.getPageNum()), Math.toIntExact(logQueryBody.getPageSize()));
    }

    private LambdaEsQueryChainWrapper<LogIndex> buildWrapper(LogQueryBody logQueryBody) {
        LambdaEsQueryChainWrapper<LogIndex> lambdaChainQuery =
                EsWrappers.lambdaChainQuery(logIndexMapper);

        lambdaChainQuery.eq(StringUtils.isNotBlank(logQueryBody.getLevel()), LogIndex::getLevel, logQueryBody.getLevel());
        lambdaChainQuery.eq(StringUtils.isNotBlank(logQueryBody.getApplicationName()), LogIndex::getApplicationName, logQueryBody.getApplicationName());
        lambdaChainQuery.eq(StringUtils.isNotBlank(logQueryBody.getTraceId()), LogIndex::getTraceId, logQueryBody.getTraceId());
        lambdaChainQuery.eq(StringUtils.isNotBlank(logQueryBody.getHost()), LogIndex::getHost, logQueryBody.getHost());
        lambdaChainQuery.like(StringUtils.isNotBlank(logQueryBody.getMessage()), LogIndex::getMessage, logQueryBody.getMessage());

        if (StringUtils.isNotBlank(logQueryBody.getBegin()) && StringUtils.isNotBlank(logQueryBody.getEnd())) {
            lambdaChainQuery.ge(LogIndex::getTimestamp, DateUtils.convertToUtc(DateUtils.convertStringToDate(logQueryBody.getBegin())));
            lambdaChainQuery.le(LogIndex::getTimestamp, DateUtils.convertToUtc(DateUtils.convertStringToDate(logQueryBody.getEnd())));
        }

        if (StringUtils.isNotBlank(logQueryBody.getApplicationName())) {
            lambdaChainQuery.index(Constants.LOG_INDEX_PREFIX + logQueryBody.getApplicationName() + "*");
        } else {
            lambdaChainQuery.index(Constants.LOG_INDEX_PREFIX + "*");
        }
        lambdaChainQuery.orderByDesc(LogIndex::getTimestamp);
        return lambdaChainQuery;
    }
}
