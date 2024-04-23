package com.zvz09.xiaochen.mc.component.provider.aliyun.function;

import com.aliyun.sdk.gateway.pop.models.Response;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface AliYunApiFunction<C> {
    Response apply(C client) throws ExecutionException, InterruptedException;
}
