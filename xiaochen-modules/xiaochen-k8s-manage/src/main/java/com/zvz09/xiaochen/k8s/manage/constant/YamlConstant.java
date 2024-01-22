package com.zvz09.xiaochen.k8s.manage.constant;

/**
 * @author Administrator
 */
public class YamlConstant {

    public static final String SA_XIAOCHEN_ADMIN = """
                    apiVersion: v1
                    kind: ServiceAccount
                    metadata:
                      name: xiaochen-admin
                      namespace: kube-system
            """;

    public static final String CRB_XIAOCHEN_ADMIN = """
            apiVersion: rbac.authorization.k8s.io/v1
            kind: ClusterRoleBinding
            metadata:
              name: xiaochen-admin-crb
            roleRef:
              apiGroup: rbac.authorization.k8s.io
              kind: ClusterRole
              name: cluster-admin
            subjects:
            - kind: ServiceAccount
              name: xiaochen-admin
              namespace: kube-system
                """;

    public static final String TOKEN_XIAOCHEN_ADMIN_TOKEN = """
            apiVersion: v1
            kind: Secret
            type: kubernetes.io/service-account-token
            metadata:
              annotations:
                kubernetes.io/service-account.name: xiaochen-admin
              name: xiaochen-admin-token
              namespace: kube-system
                """;
}
