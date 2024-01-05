# 一、搭建 NFS 服务器

### 1、背景介绍
Kubernetes 对 Pod 进行调度时，以当时集群中各节点的可用资源作为主要依据，自动选择某一个可用的节点，并将 Pod 分配到该节点上。在这种情况下，Pod 中容器数据的持久化如果存储在所在节点的磁盘上，就会产生不可预知的问题，例如，当 Pod 出现故障，Kubernetes 重新调度之后，Pod 所在的新节点上，并不存在上一次 Pod 运行时所在节点上的数据。
为了使 Pod 在任何节点上都能够使用同一份持久化存储数据，我们需要使用网络存储的解决方案为 Pod 提供数据卷。常用的网络存储方案有：NFS/cephfs/glusterfs。
本文介绍一种使用 centos 搭建 nfs 服务器的方法。此方法仅用于测试目的，请根据您生产环境的实际情况，选择合适的 NFS 服务。
### 2、安装NFS服务器
```shell
# 停止并禁用防火墙
systemctl stop firewalld
systemctl disable firewalld
# 关闭并禁用SELinux
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=disabled/' /etc/selinux/config
# 安装nfs-utils和rpcbind
apt install -y nfs-utils rpcbind

# 创建文件夹
mkdir /nfs
# 更改归属组与用户
chown -R nfsnobody:nfsnobody /nfs

```
### 3、配置NFS
执行命令 ```shell vim /etc/exports```，创建 exports 文件，文件内容如下：
```shell
# 编辑exports
vi /etc/exports
# 输入以下内容(格式：FS共享的目录 NFS客户端地址1(参数1,参数2,...) 客户端地址2(参数1,参数2,...))
/nfs 172.16.106.215/24(rw,sync,no_root_squash,anonuid=666,anongid=666)
```
如果设置为 ```shell /nfs *(rw,async,no_root_squash)```  则对所以的IP都有效
常用选项：
- ro：客户端挂载后，其权限为只读，默认选项；
- rw:读写权限；
- sync：同时将数据写入到内存与硬盘中；
- async：异步，优先将数据保存到内存，然后再写入硬盘；
- Secure：要求请求源的端口小于1024
用户映射：
- root_squash:当NFS客户端使用root用户访问时，映射到NFS服务器的匿名用户；
- no_root_squash:当NFS客户端使用root用户访问时，映射到NFS服务器的root用户；
- all_squash:全部用户都映射为服务器端的匿名用户；
- anonuid=UID：将客户端登录用户映射为此处指定的用户uid；
- anongid=GID：将客户端登录用户映射为此处指定的用户gi
### 4、设置开机启动并启动
```shell
systemctl restart rpcbind
systemctl enable nfs && systemctl restart nfs

# 查看是否有可用的NFS地址
showmount -e 127.0.0.1
```
### 5、客户端配置
```shell
# 安装nfs-utils和rpcbind
apt install -y nfs-utils rpcbind
# 创建挂载的文件夹
mkdir -p /nfs-data
# 执行以下命令挂载 nfs 服务器上的共享目录到本机路径/nfs-data
mount -t nfs -o nolock,vers=4 172.16.106.205:/nfs /nfs-data
```
参数解释：
- mount：挂载命令
- o：挂载选项
- nfs :使用的协议
- nolock :不阻塞
- vers : 使用的NFS版本号
- IP : NFS服务器的IP（NFS服务器运行在哪个系统上，就是哪个系统的IP）
- /nfs: 要挂载的目录（Ubuntu的目录）
- /nfs-data : 要挂载到的目录（开发板上的目录，注意挂载成功后，/mnt下原有数据将会被隐藏，无法找到）
```shell
# 查看挂载：
df -h
# 卸载挂载：
umount /nfs-data
# 检查 nfs 服务器端是否有设置共享目录

# showmount -e $(nfs服务器的IP)
showmount -e 172.16.106.205

# 输出结果如下所示
Export list for 172.16.106.205:
/nfs *
# 查看nfs版本
# 查看nfs服务端信息(服务端执行)
nfsstat -s

# 查看nfs客户端信息（客户端执行）
nfsstat -c
```
### 6、写入一个测试文件
```shell
# 创建一个测试文件
echo "hello nfs server" > /nfs-data/test.txt
# 在 nfs 服务器上执行以下命令，验证文件写入成功：
cat /nfs/test.txt
```
### 7、部署 NFS-Subdir-External-Provisioner 提供动态分配卷
NFS-Subdir-External-Provisioner是一个自动配置卷程序，它使用现有的和已配置的 NFS 服务器来支持通过持久卷声明动态配置 Kubernetes 持久卷。
持久卷被配置为：${namespace}-${pvcName}-${pvName}。
此组件是对 nfs-client-provisioner 的扩展，nfs-client-provisioner 已经不提供更新，且 nfs-client-provisioner 的 Github 仓库已经迁移到 NFS-Subdir-External-Provisioner 的仓库。
GitHub 地址：https://github.com/kubernetes-sigs/nfs-subdir-external-provisioner
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nfs-client-provisioner
  namespace: default # 替换成你要部署的 Namespace
---
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-client-provisioner-runner
rules:
  - apiGroups: [""]
    resources: ["persistentvolumes"]
    verbs: ["get", "list", "watch", "create", "delete"]
  - apiGroups: [""]
    resources: ["persistentvolumeclaims"]
    verbs: ["get", "list", "watch", "update"]
  - apiGroups: ["storage.k8s.io"]
    resources: ["storageclasses"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["events"]
    verbs: ["create", "update", "patch"]
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: run-nfs-client-provisioner
subjects:
  - kind: ServiceAccount
    name: nfs-client-provisioner
    namespace: default
roleRef:
  kind: ClusterRole
  name: nfs-client-provisioner-runner
  apiGroup: rbac.authorization.k8s.io
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: leader-locking-nfs-client-provisioner
  namespace: default
rules:
  - apiGroups: [""]
    resources: ["endpoints"]
    verbs: ["get", "list", "watch", "create", "update", "patch"]
---
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: leader-locking-nfs-client-provisioner
  namespace: default
subjects:
  - kind: ServiceAccount
    name: nfs-client-provisioner
    namespace: default
roleRef:
  kind: Role
  name: leader-locking-nfs-client-provisioner
  apiGroup: rbac.authorization.k8s.io
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nfs-client-provisioner
  labels:
    app: nfs-client-provisioner
spec:
  replicas: 1
  strategy:
    type: Recreate                   # 设置升级策略为删除再创建(默认为滚动更新)
  selector:
    matchLabels:
      app: nfs-client-provisioner
  template:
    metadata:
      labels:
        app: nfs-client-provisioner
    spec:
      serviceAccountName: nfs-client-provisioner
      containers:
        - name: nfs-client-provisioner
          #image: gcr.io/k8s-staging-sig-storage/nfs-subdir-external-provisioner:v4.0.0
          #image: unopsman/nfs-subdir-external-provisioner:v4.0.2   # 实测可下载
          image: registry.cn-beijing.aliyuncs.com/xngczl/nfs-subdir-external-provisione:v4.0.0
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME     # Provisioner的名称,以后设置的storageclass要和这个保持一致
              value: nfs-client
            - name: NFS_SERVER           # NFS服务器地址,需和valumes参数中配置的保持一致
              value: 172.16.106.205
            - name: NFS_PATH             # NFS服务器数据存储目录,需和valumes参数中配置的保持一致
              value: /nfs/data
      volumes:
        - name: nfs-client-root
          nfs:
            server: 172.16.106.205     # NFS服务器地址
            path: /nfs/data            # NFS服务器数据存储目录
---
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: nfs-storage
  annotations:
    storageclass.kubernetes.io/is-default-class: "false"  # 是否设置为默认的storageclass
provisioner: nfs-client                                   # 动态卷分配者名称，必须和上面创建的"provisioner"变量中设置的Name一致
parameters:
  archiveOnDelete: "true"                                 # 设置为"false"时删除PVC不会保留数据,"true"则保留数据
mountOptions:
  - hard                                                  # 指定为硬挂载方式
  - nfsvers=4                                             # 指定NFS版本,这个需要根据NFS Server版本号设置
```

### 8、创建 PVC 和 Pod 进行测试
在 Namespace 下创建一个测试用的 PVC 并观察是否自动创建是 PV 与其绑定。
```yaml
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: test-pvc
spec:
  storageClassName: nfs-storage #---需要与上面创建的storageclass的名称一致
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Mi
---
kind: Pod
apiVersion: v1
metadata:
  name: test-pod
spec:
  containers:
    - name: test-pod
      image: busybox:latest
      command:
        - "/bin/sh"
      args:
        - "-c"
        - "touch /mnt/SUCCESS && exit 0 || exit 1"  #创建一个名称为"SUCCESS"的文件
      volumeMounts:
        - name: nfs-pvc
          mountPath: "/mnt"
  restartPolicy: "Never"
  volumes:
    - name: nfs-pvc
      persistentVolumeClaim:
        claimName: test-pvc
```

### 9、进入 NFS 服务器验证是否创建对应文件
进入 NFS 服务器的 NFS 挂载目录，查看是否存在 Pod 中创建的文件：
```shell
[root@nfs ~]# cd /nfs/data/
[root@nfs data]# ll
总用量 4
drwxrwxrwx 2 root root 4096 10月 17 12:29 default-test-pvc-pvc-e85690ac-311f-46fa-af88-9501e35f8582
[root@nfs data]# cd default-test-pvc-pvc-e85690ac-311f-46fa-af88-9501e35f8582/
[root@nfs default-test-pvc-pvc-e85690ac-311f-46fa-af88-9501e35f8582]# ll
总用量 0
-rw-r--r-- 1 root root 0 10月 17 12:29 SUCCESS
[root@nfs default-test-pvc-pvc-e85690ac-311f-46fa-af88-9501e35f8582]#
```
可以看到已经生成 SUCCESS 该文件，并且可知通过 NFS Provisioner 创建的目录命名方式为“namespace名称-pvc名称-pv名称”，pv 名称是随机字符串，所以每次只要不删除 PVC，那么 Kubernetes 中的与存储绑定将不会丢失，要是删除 PVC 也就意味着删除了绑定的文件夹，下次就算重新创建相同名称的 PVC，生成的文件夹名称也不会一致，因为 PV 名是随机生成的字符串，而文件夹命名又跟 PV 有关,所以删除 PVC 需谨慎。