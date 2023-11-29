package com.zvz09.xiaochen.job.admin.config;

import com.zvz09.xiaochen.job.admin.service.IJobInfoService;
import com.zvz09.xiaochen.job.admin.service.IJobLogService;
import com.zvz09.xiaochen.job.admin.service.ServeInstanceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author lizili-YF0033
 */
@Component
@RequiredArgsConstructor
public class JobHelper implements InitializingBean{

    @Getter
    private static JobHelper jobHelper = null;
    @Getter
    private final IJobLogService jobLogService;
    @Getter
    private final IJobInfoService jobInfoService;
    @Getter
    private final ServeInstanceService serveInstanceService;
    @Override
    public void afterPropertiesSet() throws Exception {
        jobHelper = this;
    }
}
