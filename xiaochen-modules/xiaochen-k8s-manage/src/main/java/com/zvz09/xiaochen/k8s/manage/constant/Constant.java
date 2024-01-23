package com.zvz09.xiaochen.k8s.manage.constant;

/**
 * @author Administrator
 */
public class Constant {

    public static final String SA_XIAOCHEN_ADMIN = """
            apiVersion: v1
            kind: ServiceAccount
            metadata:
              name: xiao-admin
              namespace: kube-system
                        """;

    public static final String CRB_XIAOCHEN_ADMIN = """
            apiVersion: rbac.authorization.k8s.io/v1
            kind: ClusterRoleBinding
            metadata:
              name: xiao-admin-crb
            roleRef:
              apiGroup: rbac.authorization.k8s.io
              kind: ClusterRole
              name: cluster-admin
            subjects:
            - kind: ServiceAccount
              name: xiao-admin
              namespace: kube-system
                            """;

    public static final String TOKEN_XIAOCHEN_ADMIN_TOKEN = """
            apiVersion: v1
            kind: Secret
            type: kubernetes.io/service-account-token
            metadata:
              annotations:
                kubernetes.io/service-account.name: xiao-admin
              name: xiao-admin-token
              namespace: kube-system
                            """;
    public static final String PROXY_PREFIX = "/proxy";
}
