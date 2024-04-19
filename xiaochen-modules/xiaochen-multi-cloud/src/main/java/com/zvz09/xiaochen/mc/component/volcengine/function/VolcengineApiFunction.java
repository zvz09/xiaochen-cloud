package com.zvz09.xiaochen.mc.component.volcengine.function;

import com.volcengine.ApiException;
import com.volcengine.model.AbstractResponse;

@FunctionalInterface
public interface VolcengineApiFunction<ApiClient> {
    AbstractResponse apply(ApiClient client) throws ApiException;
}
