package org.casbin.watcher.lettuce.constants;

/**
 * @author Shingmo Yeung
 */
public class WatcherConstant {
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
}
