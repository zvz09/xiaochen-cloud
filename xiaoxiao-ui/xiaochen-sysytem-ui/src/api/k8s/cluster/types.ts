export namespace K8sCluster {
  // 参数接口
  export interface CreateParams {
    /*集群名称 */
    clusterName: string;

    /*apiServer url */
    apiServer: string;

    /*token */
    token?: string;

    /*kubeconfig */
    kubeconfig?: string;
  }

  export interface Cluster {
    id: string;
    createdAt: string;
    updatedAt: string;
    deleted: boolean;
    clusterName: string;
    apiServer: string;
    version: string;
  }

  export interface DialogProps {
    clusterName: string;
  }
}
