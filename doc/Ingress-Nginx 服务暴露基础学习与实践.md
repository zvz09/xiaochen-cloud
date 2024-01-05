本章讲解通过服务发现的功能进行实现 , 由 Ingress controller 来提供路由信息的刷新, Ingress controller可以理解为一个监视器不断监听 kube-apiserver 实时感知service、Pod的变化

更多学习笔记文章请关注 `WeiyiGeek` 公众账号，学习交流【邮箱联系: Master#weiyigeek.top】

原文地址: [https://mp.weixin.qq.com/s/ZxyNCP32BQjQo5ygztJttQ](/developer/tools/blog-entry?target=https%3A%2F%2Fmp.weixin.qq.com%2Fs%2FZxyNCP32BQjQo5ygztJttQ&source=article&objectId=1852348)

*** ** * ** ***

### 0x00 前言简述 {#3ti5a}

描述： 到目前为止我们了解kubernetes常用的三种暴露服务的方式：LoadBlancer Service、 NodePort Service、Ingress

* LoadBlancer Service 是kubernetes结合云平台的组件，如国外的GCE，AWS，国内阿里云等等。使用它项使用的底层云平台申请创建负载均衡器来实现，对使用云平台的集群比较方便，但有局限，费用高。
* NodePort Service 是kubernetes在每个节点上暴露一个相同的端口(默认:30000-32000)但是由于安全和易用方面（服务多了就乱了还有端口冲突问题）且对主机安全性存在一定风险（内网环境，问题不大），所以实际使用并不多，当然对于小规模的集群服务，还是比较不错的。
* Ingress 是 kubernetes 中对外暴露服务的一种方式，它是使用Nginx进行反代应用来实现，其特点是安全以及方便统一管理等。

Tips : 此外externalIPs也可以使各类service对外提供服务，但是当集群服务很多的时候，NodePort方式最大的缺点是会占用很多集群机器的端口；LB方式最大的缺点则是每个service一个LB又有点浪费和麻烦，并且需要k8s之外的支持； 而ingress则只需要一个NodePort或者一个LB就可以满足所有service对外服务的需求(便于管理)。

92

#### Ingress 基础介绍 {#6u9ja}

**Q: 什么是负载均衡Ingress?**

描述：Ingress 其实就是集群外部访问的一个入口(在kubernetes v1.1时加入)，将外部的请求转发到不同的 Server 上，其实就相当于 `Nginx、Haproxy` 等负载均衡器。

即: Nginx-Ingress 是 Kubernetes 使用 NGINX 作为反向代理和负载平衡器的入口控制器。

```text
 internet
    |
[ Ingress ]
--|-----|--
[ Services ]
```

复制

Tips : 对于k8s传统的svc来说它仅支持4层代理, 如果需要做七层代理就需要使用k8s官方在1.11中推出了ingress api接口，通过ingress达到7层代理的效果我对于ingress来说必须要绑定一个域名因为它是基于7层代理的

Tips : ingress调度的是后端的service而不是pod.

**NGINX 配置**

描述: Ingress 控制器的目标是组装一个配置文件(nginx.conf), 当修改配置文件发生任何更改后需要重新加载 NGINX。

**NGINX 模型与构建**

描述: Kubernetes 控制器使用同步循环模式来检查控制器中的所需状态是否已更新或是否需要更改。为此，我们需要使用集群中的不同对象构建模型，特别是（无特殊顺序）`Ingresses、Services、Endpoints、Secrets 和 Configmaps` 以生成反映集群状态的时间点配置文件.

描述: 建立模型是一项昂贵的操作，因此必须使用同步循环。通过使用工作队列，可以不丢失更改并删除使用sync.Mutex来强制同步循环的单次执行，此外还可以在同步循环的开始和结束之间创建一个时间窗口，允许我们丢弃不必要的更新。

Tips: NGINX 配置的最终表示是从Go 模板生成的，使用新模型作为模板所需变量的输入。

**Q: ingress-nginx解决了生产环境中哪些问题?**

* 1）动态配置服务：如果按照传统方式，当新增加一个服务时，我们可能需要在流量入口加一个反向代理指向我们新的服务，而使用ingress，只需要配置好ingress，当服务启动时，会自动注册到ingress当中，不需要额外的操作。
* 2）减少不必要的Port暴露（安全，端口容易管理）: 我们知道部署k8s时，是需要关闭防火墙的，主要原因是k8s的很多服务会以nodeport方式映射出去，这样对于宿主机来说是非常的不安全的，而ingress可以避免这个问题，只需要将ingress自身服务映射出去，就可代理后端所有的服务，则后端服务不需要映射出去。

**Q: ingress-Nginx和ingress-Nginx-Controller的区别?**

* ingress-Nginx: 是每个服务自己创建的ingress,就是nginx的转发规则,生成Nginx的配置文件
* ingress-Nginx-Controller: 相当于Nginx的服务,监听API Server,根据用户编写的ingress-nginx规则(ingress.yaml文件),动态的去更改Nginx服务的配置文件,并且reload使其生效,此过程是自动化的通过lua实现

#### Ingress 实现原理 {#45aqq}

描述: Ingress 实际上是通过服务发现的功能进行实现，通过它来提供路由信息的刷新, `Ingress controller`可以理解为一个监视器，不断监听 `kube-apiserver` 实时感知service、Pod的变化，其再结合Ingress的配置，更新反向代理负载均衡器，达到服务发现的作用。

Ingress 公开了从集群外部到集群内服务的 HTTP 和 HTTPS 路由，流量路由由 Ingress 资源上定义的规则控制,可以将 Ingress 配置为服务提供外部可访问的 URL、负载均衡流量、终止 SSL/TLS，以及提供基于名称的虚拟主机访问。

Tips: Ingress 控制器通常负责通过负载均衡器来实现 Ingress，尽管它也可以配置边缘路由器或其他前端来帮助处理流量，并且ingress不暴露任何端口或协议。

**组件组成**

描述: 从下图可以看出`Ingress-nginx`一般由三个组件组成`反向代理负载均衡器 , Ingress Controller, Ingress`

* 1）反向代理负载均衡器：通常以service的port方式运行，接收并按照ingress定义的规则进行转发，常用的有nginx，Haproxy，Traefik等，本文中使用的就是nginx。
* 2）ingress-nginx-Controller： 监听APIServer，根据用户编写的ingress规则（编写ingress的yaml文件），动态地去更改nginx服务的配置文件，并且reload重载使其生效，此过程是自动化的（通过lua脚本来实现）。
* 3）Ingress：将nginx的配置抽象成一个Ingress对象，当用户每添加一个新的服务，只需要编写一个新的ingress的yaml文件即可。

![WeiyiGeek.Ingress-Nginx流程与实现原理](https://ask.qcloudimg.com/http-save/1389665/c58da99727a82304614194a8c5c7d6d3.png)  
WeiyiGeek.Ingress-Nginx流程与实现原理

**工作流程**

* 1）ingress controller通过和kubernetes api交互，动态的去感知集群中ingress规则变化。
* 2）然后读取它，按照自定义的规则，规则就是写明了那个域名对应哪个service，生成一段nginx配置。
* 3）在写到nginx-ingress-controller的pod里，这个Ingress controller的pod里运行着一个Nginx服务，控制器会把生成的nginx配置写入/etc/nginx.conf文件中。
* 4）最后 reload 一下使配置生效，以此达到分配和动态更新问题。

注意：写入 nginx.conf 的不是service的地址，而是service backend 的 pod 的地址，避免在 service 在增加一层负载均衡转发
![WeiyiGeek.工作流程](https://ask.qcloudimg.com/http-save/1389665/61fabd5057e780a84d3f4ee20d993cf0.png)  
WeiyiGeek.工作流程

Tips : 目前可以提供Ingress controller有很多比如`traefik、nginx-ingress、Kubernetes Ingress Cpmtrper for Kong、HAProxy Ingress controller`等。

Tips : 目前常见的负载均衡有`Ingress-Nginx`和`Ingress-Traefik`等。

Tips: 如果多个 Ingress 为同一主机定义了路径，则 Ingress 控制器会合并这些定义。

Tips: 入口控制器第一次启动时，两个作业创建了准入 Webhook 使用的 SSL 证书。因此，在可以创建和验证 Ingress 定义之前，会有最多两分钟的初始延迟。

Tips: admission webhook 需要Kubernetes API服务器和入口控制器之间的连接,请保证防火墙允许8443端口通信。

#### 补充说明 {#cc4ji}

* 节点： Kubernetes 集群中的服务器
* 集群： Kubernetes 管理的一组服务器集合
* 边界路由器： 为局域网和 Internet 路由数据包的路由器，执行防火墙保护局域网络
* 集群网络： 遵循 Kubernetes 网络模型实现集群内的通信的具体实现，比如 Flannel 和 Calico
* 服务： Kubernetes 的服务 (Service) 是使用标签选择器标识的一组 Pod Service (Deployment)。 除非另有说明，否则服务的虚拟 IP 仅可在集群内部访问
* NodePort: 服务是引导外部流量到你的服务的最原始方式。
* LoadBalancer: 服务是暴露服务到 Internet 的标准方式。
* Ingress: 事实上不是一种服务类型。Ingress 可能是暴露服务的最强大方式，但同时也是最复杂的。Ingress 控制器有各种类型，包括 Google Cloud Load Balancer，Nginx，Contour，Istio等等。

*** ** * ** ***

### 0x01 Ingress 安装配置 {#arhl9}

描述: Ingress-Nginx本质上是创建了一个Nginx的Node pod，只不过这个无需手写Nginx的配置文件而是通过自动生成的方式实现;

**帮助参考**

Ingress-Nginx 官方网站：[https://kubernetes.github.io/ingress-nginx/](/developer/tools/blog-entry?target=https%3A%2F%2Fkubernetes.github.io%2Fingress-nginx%2F&source=article&objectId=1852348)

Ingress-Nginx Github地址：[https://github.com/kubernetes/ingress-nginx](/developer/tools/blog-entry?target=https%3A%2F%2Fgithub.com%2Fkubernetes%2Fingress-nginx&source=article&objectId=1852348)

版本查看: [https://github.com/kubernetes/ingress-nginx/releases](/developer/tools/blog-entry?target=https%3A%2F%2Fgithub.com%2Fkubernetes%2Fingress-nginx%2Freleases&source=article&objectId=1852348)

**版本支持说明** : [https://github.com/kubernetes/ingress-nginx/#support-versions-table](/developer/tools/blog-entry?target=https%3A%2F%2Fgithub.com%2Fkubernetes%2Fingress-nginx%2F%23support-versions-table&source=article&objectId=1852348)

```text
Ingress-nginx version	| k8s supported version| Alpine Version | Nginx Version
v1.0.0-alpha.2	1.22, 1.21, 1.20, 1.19	3.13.5	1.20.1
v1.0.0-alpha.1	1.21, 1.20, 1.19	3.13.5	1.20.1
v0.48.1	1.21, 1.20, 1.19	3.13.5	1.20.1
v0.47.0	1.21, 1.20, 1.19	3.13.5	1.20.1
v0.46.0	1.21, 1.20, 1.19	3.13.2	1.19.6
```

复制

#### 配置指南 {#chu32}

描述: 默认配置监视来自所有命名空间的Ingress 对象。要更改此行为，请使用标志`--watch-namespace`将范围限制为特定命名空间。

```text
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s
```

复制

**定制NGINX有三种方式:** 参考地址([https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration/](/developer/tools/blog-entry?target=https%3A%2F%2Fkubernetes.github.io%2Fingress-nginx%2Fuser-guide%2Fnginx-configuration%2F&source=article&objectId=1852348))

* ConfigMap: 使用ConfigMap在NGINX中设置全局配置。
* Annotations: 如果您需要特定入口规则的特定配置，请使用此选项。
* Custom template: 当需要更具体的设置（如打开文件缓存）时，将侦听选项调整为rcvbuf或当无法通过ConfigMap更改配置时。

**sysctl 调优**

描述: 在演示使用 Init Container 来调整 sysctl 默认值 kubectl patch, `(1) 积压队列设置net.core.somaxconn从128到32768, (2) 临时端口设置net.ipv4.ip_local_port_range从32768 60999到1024 65000`

```text
kubectl patch deployment -n ingress-nginx nginx-ingress-controller \
  --patch="$(curl https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/docs/examples/customization/sysctl/patch.json)"
```

复制

#### 安装部署 {#40ndg}

**Ingress-Nginx 基础环境部署流程**

* (1) 使用 NodePort : [https://kubernetes.github.io/ingress-nginx/deploy/](/developer/tools/blog-entry?target=https%3A%2F%2Fkubernetes.github.io%2Fingress-nginx%2Fdeploy%2F&source=article&objectId=1852348)

```text
wget https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.47.0/deploy/static/provider/baremetal/deploy.yaml
```

复制

* (2) 镜像拉取

```text
# 查看依赖的镜像
~/K8s/Day7/demo1$ cat deploy.yaml | grep "image:" | sort | uniq
  # image: docker.io/jettech/kube-webhook-certgen:v1.5.0
  # image: k8s.gcr.io/ingress-nginx/controller:v0.47.0@sha256:1f4f402b9c14f3ae92b11ada1dfe9893a88f0faeb0b2f4b903e2c67a0c3bf0de
# 拉取所需镜像: 如果您有国外的VPS可以直接进行拉取或者提花替换 k8s.gcr.io镜像站点;
# ingress-nginx-controll 版本稍微延迟: https://hub.docker.com/r/pollyduan/ingress-nginx-controller
# sed -i "s|k8s.gcr.io/ingress-nginx/controller\S.*$|pollyduan/ingress-nginx-controller:v0.47.0|g" deploy.yaml
docker pull pollyduan/ingress-nginx-controller:v0.47.0
docker pull docker.io/jettech/kube-webhook-certgen:v1.5.0
```

复制

* (3) 更改Tag上传到私有仓库中

```text
~/K8s/Day7/demo1$ docker login harbor.weiyigeek.top
~/K8s/Day7/demo1$ docker tag docker.io/jettech/kube-webhook-certgen:v1.5.0 harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0
~/K8s/Day7/demo1$ docker tag pollyduan/ingress-nginx-controller:v0.41.0 harbor.weiyigeek.top/test/ingress-nginx-controller:v0.41.0
  # docker push harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0
  # docker push harbor.weiyigeek.top/test/ingress-nginx-controller:v0.41.0
```

复制

* (4) 部署清单修改与部署

```text
# 将官方的镜像更改为私有镜像Tag方便在私有仓库中进行拉取
sed -i "s#docker.io/jettech/kube-webhook-certgen:v1.5.0#harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0#g" deploy.yaml
sed -i "s#k8s.gcr.io/ingress-nginx/controller:v0.47.0@sha256:1f4f402b9c14f3ae92b11ada1dfe9893a88f0faeb0b2f4b903e2c67a0c3bf0de#harbor.weiyigeek.top/test/ingress-nginx-controller:v0.41.0#g" deploy.yaml

~/K8s/Day7/demo1$ kubectl apply -f deploy.yaml
  # namespace/ingress-nginx created
  # serviceaccount/ingress-nginx created
  # configmap/ingress-nginx-controller created
  # clusterrole.rbac.authorization.k8s.io/ingress-nginx created
  # clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx created
  # role.rbac.authorization.k8s.io/ingress-nginx created
  # rolebinding.rbac.authorization.k8s.io/ingress-nginx created
  # service/ingress-nginx-controller-admission created
  # service/ingress-nginx-controller created
  # deployment.apps/ingress-nginx-controller created
  # validatingwebhookconfiguration.admissionregistration.k8s.io/ingress-nginx-admission created
  # serviceaccount/ingress-nginx-admission created
  # clusterrole.rbac.authorization.k8s.io/ingress-nginx-admission created
  # clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
  # role.rbac.authorization.k8s.io/ingress-nginx-admission created
  # rolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
  # job.batch/ingress-nginx-admission-create created
  # job.batch/ingress-nginx-admission-patch created
```

复制

* (5) 验证查看

```text
$ kubectl get ns | grep "ingress" # 名称空间查看
  # ingress-nginx     Active   59s

$ kubectl get svc -n ingress-nginx   # Service 服务查看
  # NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
  # ingress-nginx-controller             NodePort    10.96.183.29    <none>        80:32268/TCP,443:30499/TCP   22m
  # ingress-nginx-controller-admission   ClusterIP   10.98.154.140   <none>        443/TCP                      22m

$ kubectl get deployment -n ingress-nginx -o wide # deployment 控制器
  # NAME                       READY   UP-TO-DATE   AVAILABLE   AGE     CONTAINERS   IMAGES                                                       SELECTOR
  # ingress-nginx-controller   1/1     1            1           2m14s   controller   harbor.weiyigeek.top/test/ingress-nginx-controller:v0.41.0   app.kubernetes.io/component=controller,app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx
$ kubectl get rs -n ingress-nginx -o wide  # ReplicaSet 控制器
  # NAME                                  DESIRED   CURRENT   READY   AGE    CONTAINERS   IMAGES                                                       SELECTOR
  # ingress-nginx-controller-849d675ffc   1         1         1       115s   controller   harbor.weiyigeek.top/test/ingress-nginx-controller:v0.41.0   app.kubernetes.io/component=controller,app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx,pod-template-hash=849d675ffc
$ kubectl get pod -n ingress-nginx -o wide --show-labels  #Pod 资源控制器
  # NAME                                        READY   STATUS      RESTARTS   AGE   IP             NODE         NOMINATED NODE   READINESS GATES   LABELS
  # ingress-nginx-admission-create-ds8b5        0/1     Completed   0          11m   10.244.1.110   k8s-node-4   <none>           <none>            app.kubernetes.io/component=admission-webhook,app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/managed-by=Helm,app.kubernetes.io/name=ingress-nginx,app.kubernetes.io/version=0.47.0,controller-uid=2d63785a-c26d-413a-9d9d-d68ba255f0cd,helm.sh/chart=ingress-nginx-3.10.1,job-name=ingress-nginx-admission-create
  # ingress-nginx-admission-patch-kzx2v         0/1     Completed   1          11m   10.244.1.111   k8s-node-4   <none>           <none>            app.kubernetes.io/component=admission-webhook,app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/managed-by=Helm,app.kubernetes.io/name=ingress-nginx,app.kubernetes.io/version=0.47.0,controller-uid=c5ac4231-dc89-4699-996c-a1ea657cd7e7,helm.sh/chart=ingress-nginx-3.10.1,job-name=ingress-nginx-admission-patch
  # ingress-nginx-controller-849d675ffc-7ftvt   1/1     Running     0          11m   10.244.1.114   k8s-node-4   <none>           <none>            app.kubernetes.io/component=controller,app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx,pod-template-hash=849d675ffc
```

复制

* (6) 运行的Pod过程查看

```text
~/K8s/Day7/demo1$ kubectl describe -n ingress-nginx pod ingress-nginx-admission-patch-kzx2v
  # Name:         ingress-nginx-admission-patch-kzx2v
  # Namespace:    ingress-nginx
  # Events:
  #   Type    Reason          Age                  From               Message
  #   ----    ------          ----                 ----               -------
  #   Normal  Scheduled       4m6s                 default-scheduler  Successfully assigned ingress-nginx/ingress-nginx-admission-patch-kzx2v to k8s-node-4
  #   Normal  Pulling         4m5s                 kubelet            Pulling image "harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0"
  #   Normal  Pulled          4m4s                 kubelet            Successfully pulled image "harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0" in 788.689782ms
  #   Normal  Created         4m1s (x2 over 4m3s)  kubelet            Created container patch
  #   Normal  Started         4m1s (x2 over 4m3s)  kubelet            Started container patch
  #   Normal  Pulled          4m1s                 kubelet            Container image "harbor.weiyigeek.top/test/kube-webhook-certgen:v1.5.0" already present on machine
  #   Normal  SandboxChanged  3m59s                kubelet            Pod sandbox changed, it will be killed and re-created.
```

复制

#### 基础示例 {#3003o}

官方示例参考: [https://kubernetes.github.io/ingress-nginx/examples/](/developer/tools/blog-entry?target=https%3A%2F%2Fkubernetes.github.io%2Fingress-nginx%2Fexamples%2F&source=article&objectId=1852348)

##### 示例1.Ingress 常规使用方案 {#atsu}

Ingress HTTP 代理访问资源清单的编写

```text
cat > dep-svc-nginx-http-v1.yaml<<'EOF'
# Deployment 控制器
apiVersion: apps/v1
kind: Deployment 
metadata:
  name: nginx-dm 
spec:
  replicas: 2
  selector:                  # 选择器
    matchLabels:  
      name: nginx-ingress-v1  # 匹配的Pod标签非常重要
  template:
    metadata:
      labels:
        name: nginx-ingress-v1
    spec:
      containers:
        - name: nginx 
          image: harbor.weiyigeek.top/test/nginx:v1.0
          imagePullPolicy: IfNotPresent 
          ports:
            - containerPort: 80
---
# Service 控制器
apiVersion: v1 
kind: Service 
metadata:
  name: nginx-ingress-v1-svc 
spec:
  ports:
    - port: 80 
      targetPort: 80 
      protocol: TCP 
  selector:
    name: nginx-ingress-v1               # 【重点】用于关联Pod所以标签与Deployment定义的标签一种
EOF

cat > ingress-nginx-http-v1.yaml<<'EOF'
# Ingress 扩展
# Warning: extensions/v1beta1 Ingress is deprecated in v1.14+, unavailable in v1.22+; use networking.k8s.io/v1 Ingress
apiVersion: networking.k8s.io/v1         # 注意点否则将报上面的预警
kind: Ingress 
metadata:
  name: nginx-ingress-http 
spec:
  rules:
    - host: web.weiyigeek.top
      http:
        paths:
        - pathType: Prefix
          path: / 
          backend:
           service:
            name: nginx-ingress-v1-svc   #【重点】关联Service控制器所以与Service定义的源数据名称一致
            port:
              number: 80
EOF
```

复制

**操作流程:**

```text
# (1) 部署 Deployment 与 Service
~/K8s/Day7/demo2$ kubectl create -f dep-svc-nginx-http-v1.yaml

# PS: 如果出现错误则需要在进行ingress创建的时候需要将Webhook删了才能正常创建ingress。
# ~/K8s/Day7/demo2$ kubectl delete -A ValidatingWebhookConfiguration ingress-nginx-admission
  # validatingwebhookconfiguration.admissionregistration.k8s.io "ingress-nginx-admission" deleted
  
~/K8s/Day7/demo2$ kubectl create -f ingress-nginx-http-v1.yaml
  # ingress.networking.k8s.io/nginx-ingress-http created

# (2) 查看
$ kubectl get svc -n ingress-nginx # 查看服务端口
  # NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
  # ingress-nginx-controller             NodePort    10.96.183.29    <none>        80:32268/TCP,443:30499/TCP   8h
  # ingress-nginx-controller-admission   ClusterIP   10.98.154.140   <none>        443/TCP                      8h
~/K8s/Day7/demo2$ kubectl get ingress
# Warning: extensions/v1beta1 Ingress is deprecated in v1.14+, unavailable in v1.22+; use networking.k8s.io/v1 Ingress  # 我们已使用`networking.k8s.io/v1`
  # NAME                 CLASS    HOSTS               ADDRESS         PORTS   AGE
  # nginx-ingress-http   <none>   web.weiyigeek.top   10.10.107.214(绑定的节点地址)   80      8h
~/K8s/Day7/demo2$ kubectl get pod -o wide
  # NAME                        READY   STATUS    RESTARTS   AGE   IP             NODE         NOMINATED NODE   READINESS GATES
  # nginx-dm-7654c987b4-s5cj2   1/1     Running   0          8h    10.244.1.125   k8s-node-4   <none>           <none>
  # nginx-dm-7654c987b4-wgfm2   1/1     Running   0          8h    10.244.1.126   k8s-node-4   <none>           <none>


# (3) 访问端设置本地DNS解析 & 在浏览器中进行HTTP访问
# /etc/hosts
10.10.107.214 web.weiyigeek.top 
# PS: 这里访问的是Node节点映射出的端口而非直接访问的80端口
~/K8s/Day7/demo2$ curl web.weiyigeek.top:32268/host.html
  # Hostname: nginx-dm-7654c987b4-wgfm2  <br>
  # Image Version: <u> 1.0 </u>
  # Nginx Version: 1.19.4
~/K8s/Day7/demo2$ curl web.weiyigeek.top:32268/host.html
  # Hostname: nginx-dm-7654c987b4-s5cj2  <br>
  # Image Version: <u> 1.0 </u>
  # Nginx Version: 1.19.4


# (4) 验证
/K8s/Day7/demo2$ sudo ipvsadm -Ln
IP Virtual Server version 1.2.1 (size=4096)
Prot LocalAddress:Port Scheduler Flags
  -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
TCP  172.17.0.1:30499 rr
  -> 10.244.1.124:443             Masq    1      0          0
TCP  172.17.0.1:32268 rr
  -> 10.244.1.124:80              Masq    1      0          0
TCP  172.18.0.1:30499 rr
  -> 10.244.1.124:443             Masq    1      0          0
TCP  172.18.0.1:32268 rr
  -> 10.244.1.124:80              Masq    1      0          0
TCP  172.18.0.1:32306 rr
  -> 10.244.1.107:80              Masq    1      0          0
  -> 10.244.1.108:80              Masq    1      0          0
  -> 10.244.1.109:80              Masq    1      0          0

# Node(节点)
TCP  10.10.107.202:30499 rr
  -> 10.244.1.124:443             Masq    1      0          0
TCP  10.10.107.202:32268 rr
  -> 10.244.1.124:80   

# NodePort
TCP  10.96.183.29:80 rr
  -> 10.244.1.124:80              Masq    1      0          0
TCP  10.96.183.29:443 rr
  -> 10.244.1.124:443             Masq    1      0          0

# CluterPort
TCP  10.98.154.140:443 rr
  -> 10.244.1.124:8443            Masq    1      0          0
TCP  10.102.158.47:80 rr
  -> 10.244.1.125:80              Masq    1      0          0
  -> 10.244.1.126:80              Masq    1      0          0
```

复制

##### 示例2.Ingress HTTPS 代理访问 {#elqi9}

描述: TLS 证书配置参考地址([https://kubernetes.github.io/ingress-nginx/user-guide/tls/](/developer/tools/blog-entry?target=https%3A%2F%2Fkubernetes.github.io%2Fingress-nginx%2Fuser-guide%2Ftls%2F&source=article&objectId=1852348)),注意创建自建证书（主：浏览器不认可的）。默认得Ingress-nginx配置了SSL会自动跳转到https网页。

```text
#指示位置部分是否仅可访问SSL（当Ingress包含证书默认为True) bool, 当设置为false时禁用https强制跳转。
nginx.ingress.kubernetes.io/ssl-redirect    
nginx.ingress.kubernetes.io/force-ssl-redirect # 即使lngress未启用TLS，也强制重定向到HTTPS bool
```

复制

资源清单示例:

```text
# Deployment & Service
cat > dep-ingress-nginx-https-v3.yaml<<'EOF'
apiVersion: apps/v1
kind: Deployment 
metadata:
  name: ingress-nginx-https
spec:
  replicas: 2 
  selector:                          # 选择器
    matchLabels:  
      name: ingress-nginx-https-v3  # 匹配的Pod标签非常重要
  template:
    metadata:
      labels:
        name: ingress-nginx-https-v3
    spec:
      containers:
        - name: ingress-nginx-https-v3
          image: harbor.weiyigeek.top/test/nginx:v3.0
          imagePullPolicy: IfNotPresent 
          ports:
            - containerPort: 80
---
apiVersion: v1 
kind: Service 
metadata:
  name: ingress-nginx-https-svc
spec:
  ports:
    - port: 80 
      targetPort: 80 
      protocol: TCP 
  selector:
    name: ingress-nginx-https-v3   # 关联指定标签的Pod
EOF

# Ingress
cat > ingress-nginx-https-v3.yaml<<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress 
metadata:
  name: ingress-nginx-https
spec:
  tls:
    - hosts:
      - www3.weiyigeek.top
      secretName: tls-secret 
  rules:
    - host: www3.weiyigeek.top
      http:
        paths:
        - pathType: Prefix
          path: / 
          backend:
            service:
              name: ingress-nginx-https-svc   #【重点】 关联Service控制器所以与Service定义的源数据名称一致
              port:
                number: 80
EOF
```

复制

**操作流程：**

```text
# (1) 创建 Pod 与 Service
~/K8s/Day7/demo2$ kubectl create -f dep-ingress-nginx-https-v3.yaml
  # deployment.apps/ingress-nginx-https created
  # service/ingress-nginx-https-svc created

# (2) 创建证书及cert存储方式
openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -subj "/CN=www3.weiyigeek.top/O=www3.weiyigeek.top"
  # Generating a RSA private key
  # ..+++++
  # ........................+++++
  # writing new private key to 'tls.key'
  # -----
kubectl create secret tls tls-secret --key tls.key --cert tls.crt # 后续讲解
  # secret/tls-secret created

# (3) 创建 ingress-nginx https 
kubectl create -f ingress-nginx-https-v3.yaml
  # ingress.networking.k8s.io/ingress-nginx-https created

# (4) 查看
~/K8s/Day7/demo2$ kubectl get ingress
  # Warning: extensions/v1beta1 Ingress is deprecated in v1.14+, unavailable in v1.22+; use networking.k8s.io/v1 Ingress
  # NAME                  CLASS    HOSTS                ADDRESS         PORTS     AGE
  # ingress-nginx-https   <none>   www3.weiyigeek.top   10.10.107.214   80, 443   117s
~/K8s/Day7/demo2$ kubectl get svc -n ingress-nginx  # ingress-nginx 的 SVC 信息里面有Nodeport暴露的端口
  # NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
  # ingress-nginx-controller             NodePort    10.96.183.29    <none>        80:32268/TCP,443:30499/TCP   9h
  # ingress-nginx-controller-admission   ClusterIP   10.98.154.140   <none>        443/TCP                      9h

# (5) 验证
# http访问: https://www3.weiyigeek.top/
无法访问  # 由于配置了https会导致http无法正常访问(在后续实验中会将http访问的流量强制转到https之中)

# https访问: https://www3.weiyigeek.top:30499/host.html
Hostname: ingress-nginx-https-55bb4cd6fd-4rmpm ,Image Version: 3.0, Nginx Version: 1.19.4 
```

复制
![WeiyiGeek.ingress-nginx-https](https://ask.qcloudimg.com/http-save/1389665/31407256f79d83b8b1376be91c2bcc7f.png)  
WeiyiGeek.ingress-nginx-https

PS : 实际上我们通过Ingress资源清单添加的域名绑定主机时被写入到 `ingress-nginx-controller` Pod 中的nginx.conf 文件之中，这才能让我们通过自定义域名的方式访问虚拟主机;

```text
~/K8s/Day7/demo2$ kubectl exec -n ingress-nginx ingress-nginx-controller-849d675ffc-6rxlm -it -- /bin/sh
/etc/nginx $ ls
# fastcgi.conf            koi-utf                 modsecurity             owasp-modsecurity-crs   uwsgi_params.default
# fastcgi.conf.default    koi-win                 modules                 scgi_params             win-utf
# fastcgi_params          lua                     nginx.conf              scgi_params.default
# fastcgi_params.default  mime.types              nginx.conf.default      template
# geoip                   mime.types.default      opentracing.json        uwsgi_params

/etc/nginx $ cat nginx.conf | grep "www3.weiyigeek.top" -n
  # 551:    ## start server www3.weiyigeek.top
  # 553:            server_name www3.weiyigeek.top ;
  # 679:    ## end server www3.weiyigeek.top
```

复制

Ingress-Nginx-Https配置示例:

```text
# 下面可以帮助我们认是ingress-nginx实现https是如何进行配置的;
## start server www3.weiyigeek.top
        server {
                server_name www3.weiyigeek.top ;

                listen 80  ;
                listen 443  ssl http2 ;

                set $proxy_upstream_name "-";

                ssl_certificate_by_lua_block {
                        certificate.call()
                }

                location / {
                        set $namespace      "default";
                        set $ingress_name   "ingress-nginx-https";
                        set $service_name   "ingress-nginx-https-svc";
                        set $service_port   "80";
                        set $location_path  "/";

                        rewrite_by_lua_block {
                                lua_ingress.rewrite({
                                        force_ssl_redirect = false,
                                        ssl_redirect = true,
                                        force_no_ssl_redirect = false,
                                        use_port_in_redirects = false,
                                        path_type = "Prefix",
                                })
                                balancer.rewrite()
                                plugins.run()
                        }

                        # be careful with `access_by_lua_block` and `satisfy any` directives as satisfy any
                        # will always succeed when there's `access_by_lua_block` that does not have any lua code doing `ngx.exit(ngx.DECLINED)`
                        # other authentication method such as basic auth or external auth useless - all requests will be allowed.
                        #access_by_lua_block {
                        #}

                        header_filter_by_lua_block {
                                lua_ingress.header()
                                plugins.run()
                        }

                        body_filter_by_lua_block {
                        }

                        log_by_lua_block {
                                balancer.log()

                                monitor.call()

                                plugins.run()
                        }

                        port_in_redirect off;

                        set $balancer_ewma_score -1;
                        set $proxy_upstream_name "default-ingress-nginx-https-svc-80";
                        set $proxy_host          $proxy_upstream_name;
                        set $pass_access_scheme  $scheme;

                        set $pass_server_port    $server_port;

                        set $best_http_host      $http_host;
                        set $pass_port           $pass_server_port;

                        set $proxy_alternative_upstream_name "";

                        client_max_body_size                    1m;

                        proxy_set_header Host                   $best_http_host;

                        # Pass the extracted client certificate to the backend

                        # Allow websocket connections
                        proxy_set_header                        Upgrade           $http_upgrade;

                        proxy_set_header                        Connection        $connection_upgrade;

                        proxy_set_header X-Request-ID           $req_id;
                        proxy_set_header X-Real-IP              $remote_addr;

                        proxy_set_header X-Forwarded-For        $remote_addr;

                        proxy_set_header X-Forwarded-Host       $best_http_host;
                        proxy_set_header X-Forwarded-Port       $pass_port;
                        proxy_set_header X-Forwarded-Proto      $pass_access_scheme;

                        proxy_set_header X-Scheme               $pass_access_scheme;

                        # Pass the original X-Forwarded-For
                        proxy_set_header X-Original-Forwarded-For $http_x_forwarded_for;

                        # mitigate HTTPoxy Vulnerability
                        # https://www.nginx.com/blog/mitigating-the-httpoxy-vulnerability-with-nginx/
                        proxy_set_header Proxy                  "";

                        # Custom headers to proxied server

                        proxy_connect_timeout                   5s;
                        proxy_send_timeout                      60s;
                        proxy_read_timeout                      60s;

                        proxy_buffering                         off;
                        proxy_buffer_size                       4k;
                        proxy_buffers                           4 4k;

                        proxy_max_temp_file_size                1024m;

                        proxy_request_buffering                 on;
                        proxy_http_version                      1.1;

                        proxy_cookie_domain                     off;
                        proxy_cookie_path                       off;

                        # In case of errors try the next upstream server before returning an error
                        proxy_next_upstream                     error timeout;
                        proxy_next_upstream_timeout             0;
                        proxy_next_upstream_tries               3;

                        proxy_pass http://upstream_balancer;

                        proxy_redirect                          off;

                }
        }
        ## end server www3.weiyigeek.top
```

复制

##### 示例3.Ingress Rewrite 重写重定向访问 {#7bl94}

描述: 以下是Ingress-Nginx 重写`annotations`的相关属性

```text
名称 	描述 	值类型
nginx.ingress.kubernetes.io/use-regex #指示Ingress上定义的路径是否使用正则表达式 	bool
nginx.ingress.kubernetes.io/rewrite-target  #必须重定向流量的目标URI 	string
nginx.ingress.kubernetes.io/app-root  #定义Controller必须重定向的应用程序根，如果它在'/'上下文中 	string
nginx.ingress.kubernetes.io/ssl-redirect    #指示位置部分是否仅可访问SSL（当Ingress包含证书默认为True) bool
nginx.ingress.kubernetes.io/force-ssl-redirect #即使lngress未启用TLS，也强制重定向到HTTPS 	bool

annotations:
  # 指定 Ingress Controller 的类型
  kubernetes.io/ingress.class: "nginx"
  # 指定我们的 rules 的 path 可以使用正则表达式
  nginx.ingress.kubernetes.io/use-regex: "true"
  # 连接超时时间，默认为 5s
  nginx.ingress.kubernetes.io/proxy-connect-timeout: "600"
  # 后端服务器回转数据超时时间，默认为 60s
  nginx.ingress.kubernetes.io/proxy-send-timeout: "600"
  # 后端服务器响应超时时间，默认为 60s
  nginx.ingress.kubernetes.io/proxy-read-timeout: "600"
  # 客户端上传文件，最大大小，默认为 20m
  nginx.ingress.kubernetes.io/proxy-body-size: "10m"
  # URL 重写
```

复制

Ingress Rewrite 资源清单示例:

```text
cat > ingress-nginx-http-rewrite-https-v3.yaml<<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress 
metadata:
  name: ingress-nginx-http-rewrite-https
  annotations:  # 注释
    nginx.ingress.kubernetes.io/rewrite-target: https://www3.weiyigeek.top:30499
spec:
  rules:
    - host: go.weiyigeek.top
      http:
        paths:
        - pathType: Prefix
          path: / 
          backend:
            service:
              name: ingress-nginx-https-svc   #【重点】 关联Service控制器所以与Service定义的源数据名称一致
              port:
                number: 80
EOF
```

复制

操作流程:

```text
# (1) 创建 Ingress 对象
~/K8s/Day7/demo2$ kubectl create -f ingress-nginx-http-rewrite-https-v3.yaml
# ingress.networking.k8s.io/ingress-nginx-http-rewrite-https created


# (2) 查看
~/K8s/Day7/demo2$ kubectl get ingress
  # NAME                               CLASS    HOSTS                ADDRESS         PORTS     AGE
  # ingress-nginx-http-rewrite-https   <none>   go.weiyigeek.top     10.10.107.214   80        7s
  # ingress-nginx-https                <none>   www3.weiyigeek.top   10.10.107.214   80, 443   31m
  # nginx-ingress-http                 <none>   web.weiyigeek.top    10.10.107.214   80        9h
~/K8s/Day7/demo2$ kubectl get svc -n ingress-nginx
  # ingress-nginx-controller  NodePort  10.96.183.29  80:32268/TCP,443:30499/TCP 


# (3) 访问验证(注意将域名指向节点地址) 
~/K8s/Day7/demo2$ curl -I http://go.weiyigeek.top:32268 # 强制转换到https
  # HTTP/1.1 302 Moved Temporarily
  # Date: Sat, 14 Nov 2020 15:14:00 GMT
  # Content-Type: text/html
  # Content-Length: 138
  # Connection: keep-alive
  # Location: https://www3.weiyigeek.top:30499/
```

复制

**补充说明:** Ingress-nginx 的前后端分离`(Rewrite)`演示

* 资源清单

```text
tee ingress-rewrite.yaml <<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-rewrite
  namespace: demo
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$2
spec:
  rules:
    - host: rewrite.weiyigeek.top
      http:
        paths:
        - path: /weiyigeek(/|$)(.*)
          pathType: ImplementationSpecific
          backend:
            service:
              name: backend-svc
              port:
                number: 8080
EOF
```

复制

* 部署验证

```text
$ kubectl create -f ingress-rewrite.yaml

$ kubectl describe ing -n demo ingress-rewrite
# Rules:
#   Host                   Path  Backends
#   ----                   ----  --------
#   rewrite.weiyigeek.top
#                          /weiyigeek(/|$)(.*)   backend-svc:8080 (192.168.109.90:8080,192.168.109.93:8080)
# Annotations:             nginx.ingress.kubernetes.io/rewrite-target: /$2

$ curl http://10.10.107.221 -H 'host: rewrite.weiyigeek.top' -k
  # <html>
  # <head><title>404 Not Found</title></head>
  # <body>
  # <center><h1>404 Not Found</h1></center>
  # <hr><center>nginx</center>
  # </body>
  # </html>

$ curl -v http://10.10.107.221/weiyigeek/ -H 'host: rewrite.weiyigeek.top' -k  # 匹配指定路径进行访问
hostname-web-backend-1-WeiyiGeek-Tomcat

$ curl  http://10.10.107.221/weiyigeek/test -H 'host: rewrite.weiyigeek.top' -k # 后端应用存在test目录以及文件方可，否则显示应用程序自动的错误。
```

复制

*** ** * ** ***

##### 示例4.Ingress VirtualHost 虚拟主机访问 {#2tv95}

描述: 我们知道Ingress-Nginx是基于Nginx的所以其也支持虚拟主机进行绑定访问，实现以不同的域名访问同一个web界面;

**Ingress 资源清单示例:**

```text
# 1) 采用 networking.k8s.io/v1 APIVERSION
cat > ingress-nginx-virtual-host.yaml<<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress 
metadata:
  name: ingress-nginx-virtual-host
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  defaultBackend:
    service:
      name: nginx-ingress-v1-svc
      port:
        number: 80 
  rules: 
    - host: host.weiyigeek.top # 以同一主机根据路径访问不同的Pod
      http:
        paths:
        - path: /
          pathType: Prefix # 【坑呀】supported values: "Exact", "ImplementationSpecific", "Prefix"
          backend:
            service:
              name: nginx-ingress-v1-svc   #【重点】 关联Service控制器所以与Service定义的源数据名称一致
              port:
                number: 80   
        - path: /v3
          pathType: Prefix # 【坑呀】supported values: "Exact", "ImplementationSpecific", "Prefix"
          backend:
            service:
              name: ingress-nginx-https-svc   #【重点】 关联Service控制器所以与Service定义的源数据名称一致
              port:
                number: 80
    - host: ingress.test2.com   #只需要在rules字段下添加另一个host字段即可，这里只实现httpd的，如果有需求可以添加上tomcat的。
      http:
        paths:
        - path: /
          backend:
            serviceName: httpd-svc   #注意，serviceName所有虚拟主机必须保持一致
            servicePort: 80
EOF

# 2) 采用 extensions/v1beta1 APIVERSION (旧版本-将会被弃用)
cat > ingress-nginx-virtual-host.yaml<<'EOF'
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: path-ingress
spec:
  rules:
  - host: host.weiyigeek.top
    http:
      paths:
      - path: /v1
        backend:
          serviceName: nginx-ingress-v1-svc
          servicePort: 80
      - path: /v2
        backend:
          serviceName: ingress-nginx-https-svc
          servicePort: 80
  - host: ingress.weiyigeek.top
    http:
      paths:
      - path: /
        backend:
          serviceName: httpd-svc
          servicePort: 80
EOF
```

复制

**操作流程：**

```text
# (0) 资源清单编写一览表
~/K8s/Day7/demo2$ kubectl explain --api-version=networking.k8s.io/v1 ingress.spec.rules.http.paths
 kubectl explain --api-version=networking.k8s.io/v1beta1 ingress.spec.rules.http.paths
  # KIND:     Ingress
  # VERSION:  networking.k8s.io/v1

# (1) 创建Ingresss对象 
~/K8s/Day7/demo2$ kubectl create -f ingress-nginx-virtual-host.yaml
  # ingress.networking.k8s.io/ingress-nginx-virtual-host created

# (2) 查看 ingress
~/K8s/Day7/demo2$ kubectl get ingress | grep "virtual"
  # ingress-nginx-virtual-host   <none>   host.weiyigeek.top, ingress.weiyigeek.top   10.10.107.214   80   102s

# (3) 设置/etc/hosts与该域名绑定
10.10.107.214 host.weiyigeek.top 
10.10.107.214 ingress.weiyigeek.top

# (4) 查看 ingress-nginx-virtual-host Pod 信息
$ kubectl describe ingress-nginx-virtual-host
```

复制

*** ** * ** ***

##### 示例5.Ingress BasicAuth（基础认证）功能 {#3j5ah}

描述:因为Ingress的实现方案采用的是Nginx的软件，所以Nginx的相关特性Ingress都支持, 下面我们利用 Nginx 的 BasicAuth 实现基础认证演示

Ingress 资源清单:

```text
cat > ingress-nginx-auth.yaml<<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress 
metadata:
  name: ingress-nginx-auth
  annotations:
    nginx.ingress.kubernetes.io/auth-type: basic 
    nginx.ingress.kubernetes.io/auth-secret: basic-auth 
    nginx.ingress.kubernetes.io/auth-realm: 'Authentication Required - WeiyiGeek' 
spec:
  rules: 
    - host: auth.weiyigeek.top   # 后端应用通过该域名进行访问以同一主机根据路径访问不同的Pod
      http:
        paths:
        - path: /
          pathType: Prefix
          backend:
            service:
              name: nginx-ingress-v1-svc   #【重点】 关联Service控制器所以与Service定义的源数据名称一致
              port:
                number: 80
  defaultBackend:
    service:
      name: nginx-ingress-v1-svc
      port:
        number: 80   
EOF
```

复制

操作流程:

```text
# (1) 先安装Apache的模块 : Nginx的认证方案所需
$ apt search htpasswd
  # Sorting... Done
  # Full Text Search... Done
  # apache2-utils/focal-security,focal-updates 2.4.41-4ubuntu3.1 amd64
  #   Apache HTTP Server (utility programs for web servers)
  # ... 下面还有其他的包这里旧不看了

$ sudo apt install apache2-utils
$ htpasswd -c auth weiyigeek  #创建密钥文件文件名为auth，用户名为weiyigeek

# (2) 把该文件以secret方式进行保存，类型为generic
# -rw-r--r-- 1 weiyigeek weiyigeek   48 Nov 15 12:48 auth
$ kubectl create secret generic basic-auth --from-file=auth
  # secret/basic-auth created


# (3) 部署上面Ingress资源清单
weiyigeek@ubuntu:~/K8s/Day7/demo2$ kubectl create -f ingress-nginx-auth.yaml
  # ingress.networking.k8s.io/ingress-nginx-auth configured

# (4) 查看
weiyigeek@ubuntu:~/K8s/Day7/demo2$ kubectl get ingress
  # NAME                               CLASS    HOSTS                ADDRESS         PORTS     AGE
  # ingress-nginx-auth                 <none>   auth.weiyigeek.top   10.10.107.214   80        2m26s
  # ingress-nginx-http-rewrite-https   <none>   go.weiyigeek.top     10.10.107.214   80        13h
  # ingress-nginx-https                <none>   www3.weiyigeek.top   10.10.107.214   80, 443   14h
  # nginx-ingress-http                 <none>   web.weiyigeek.top    10.10.107.214   80        23h
  # path-ingress                       <none>   host.weiyigeek.top   10.10.107.214   80        26m

weiyigeek@ubuntu:~/K8s/Day7/demo2$ kubectl describe ingress ingress-nginx-auth
  # Name:             ingress-nginx-auth
  # Namespace:        default
  # Address:          10.10.107.214
  # Default backend:  nginx-ingress-v1-svc:80   10.244.1.125:80,10.244.1.126:80)
  # Rules:
  #   Host                Path  Backends
  #   ----                ----  --------
  #   auth.weiyigeek.top
  #                       /   nginx-ingress-v1-svc:80   10.244.1.125:80,10.244.1.126:80)
  # Annotations:          nginx.ingress.kubernetes.io/auth-realm: Authentication Required - WeiyiGeek
  #                       nginx.ingress.kubernetes.io/auth-secret: basic-auth
  #                       nginx.ingress.kubernetes.io/auth-type: basic
  # Events:
  #   Type    Reason  Age                  From                      Message
  #   ----    ------  ----                 ----                      -------
  #   Normal  Sync    22s (x3 over 2m36s)  nginx-ingress-controller  Scheduled for sync

# (5) 访问验证
~/K8s/Day7/demo2$ curl -I http://auth.weiyigeek.top:32268
  # HTTP/1.1 401 Unauthorized
  # Date: Sun, 15 Nov 2020 05:00:42 GMT
  # Content-Type: text/html
  # Content-Length: 172
  # Connection: keep-alive
  # WWW-Authenticate: Basic realm="Authentication Required - WeiyiGeek"
```

复制
![WeiyiGeek.WWW-Authenticate](https://ask.qcloudimg.com/http-save/1389665/6f766b2c8fd172942e170eea43b21a13.png)  
WeiyiGeek.WWW-Authenticate

*** ** * ** ***

##### 示例6.Ingress Redirect 域名重定向功能 {#23vip}

描述: 我们可以利用Ingress中的域名重定向`(Redirect)`注解来实现访问重定向。

```text
# 简单格式:
annotations:
  nginx.ingress.kubernetes.io/permanent-redirect: 'https://blog.weiyigeek.top'
```

复制

Ingress 规则配置清单示例:

```text
tee Ingress-Redirect.yaml <<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress 
metadata:
  name: ingress-redirect
  namespace: demo
  annotations:
    nginx.ingress.kubernetes.io/permanent-redirect: 'https://blog.weiyigeek.top'
spec:
  rules: 
    - host: redirect.weiyigeek.top
      http:
        paths:
        - path: /
          pathType: Prefix
          backend:
            service:
              name: front-svc
              port:
                number: 80
  defaultBackend:
    service:
      name: front-svc
      port:
        number: 80   
EOF
```

复制

部署资源清单以及查看

```text
# 创建 Ingress 资源对象
kubectl create -f Ingress-Redirect.yaml

# 查看Ingress控制器下面创建的应用信息以及详细信息
kubectl get ing -n demo ingress-redirect && kubectl describe ing -n demo ingress-redirect
  # NAME               CLASS    HOSTS                    ADDRESS   PORTS   AGE
  # ingress-redirect   <none>   redirect.weiyigeek.top             80      23s
  ....

# 验证重定向观察设置的虚拟主机的域名与未设置虚拟主机域名之间的不同
curl -v http://10.10.107.221 -H 'host: redirect.weiyigeek.top' -k  # 重定向到我们指定的域名
curl -v http://10.10.107.221 -H 'host: r.weiyigeek.top' -k         # 不存在的虚拟主机名称则访问默认 defaultBackend 服务。
```

复制
![WeiyiGeek.redirect](https://ask.qcloudimg.com/http-save/1389665/b01592f09fd36e45c46623c8328bbfa6.png)  
WeiyiGeek.redirect

##### 示例7.Ingress 黑白名单访问限制 {#2au2q}

描述: 某些内部系统可能只允许某些IP地址访问时，可利用 Ingress-nginx 的黑白名单，其实现的方式有如下两个。

* Annotations(注解) ：只对指定的ingress生效`nginx.ingress.kubernetes.io/whitelist-source-range`
* ConfigMap(配置文件) ：全局生效

Tips: 建议黑名单可以使用ConfigMap去配置，白名单建议使用Annotations去配置。

* Step 1.添加白名单的方式可以直接写`annotation`也可以配置在ConfigMap中。

```text
# 方式1.annotation方式
tee ingress-whilte-annotation.yaml <<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-whilte
  namespace: demo
  annotations:
    nginx.ingress.kubernetes.io/service-weight: '1'
    nginx.ingress.kubernetes.io/whitelist-source-range: 10.20.172.103,10.10.107.220
spec:
  rules: 
    - host: whilte.weiyigeek.top
      http:
        paths:
        - path: /
          pathType: ImplementationSpecific
          backend:
            service:
              name: front-svc
              port:
                number: 80
EOF

# 方式2.ConfigMap方式
tee ingress-whilte-ConfigMap.yaml <<'EOF'
apiVersion: v1
kind: ConfigMap
metadata:
  name: ingress-whilte
  namespace: demo
data:
  whitelist-source-range: 10.10.107.0/24
EOF


# 创建 Ingress 资源对象
$ kubectl create -f ingress-whilte-annotation.yaml

$ kubectl get ingress -n demo ingress-whilte
  # NAME             CLASS    HOSTS                  ADDRESS   PORTS   AGE
  # ingress-whilte   <none>   whilte.weiyigeek.top             80      75s

$ kubectl describe ingress -n demo ingress-whilte
  # Rules:
  #   Host                  Path  Backends
  #   ----                  ----  --------
  #   whilte.weiyigeek.top
  #                         /   front-svc:80 (192.168.109.91:80,192.168.109.92:80)
  # Annotations:            nginx.ingress.kubernetes.io/service-weight: 1
  #                         nginx.ingress.kubernetes.io/whitelist-source-range: 10.20.172.103,10.10.107.220

# 主机: 10.10.107.220 (拥有访问权限时正常返回数据)
10.10.107.220 $ curl http://10.10.107.221/ -H 'host: whilte.weiyigeek.top' -k -i
  # HTTP/1.1 200 OK
  # Date: Fri, 16 Jul 2021 07:47:00 GMT
  # Content-Type: text/html
  # Content-Length: 37
  # Connection: keep-alive
  # Last-Modified: Thu, 15 Jul 2021 12:24:55 GMT
  # ETag: "60f02917-25"
  # Accept-Ranges: bytes
# hostname-web-front-0-WeiyiGeek-Nginx

# 主机: 10.10.107.221 (无访问权限则显示403)
10.10.107.221 $ curl http://10.10.107.221/ -H 'host: whilte.weiyigeek.top' -k -i
  # HTTP/1.1 403 Forbidden
  # Date: Fri, 16 Jul 2021 07:47:54 GMT
  # Content-Type: text/html
  # Content-Length: 146
  # Connection: keep-alive
# <center><h1>403 Forbidden</h1></center>
```

复制

* Step 2.黑名单的方式也可以在annotations与ConfigMap中配置。 描述: 我们可以在注解中加入`nginx.ingress.kubernetes.io/server-snippet: |-`则可以加入自定义片段。

```text
# - Annotation
tee ingress-black-annotation.yaml <<'EOF'
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-black
  namespace: demo
  annotations:
    nginx.ingress.kubernetes.io/server-snippet: |-
      deny 10.10.107.220;
      deny 10.20.172.103;
      allow all;
spec:
  rules: 
    - host: black.weiyigeek.top
      http:
        paths:
        - path: /
          pathType: ImplementationSpecific
          backend:
            service:
              name: backend-svc
              port:
                number: 8080
status:
  loadBalancer: {}
EOF

# - ConfigMap
tee ingress-black-ConfigMap.yaml <<'EOF'
apiVersion: v1
kind: ConfigMap
metadata:
  name: ingress-black
  namespace: demo
data:
  whitelist-source-range: 10.20.172.0/24
  block-cidrs: 10.10.107.220
EOF

# - 创建 Ingress 对象
kubectl apply -f ingress-black-annotation.yaml

# - 查看与详细 Ingress 对象
kubectl get ing -n demo ingress-black && kubectl describe ing -n demo ingress-black
  # NAME            CLASS    HOSTS                 ADDRESS   PORTS   AGE
  # ingress-black   <none>   black.weiyigeek.top             80      20s

  # Rules:
  #   Host                 Path  Backends
  #   ----                 ----  --------
  #   black.weiyigeek.top  /     backend-svc:8080 (192.168.109.90:8080,192.168.109.93:8080)
  # Annotations:           nginx.ingress.kubernetes.io/server-snippet:
  #                          deny 10.10.107.220;
  #                          deny 10.20.172.103;
  #                          allow all;

# 验证 Ingress 代理的后端应用
# (1) 10.10.107.220 (黑名单无法访问)
curl http://10.10.107.221/ -H 'host: black.weiyigeek.top' -k -i
  # HTTP/1.1 403 Forbidden
  # ......................
  # <center><h1>403 Forbidden</h1></center>

# (2) 10.10.107.221 (未在黑名单之内则可以访问)  
curl http://10.10.107.221/ -H 'host: black.weiyigeek.top' -k -i
  # HTTP/1.1 200
  # Date: Fri, 16 Jul 2021 13:23:36 GMT
  # Content-Type: text/html
  # Content-Length: 40
  # Connection: keep-alive
  # Accept-Ranges: bytes
  # ETag: W/"40-1626351895000"
  # Last-Modified: Thu, 15 Jul 2021 12:24:55 GMT

  # hostname-web-front-0-WeiyiGeek-Nginx
```

复制  
原创声明：本文系作者授权腾讯云开发者社区发表，未经许可，不得转载。

如有侵权，请联系 [cloudcommunity@tencent.com](mailto:cloudcommunity@tencent.com) 删除。  
[负载均衡](/developer/tag/117)  
[nginx](/developer/tag/10315)  
[kubernetes](/developer/tag/10652)  
原创声明：本文系作者授权腾讯云开发者社区发表，未经许可，不得转载。

如有侵权，请联系 [cloudcommunity@tencent.com](mailto:cloudcommunity@tencent.com) 删除。  
[负载均衡](/developer/tag/117)  
[nginx](/developer/tag/10315)  
[kubernetes](/developer/tag/10652)  
[#Ingress](/developer/search/article-Ingress)  
[#Ingress-nginx](/developer/search/article-Ingress-nginx)  
[#WeiyIGeek](/developer/search/article-WeiyIGeek)
