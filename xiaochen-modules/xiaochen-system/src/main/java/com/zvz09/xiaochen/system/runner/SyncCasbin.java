package com.zvz09.xiaochen.system.runner;

import com.zvz09.xiaochen.system.api.domain.entity.CasbinRule;
import com.zvz09.xiaochen.system.service.ICasbinRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lizili-YF0033
 * 启动时 同步数据库中的casbin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncCasbin implements ApplicationRunner {

    private final ICasbinRuleService casbinRuleService;
    private final Enforcer enforcer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载casbin配置..............");
        List<CasbinRule> casbinRuleList = casbinRuleService.list();
        List<List<String>> list = new ArrayList<>();
        casbinRuleList.forEach(casbinRule -> {
            List<String> strings = new ArrayList<>();
            strings.add(casbinRule.getV0());
            strings.add(casbinRule.getV1());
            strings.add(casbinRule.getV2());
            list.add(strings);
        });
        enforcer.addPolicies(list);
        log.info("casbin 配置加载完成..............");
    }

}
