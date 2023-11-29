package com.zvz09.xiaochen.log.server.job;

import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Component
public class CleanOverdueLogJob {

    @XiaoChenJob("cleanOverdueLog")
    public void cleanOverdueLog() {
        log.info("*****************************Clean overdue log job start**************************************");
    }
}
