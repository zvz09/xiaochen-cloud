package com.zvz09.xiaochen.job.client.config;


import com.zvz09.xiaochen.job.core.ExecutorBizAdmin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientHelper implements InitializingBean {

    @Getter
    private static ClientHelper clientHelper = null;
    @Getter
    private final ExecutorBizAdmin executorBizAdmin;


    @Override
    public void afterPropertiesSet() throws Exception {
        clientHelper = this;
    }
}
