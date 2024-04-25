package com.zvz09.xiaochen.k8s.manage.function;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;

/**
 * @author Administrator
 */
@FunctionalInterface
public interface ApiClientFunction<T> {
    KubernetesObject apply(ApiClient apiClient, T resource) throws ApiException;
}
