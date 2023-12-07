package com.zvz09.xiaochen.log.server.job;

import co.elastic.clients.elasticsearch.indices.IndexState;
import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import com.zvz09.xiaochen.log.server.domain.LogIndex;
import com.zvz09.xiaochen.log.server.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CleanOverdueLogJob {

    private final ElasticsearchService<LogIndex> elasticsearchService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @XiaoChenJob("cleanOverdueLog")
    public void cleanOverdueLog() {
        log.info("*****************************Clean overdue log job start**************************************");
        // 获取当前日期时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 计算180天前的日期时间
        LocalDateTime dateTime180DaysAgo = currentDateTime.minusDays(180);

        String formattedDate = dateTime180DaysAgo.format(dateFormatter);


        Map<String, IndexState> result = elasticsearchService.index.query(String.format("%s*%s", Constants.LOG_INDEX_PREFIX,formattedDate));

        if(result!=null){
            result.keySet().forEach(elasticsearchService.index::delete);
        }

        log.info("*****************************Clean overdue log job end **************************************");
    }
}
