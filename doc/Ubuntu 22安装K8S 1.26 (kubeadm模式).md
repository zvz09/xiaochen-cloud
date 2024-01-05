## 前言
我用自己的电脑跑了三个虚拟机节点，并希望在这三个节点上安装k8s 1.26版本，部署成一个master两个node的架构来满足最基本的学习；下面我们就来逐步讲解整个安装过程。

## 一 系统环境初始化
### 1.1 系统环境
| 主机名 | IP地址 | 系统版本 | 配置        | 角色     |
|------|--------|----------|-----------|--------|
| master01 | 192.168.1.100 | Ubuntu 22.04.3 LTS| 4C8G 50GB | master |
| node01 | 192.168.1.101 | Ubuntu 22.04.3 LTS| 4C8G 50GB | worker    |
| node02 | 192.168.1.102 | Ubuntu 22.04.3 LTS| 4C8G 50GB | worker    |
### 1.2 修改系统设置
- 检查网络
  在每个节点安装成功后，需要通过`ping`命令检查以下几项：
    1. 是否能够`ping`通`baidu.com`；
    2. 是否能够`ping`通宿主机；
    3. 是否能够`ping`通子网内其他节点；
- 检查时区
  时区不正确的可以通过下面的命令来修正：
    ```shell
    sudo tzselect
    ```
- 配置ubuntu系统国内源
    ```shell
    # 备份系统源
    sudo cp /etc/apt/sources.list /etc/apt/sources.list.bak
    sudo rm -rf /etc/apt/sources.list
    # 创建新的系统源
    sudo vi /etc/apt/sources.list
    # 添加以下内容
    deb http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
    deb-src http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
    deb-src http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
    deb-src http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
    deb-src http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse
    deb-src http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse
  
    # 修改完毕后，需要执行以下命令生效
    sudo apt-get update
    sudo apt-get upgrade
    ```
- 禁用 selinux
  默认ubuntu下没有这个模块，centos下需要禁用selinux；
- 禁用swap
  临时禁用命令：
  ```shell
  sudo swapoff -a
  ```
  永久禁用：
    ```shell
    sudo vi /etc/fstab
    ```
- 
- 修改内核参数
    ```shell
    sudo tee /etc/modules-load.d/containerd.conf <<EOF
    overlay
    br_netfilter
    EOF
    
    sudo modprobe overlay
    sudo modprobe br_netfilter
    ```
    ```shell
    sudo tee /etc/sysctl.d/kubernetes.conf <<EOF
    net.bridge.bridge-nf-call-ip6tables = 1
    net.bridge.bridge-nf-call-iptables = 1
    net.ipv4.ip_forward = 1
    EOF
    ```
  运行以下命令使得上述配置生效：
    ```shell
    sudo sysctl --system
    ```
## 二 安装Docker Engine
```shell
# 卸载旧版本
sudo apt-get remove docker docker-engine docker.io containerd runc
# 更新apt
sudo apt-get update
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
# 添加官方GPG key
sudo mkdir -m 0755 -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
# 配置repository
echo \
 "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
 $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
# 更新apt索引
sudo apt-get update
# 安装Docker Engine
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 配置中国科学技术大学镜像源镜像源  
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
EOF
# 设置开机自启动docker
sudo systemctl enable docker.service
# 重启docker engine
sudo systemctl restart docker.service
```

## 三 修改containerd配置
修改这个配置是关键，否则会因为科学上网的问题导致k8s安装出错；比如后续kubeadm init失败，kubeadm join后节点状态一直处于NotReady状态等问题；
备份默认配置
```shell
sudo mv /etc/containerd/config.toml /etc/containerd/config.toml.bak
```
修改配置
```shell
sudo vi /etc/containerd/config.toml
```
修改内容如下：
```toml
disabled_plugins = []
imports = []
oom_score = 0
plugin_dir = ""
required_plugins = []
root = "/var/lib/containerd"
state = "/run/containerd"
temp = ""
version = 2

[cgroup]
 path = ""

[debug]
 address = ""
 format = ""
 gid = 0
 level = ""
 uid = 0

[grpc]
 address = "/run/containerd/containerd.sock"
 gid = 0
 max_recv_message_size = 16777216
 max_send_message_size = 16777216
 tcp_address = ""
 tcp_tls_ca = ""
 tcp_tls_cert = ""
 tcp_tls_key = ""
 uid = 0

[metrics]
 address = ""
 grpc_histogram = false

[plugins]

 [plugins."io.containerd.gc.v1.scheduler"]
  deletion_threshold = 0
  mutation_threshold = 100
  pause_threshold = 0.02
  schedule_delay = "0s"
  startup_delay = "100ms"

 [plugins."io.containerd.grpc.v1.cri"]
  device_ownership_from_security_context = false
  disable_apparmor = false
  disable_cgroup = false
  disable_hugetlb_controller = true
  disable_proc_mount = false
  disable_tcp_service = true
  enable_selinux = false
  enable_tls_streaming = false
  enable_unprivileged_icmp = false
  enable_unprivileged_ports = false
  ignore_image_defined_volumes = false
  max_concurrent_downloads = 3
  max_container_log_line_size = 16384
  netns_mounts_under_state_dir = false
  restrict_oom_score_adj = false
  sandbox_image = "registry.aliyuncs.com/google_containers/pause:3.9"
  selinux_category_range = 1024
  stats_collect_period = 10
  stream_idle_timeout = "4h0m0s"
  stream_server_address = "127.0.0.1"
  stream_server_port = "0"
  systemd_cgroup = false
  tolerate_missing_hugetlb_controller = true
  unset_seccomp_profile = ""

  [plugins."io.containerd.grpc.v1.cri".cni]
   bin_dir = "/opt/cni/bin"
   conf_dir = "/etc/cni/net.d"
   conf_template = ""
   ip_pref = ""
   max_conf_num = 1

  [plugins."io.containerd.grpc.v1.cri".containerd]
   default_runtime_name = "runc"
   disable_snapshot_annotations = true
   discard_unpacked_layers = false
   ignore_rdt_not_enabled_errors = false
   no_pivot = false
   snapshotter = "overlayfs"

   [plugins."io.containerd.grpc.v1.cri".containerd.default_runtime]
    base_runtime_spec = ""
    cni_conf_dir = ""
    cni_max_conf_num = 0
    container_annotations = []
    pod_annotations = []
    privileged_without_host_devices = false
    runtime_engine = ""
    runtime_path = ""
    runtime_root = ""
    runtime_type = ""

    [plugins."io.containerd.grpc.v1.cri".containerd.default_runtime.options]

   [plugins."io.containerd.grpc.v1.cri".containerd.runtimes]

    [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
     base_runtime_spec = ""
     cni_conf_dir = ""
     cni_max_conf_num = 0
     container_annotations = []
     pod_annotations = []
     privileged_without_host_devices = false
     runtime_engine = ""
     runtime_path = ""
     runtime_root = ""
     runtime_type = "io.containerd.runc.v2"

     [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
      BinaryName = ""
      CriuImagePath = ""
      CriuPath = ""
      CriuWorkPath = ""
      IoGid = 0
      IoUid = 0
      NoNewKeyring = false
      NoPivotRoot = false
      Root = ""
      ShimCgroup = ""
      SystemdCgroup = true

   [plugins."io.containerd.grpc.v1.cri".containerd.untrusted_workload_runtime]
    base_runtime_spec = ""
    cni_conf_dir = ""
    cni_max_conf_num = 0
    container_annotations = []
    pod_annotations = []
    privileged_without_host_devices = false
    runtime_engine = ""
    runtime_path = ""
    runtime_root = ""
    runtime_type = ""

    [plugins."io.containerd.grpc.v1.cri".containerd.untrusted_workload_runtime.options]

  [plugins."io.containerd.grpc.v1.cri".image_decryption]
   key_model = "node"

  [plugins."io.containerd.grpc.v1.cri".registry]
   config_path = ""

   [plugins."io.containerd.grpc.v1.cri".registry.auths]

   [plugins."io.containerd.grpc.v1.cri".registry.configs]

   [plugins."io.containerd.grpc.v1.cri".registry.headers]

   [plugins."io.containerd.grpc.v1.cri".registry.mirrors]

  [plugins."io.containerd.grpc.v1.cri".x509_key_pair_streaming]
   tls_cert_file = ""
   tls_key_file = ""

 [plugins."io.containerd.internal.v1.opt"]
  path = "/opt/containerd"

 [plugins."io.containerd.internal.v1.restart"]
  interval = "10s"

 [plugins."io.containerd.internal.v1.tracing"]
  sampling_ratio = 1.0
  service_name = "containerd"

 [plugins."io.containerd.metadata.v1.bolt"]
  content_sharing_policy = "shared"

 [plugins."io.containerd.monitor.v1.cgroups"]
  no_prometheus = false

 [plugins."io.containerd.runtime.v1.linux"]
  no_shim = false
  runtime = "runc"
  runtime_root = ""
  shim = "containerd-shim"
  shim_debug = false

 [plugins."io.containerd.runtime.v2.task"]
  platforms = ["linux/amd64"]
  sched_core = false

 [plugins."io.containerd.service.v1.diff-service"]
  default = ["walking"]

 [plugins."io.containerd.service.v1.tasks-service"]
  rdt_config_file = ""

 [plugins."io.containerd.snapshotter.v1.aufs"]
  root_path = ""

 [plugins."io.containerd.snapshotter.v1.btrfs"]
  root_path = ""

 [plugins."io.containerd.snapshotter.v1.devmapper"]
  async_remove = false
  base_image_size = ""
  discard_blocks = false
  fs_options = ""
  fs_type = ""
  pool_name = ""
  root_path = ""

 [plugins."io.containerd.snapshotter.v1.native"]
  root_path = ""

 [plugins."io.containerd.snapshotter.v1.overlayfs"]
  root_path = ""
  upperdir_label = false

 [plugins."io.containerd.snapshotter.v1.zfs"]
  root_path = ""

 [plugins."io.containerd.tracing.processor.v1.otlp"]
  endpoint = ""
  insecure = false
  protocol = ""

[proxy_plugins]

[stream_processors]

 [stream_processors."io.containerd.ocicrypt.decoder.v1.tar"]
  accepts = ["application/vnd.oci.image.layer.v1.tar+encrypted"]
  args = ["--decryption-keys-path", "/etc/containerd/ocicrypt/keys"]
  env = ["OCICRYPT_KEYPROVIDER_CONFIG=/etc/containerd/ocicrypt/ocicrypt_keyprovider.conf"]
  path = "ctd-decoder"
  returns = "application/vnd.oci.image.layer.v1.tar"

 [stream_processors."io.containerd.ocicrypt.decoder.v1.tar.gzip"]
  accepts = ["application/vnd.oci.image.layer.v1.tar+gzip+encrypted"]
  args = ["--decryption-keys-path", "/etc/containerd/ocicrypt/keys"]
  env = ["OCICRYPT_KEYPROVIDER_CONFIG=/etc/containerd/ocicrypt/ocicrypt_keyprovider.conf"]
  path = "ctd-decoder"
  returns = "application/vnd.oci.image.layer.v1.tar+gzip"

[timeouts]
 "io.containerd.timeout.bolt.open" = "0s"
 "io.containerd.timeout.shim.cleanup" = "5s"
 "io.containerd.timeout.shim.load" = "5s"
 "io.containerd.timeout.shim.shutdown" = "3s"
 "io.containerd.timeout.task.state" = "2s"

[ttrpc]
 address = ""
 gid = 0
 uid = 0
```
重启containerd服务
```shell
sudo systemctl enable containerd
sudo systemctl daemon-reload && systemctl restart containerd
```
### 四 安装k8s组件

```shell
# 添加k8s的阿里云yum源
curl -s https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add -
sudo apt-add-repository "deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main"
sudo apt-get update
# 安装k8s组件
sudo apt update
sudo apt install -y kubelet=1.26.1-00 kubeadm=1.26.1-00 kubectl=1.26.1-00
sudo apt-mark hold kubelet kubeadm kubectl
# 配置k8s镜像列表
sudo kubeadm config images list --kubernetes-version=v1.26.1
# 下载镜像
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/kube-apiserver:v1.26.1
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/kube-controller-manager:v1.26.1
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/kube-scheduler:v1.26.1
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/kube-proxy:v1.26.1
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.9
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/etcd:3.5.6-0
sudo docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/coredns:v1.9.3
```

### 五 初始化master

- 生成kubeadm默认配置文件
    ```shell
    sudo kubeadm config print init-defaults > kubeadm.yaml
    ```
- 修改配置文件
  1.修改localAPIEndpoint.advertiseAddress为master的ip；
  2.修改nodeRegistration.name为当前节点名称；
  3.修改imageRepository为国内源：registry.cn-hangzhou.aliyuncs.com/google_containers
  4.添加networking.podSubnet，该网络ip范围不能与networking.serviceSubnet冲突，也不能与节点网络192.168.56.0/24相冲突；所以我就设置成192.168.66.0/24；
```yaml
apiVersion: kubeadm.k8s.io/v1beta3
bootstrapTokens:
  - groups:
      - system:bootstrappers:kubeadm:default-node-token
    token: abcdef.0123456789abcdef
    ttl: 24h0m0s
    usages:
      - signing
      - authentication
kind: InitConfiguration
localAPIEndpoint:
  advertiseAddress: 192.168.93.129
  bindPort: 6443
nodeRegistration:
  criSocket: unix:///var/run/containerd/containerd.sock
  imagePullPolicy: IfNotPresent
  name: master-1
  taints: null
---
apiServer:
  timeoutForControlPlane: 4m0s
apiVersion: kubeadm.k8s.io/v1beta3
certificatesDir: /etc/kubernetes/pki
clusterName: kubernetes
controllerManager: {}
dns: {}
etcd:
  local:
    dataDir: /var/lib/etcd
imageRepository: registry.cn-hangzhou.aliyuncs.com/google_containers
kind: ClusterConfiguration
kubernetesVersion: 1.26.0
networking:
  dnsDomain: cluster.local
  serviceSubnet: 10.96.0.0/12
  podSubnet: 192.168.66.0/24
scheduler: {}    
```
- 执行初始化操作
```shell
sudo kubeadm init --config kubeadm.yaml
```
- 初始化成功后，按照提示执行下面命令：
```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
# 再切换到root用户执行下面命令：
export KUBECONFIG=/etc/kubernetes/admin.conf

```
### 六 master安装calico网络插件
- 下载calico.yaml配置
```shell
curl https://docs.tigera.io/archive/v3.25/manifests/calico.yaml -O

```
- 下载后需要修改其中的一项配置：

```yaml
 - name: CLUSTER_TYPE
   value: "k8s,bgp"
```
在该配置项下添加以下配置：
```yaml
 - name: IP_AUTODETECTION_METHOD
   value: "interface=ens.*"
```
- 安装插件
```shell
sudo kubectl apply -f calico.yaml
```

### 七 接入两个工作节点
```shell
# 生成token
sudo kubeadm token create --print-join-command
# node1/node2 加入
sudo kubeadm join --token ......
# 查看各节点状态
sudo kubectl get nodes
```
