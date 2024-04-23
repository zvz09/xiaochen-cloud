package com.zvz09.xiaochen.mc.component.provider.volcengine.function;

import com.volcengine.ApiException;
import com.volcengine.model.AbstractResponse;

@FunctionalInterface
public interface VolcengineApiFunction<ApiClient> {
    AbstractResponse apply(ApiClient client) throws ApiException;
}
