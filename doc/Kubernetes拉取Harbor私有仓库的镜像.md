## 1. 前言
   k8s在默认情况下，只能拉取harbor镜像仓库的公有镜像，如果拉取私有仓库镜像，则是会报 ErrImagePull 和 ImagePullBackOff 的错误：

```shell
Events:
Type     Reason     Age   From               Message
  ----     ------     ----  ----               -------
Normal   Scheduled  13s   default-scheduler  Successfully assigned learn/web-7c9c86c7d-hkh79 to k8s-master1
Normal   Pulling    12s   kubelet            Pulling image "192.168.18.100:80/lnmp/nginx:v2"
Warning  Failed     12s   kubelet            Failed to pull image "192.168.18.100:80/lnmp/nginx:v2": rpc error: code = Unknown desc = Error response from daemon: pull access denied for 192.168.18.100:80/lnmp/nginx, repository does not exist or may require 'docker login': denied: requested access to the resource is denied
Warning  Failed     12s   kubelet            Error: ErrImagePull
Normal   BackOff    11s   kubelet            Back-off pulling image "192.168.18.100:80/lnmp/nginx:v2"
Warning  Failed     11s   kubelet            Error: ImagePullBackOff
```
解决办法：
在harbor仓库中把镜像的项目设置为公开。
创建认证登录秘钥，拉取镜像时带上该秘钥。
## 2. k8s使用秘钥拉取harbor私有镜像
### 2.1 登录Docker
在服务器上，要想拉取私有镜像必须先在镜像仓库上进行身份验证，即登录harbor仓库：
```shell
[root@k8s-master ~]# docker login 192.168.18.100:80
Authenticating with existing credentials...
WARNING! Your password will be stored unencrypted in /root/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
```
我这里已经登录过了，所以会更新保存有授权令牌的 config.json 文件，这个就是登录Harbor的秘钥文件。从提示中可以看到是在 /root/.docker/config.json 中。这个文件内容如下：

```shell
[root@k8s-master ~]# cat /root/.docker/config.json
{
"auths": {
"192.168.18.100:80": {
"auth": "YWRtaW46SGFyYm9yMTIzNDU="
}
},
"HttpHeaders": {
"User-Agent": "Docker-Client/19.03.9 (linux)"
}
}
```
### 2.2 对秘钥文件进行base64加密
```shell
[root@k8s-master ~]# cat ~/.docker/config.json |base64 -w 0
ewoJImF1dGhzIjogewoJCSIxOTIuMTY4LjE4LjEwMDo4MCI6IHsKCQkJImF1dGgiOiAiWVdSdGFXNDZTR0Z5WW05eU1USXpORFU9IgoJCX0KCX0sCgkiSHR0cEhlYWRlcnMiOiB7CgkJIlVzZXItQWdlbnQiOiAiRG9ja2VyLUNsaWVudC8xOS4wMy45IChsaW51eCkiCgl9Cn0=
```
### 2.3 k8s创建secret秘钥
创建 docker-secret.yaml 文件

```yaml
---
apiVersion: v1
data:
  .dockerconfigjson: >-
    ewoJImF1dGhzIjogewoJCSJoYXJib3IueGlhb2NoZW4uY29tIjogewoJCQkiYXV0aCI6ICJZV1J0YVc0Nk5YUm5ZbWgxT0E9PSIKCQl9Cgl9Cn0=
kind: Secret
metadata:
  name: docker-login
  namespace: default
type: kubernetes.io/dockerconfigjson


```
创建secret：
```shell
[root@k8s-master ~]# kubectl create -f docker-secret.yaml
```
查看secret：

```shell
[root@k8s-master ~]# kubectl get secret
NAME                  TYPE                                  DATA   AGE
default-token-dbmds   kubernetes.io/service-account-token   3      28d
docker-login          kubernetes.io/dockerconfigjson        1      5h53m
```
### 2.4 创建应用，拉取Harbor私有仓库镜像
文件 nginx.yaml 内容如下：

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web02
  labels:
    app: web02
  namespace: learn
spec:
  replicas: 1
  selector:
    matchLabels:
      app: web02
  template:
    metadata:
      labels:
        app: web02
    spec:
      containers:
        - image: 192.168.18.100:80/lnmp/nginx:v2
          imagePullPolicy: IfNotPresent
          name: nginx
      imagePullSecrets:
        - name: docker-login
      dnsPolicy: ClusterFirst
      restartPolicy: Always
```
可以看到镜像成功拉取，Pod正常运行了：

```shell
[root@k8s-master ~]# kubectl -n learn get pods
NAME                     READY   STATUS    RESTARTS   AGE
web02-76969fc49b-j6fb4   1/1     Running   0          41s
[root@k8s-master ~]# kubectl -n learn get deployment
NAME    READY   UP-TO-DATE   AVAILABLE   AGE
web02   1/1     1            1           49s
```
上面yaml文件中，拉取镜像下面携带秘钥的字段是 imagePullSecrets ，值填的是上面创建secret的名字。