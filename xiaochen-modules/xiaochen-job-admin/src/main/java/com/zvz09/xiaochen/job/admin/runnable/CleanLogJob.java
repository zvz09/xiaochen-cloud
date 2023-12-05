package com.zvz09.xiaochen.job.admin.runnable;

import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lizili-YF0033
 */
@Component
@RequiredArgsConstructor
public class CleanLogJob {

    private final IJobLogService jobLogService;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void run()  {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(180);
        jobLogService.deleteByTime(dateTime);
    }
}
