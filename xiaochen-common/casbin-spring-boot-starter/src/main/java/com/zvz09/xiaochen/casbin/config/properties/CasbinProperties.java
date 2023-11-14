package com.zvz09.xiaochen.casbin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
@ConfigurationProperties("casbin")
public class CasbinProperties {

    private String tableName = "casbin_rule";
    /**
     * Enable Casbin
     */
    private boolean enableCasbin = true;
    /**
     * Whether to use a synchronized Enforcer
     */
    private boolean useSyncedEnforcer = false;
    /**
     * Storage strategy
     */
    private String storeType = "jdbc";

    /**
     * Redis topic for Watcher
     */
    private String policyTopic = "CASBIN_POLICY_TOPIC";
    /**
     * Data table initialization strategy
     */
    private String initializeSchema = "create";
    /**
     * Whether to use Watcher for strategy synchronization
     */
    private boolean enableWatcher = false;
    /**
     * The configuration will only take effect if the adapter supports this function
     * Can be manually switched through enforcer.enableAutoSave(true)
     */
    private boolean autoSave = true;
    /**
     * If the local model file address is not set or the file is not found in the default path
     * Use default rbac configuration
     */
    private boolean useDefaultModelIfModelNotSetting = true;

}
