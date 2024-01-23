package com.zvz09.xiaochen.k8s.manage.service;


import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.k8s.manage.constant.Constant;
import com.zvz09.xiaochen.k8s.manage.utils.KubernetesResourceUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.openapi.apis.VersionApi;
import io.kubernetes.client.openapi.models.V1ClusterRoleBinding;
import io.kubernetes.client.openapi.models.V1Secret;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.openapi.models.VersionInfo;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.Objects;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class K8sService {


    public String createToken(String kubeConfigStr) {
        String token = null;
        try {
            ApiClient client = Config.fromConfig(new StringReader(kubeConfigStr));
            CoreV1Api coreV1Api = new CoreV1Api(client);
            RbacAuthorizationV1Api rbacAuthorizationV1Api = new RbacAuthorizationV1Api(client);
            createServiceAccount(coreV1Api, (V1ServiceAccount) Yaml.load(new StringReader(Constant.SA_XIAOCHEN_ADMIN)));

            createClusterRoleBinding(rbacAuthorizationV1Api, (V1ClusterRoleBinding) Yaml.load(new StringReader(Constant.CRB_XIAOCHEN_ADMIN)));

            V1Secret secret = createSecret(coreV1Api, (V1Secret) Yaml.load(new StringReader(Constant.TOKEN_XIAOCHEN_ADMIN_TOKEN)));
            token = new String(secret.getData().get("token"));

        } catch (Exception e) {
            log.error("创建token 异常", e);
            throw new RuntimeException(e);
        }
        return token;
    }

    private static void createServiceAccount(CoreV1Api coreV1Api, V1ServiceAccount serviceAccount) {
        KubernetesResourceUtil.createResource(
                coreV1Api.getApiClient(),
                (apiClient, resource) -> coreV1Api.readNamespacedServiceAccount(serviceAccount.getMetadata().getName(), serviceAccount.getMetadata().getNamespace(), null),
                (apiClient, resource) -> coreV1Api.createNamespacedServiceAccount(serviceAccount.getMetadata().getNamespace(), resource, null, null, null, null),
                serviceAccount
        );
    }

    private void createClusterRoleBinding(RbacAuthorizationV1Api rbacAuthorizationV1Api, V1ClusterRoleBinding clusterRoleBinding) {
        KubernetesResourceUtil.createResource(
                rbacAuthorizationV1Api.getApiClient(),
                (apiClient, resource) -> rbacAuthorizationV1Api.readClusterRoleBinding(resource.getMetadata().getName(), null),
                (apiClient, resource) -> rbacAuthorizationV1Api.createClusterRoleBinding(resource, null, null, null, null),
                clusterRoleBinding
        );
    }

    private V1Secret createSecret(CoreV1Api coreV1Api, V1Secret secret) {
        KubernetesResourceUtil.createResource(
                coreV1Api.getApiClient(),
                (apiClient, resource) -> coreV1Api.readNamespacedSecret(resource.getMetadata().getName(), resource.getMetadata().getNamespace(), null),
                (apiClient, resource) -> coreV1Api.createNamespacedSecret(resource.getMetadata().getNamespace(), resource, null, null, null, null),
                secret
        );
        try {
            return coreV1Api.readNamespacedSecret(Objects.requireNonNull(secret.getMetadata()).getName(), Objects.requireNonNull(secret.getMetadata()).getNamespace(), null);
        } catch (ApiException e) {
            KubernetesResourceUtil.handleApiException("查询secret异常", e);
        }
        return null;
    }

    public String getK8sVersion(String apiServer, String token) {
        return this.getK8sVersion(this.buildClient(apiServer, token));
    }

    public String getK8sVersion(ApiClient apiClient) {
        try {
            Configuration.setDefaultApiClient(apiClient);
            VersionApi api = new VersionApi();
            VersionInfo versionInfo = api.getCode();
            return versionInfo.getGitVersion();
        } catch (Exception e) {
            log.error("获取集群版本号异常：", e);
            throw new BusinessException("获取集群版本号异常");
        }
    }

    public ApiClient buildClient(String apiServer, String token) {
        return new ClientBuilder().setBasePath(apiServer).setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication(token))
                .build();
    }
}
