package com.zvz09.xiaochen.job.admin.route;


import com.zvz09.xiaochen.job.admin.route.strategy.ExecutorRouteFirst;
import com.zvz09.xiaochen.job.admin.route.strategy.ExecutorRouteLast;
import com.zvz09.xiaochen.job.admin.route.strategy.ExecutorRouteRandom;
import com.zvz09.xiaochen.job.admin.route.strategy.ExecutorRouteRound;
import lombok.Getter;

/**
 * @author zvz09
 */

@Getter
public enum ExecutorRouteStrategyEnum {

    FIRST("第一个", new ExecutorRouteFirst()),
    LAST("最后一个", new ExecutorRouteLast()),
    ROUND("轮询", new ExecutorRouteRound()),
    RANDOM("随机", new ExecutorRouteRandom()),
    BROADCAST("广播", null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private final String title;
    private final ExecutorRouter router;

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
