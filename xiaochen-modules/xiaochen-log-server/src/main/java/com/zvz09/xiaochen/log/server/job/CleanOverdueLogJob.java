package com.zvz09.xiaochen.log.server.job;

import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import com.zvz09.xiaochen.log.server.mapper.LogIndexMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CleanOverdueLogJob {

    private final LogIndexMapper logIndexMapper;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @XiaoChenJob("cleanOverdueLog")
    public void cleanOverdueLog() {
        log.info("*****************************Clean overdue log job start**************************************");
        // 获取当前日期时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 计算180天前的日期时间
        LocalDateTime dateTime180DaysAgo = currentDateTime.minusDays(180);

        String formattedDate = dateTime180DaysAgo.format(dateFormatter);

        GetIndexResponse indexResponse = logIndexMapper.getIndex(String.format("%s*%s",Constants.LOG_INDEX_PREFIX,formattedDate));
        if(indexResponse!=null){
           Set<String> indexSet = indexResponse.getAliases().keySet();
            indexSet.forEach(logIndexMapper::deleteIndex);
        }
        log.info("*****************************Clean overdue log job end **************************************");
    }
}
