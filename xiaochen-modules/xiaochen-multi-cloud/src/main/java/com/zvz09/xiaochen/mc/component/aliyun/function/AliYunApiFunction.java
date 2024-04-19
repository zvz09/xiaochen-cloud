package com.zvz09.xiaochen.mc.component.aliyun.function;

import com.aliyun.sdk.gateway.pop.models.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface AliYunApiFunction<C> {
    CompletableFuture<Response> apply(C client) throws ExecutionException, InterruptedException;
}
