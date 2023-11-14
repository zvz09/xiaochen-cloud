package org.casbin.adapter.constants;

/**
 * @author Shingmo Yeung
 */
public class Constant {
    /**
     * Redis Type
     */
    public static final String LETTUCE_REDIS_TYPE_STANDALONE = "standalone";
    public static final String LETTUCE_REDIS_TYPE_CLUSTER = "cluster";

    /**
     * Redis URI
     */
    public static final String REDIS_URI_PREFIX = "redis://";
    public static final String REDIS_URI_PASSWORD_SPLIT = "@";

    public static final String DEFAULT_ADAPTER_KEY = "casbin_rule";
}
