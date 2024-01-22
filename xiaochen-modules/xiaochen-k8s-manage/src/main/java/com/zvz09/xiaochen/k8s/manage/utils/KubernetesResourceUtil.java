package com.zvz09.xiaochen.k8s.manage.utils;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.k8s.manage.function.ApiClientFunction;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KubernetesResourceUtil {
    public static <T> void createResource(ApiClient apiClient, ApiClientFunction<T> readFunction, ApiClientFunction<T> createFunction, T resource) {
        try {
            readFunction.apply(apiClient, resource);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                try {
                    createFunction.apply(apiClient, resource);
                } catch (ApiException ex) {
                    handleApiException("创建资源异常", e);
                }
            } else {
                handleApiException("查询资源异常", e);
            }
        }
    }

    public static void handleApiException(String message, ApiException e) {
        log.error(message, e);
        throw new BusinessException(message);
    }

}
