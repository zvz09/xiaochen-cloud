package com.zvz09.xiaochen.casbin.config;

import com.zvz09.xiaochen.casbin.config.properties.CasbinProperties;
import lombok.extern.slf4j.Slf4j;
import org.casbin.adapter.RedisAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.main.SyncedEnforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.Watcher;
import org.casbin.watcher.lettuce.LettuceRedisWatcher;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author zvz09
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CasbinProperties.class, RedisProperties.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class CasbinConfig {

    @Bean
    public Adapter autoConfigRedisAdapter(CasbinProperties casbinProperties, RedisProperties redisProperties) {
        return new RedisAdapter(redisProperties.getHost(), redisProperties.getPort(), redisProperties.getPassword());
    }

    @Bean
    public Enforcer enforcer(CasbinProperties properties, Adapter adapter) {
        Model model = new Model();
        // request definition
        model.addDef("r", "r", "sub, obj, act");
        // policy definition
        model.addDef("p", "p", "sub, obj, act");
        // role definition
        model.addDef("g", "g", "_, _");
        // policy effect
        model.addDef("e", "e", "some(where (p.eft == allow))");
        // matchers
        model.addDef("m", "m", "(g(r.sub, p.sub) && r.obj == p.obj && r.act == p.act) || r.sub == \"888\"");

        Enforcer enforcer;
        if (properties.isUseSyncedEnforcer()) {
            enforcer = new SyncedEnforcer(model, adapter);
            log.info("Casbin use SyncedEnforcer");
        } else {
            enforcer = new Enforcer(model, adapter);
        }
        enforcer.enableAutoSave(properties.isAutoSave());
        return enforcer;
    }

    @Bean
    public Watcher redisWatcher(RedisProperties redisProperties, CasbinProperties casbinProperties, Enforcer enforcer) {
        int timeout = redisProperties.getTimeout() != null ? (int) redisProperties.getTimeout().toMillis() : 2000;
        LettuceRedisWatcher watcher = new LettuceRedisWatcher(redisProperties.getHost(), redisProperties.getPort(),
                casbinProperties.getPolicyTopic(), timeout, redisProperties.getPassword());
        enforcer.setWatcher(watcher);
        log.info("Casbin set watcher: {}", watcher.getClass().getName());
        return watcher;
    }
}
