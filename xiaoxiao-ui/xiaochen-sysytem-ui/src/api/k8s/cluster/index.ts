import http from "@/config/axios";
import { K8S_SERVICE_PATH } from "@/api/config/servicePort";
import { K8sCluster } from "@/api/k8s/cluster/types";
import { V1NamespaceList } from "@kubernetes/client-node";

/**
 * 列表
 * @returns
 */
export const listK8sCluster = () => {
  return http.get<K8sCluster.Cluster[]>(K8S_SERVICE_PATH + `/cluster`);
};

/**
 * 新增cluster
 * @param {object} params ClusterDTO
 * @param {string} params.clusterName 集群名称
 * @param {string} params.apiServer apiServer url
 * @param {string} params.token token
 * @param {string} params.kubeconfig kubeconfig
 * @returns
 */
export const addK8sCluster = (params: K8sCluster.CreateParams) => {
  return http.post(K8S_SERVICE_PATH + `/cluster`, params);
};

/**
 * 删除cluster
 * @param {string} id id
 * @returns
 */
export const deleteK8sCluster = (id: number) => {
  return http.delete(K8S_SERVICE_PATH + `/cluster?id=${id}`);
};

export const listAllNameSpaces = (clusterName: string, continueStr: string) => {
  return http.get<V1NamespaceList>(
    K8S_SERVICE_PATH + `/proxy/${clusterName}/api/v1/namespaces?limit=500&continue=${continueStr}`
  );
};
