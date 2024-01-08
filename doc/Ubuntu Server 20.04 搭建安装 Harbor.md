## 1. 简介
   Harbor 是由 VMware 公司中国团队为企业用户设计的 Registry server 开源项目，包括了权限管理(RBAC)、LDAP、审计、管理界面、自我注册、HA 等企业必需的功能，同时针对中国用户的特点，设计镜像复制和中文支持等功能。
   作为一个企业级私有 Registry 服务器，Harbor 提供了更好的性能和安全。提升用户使用 Registry 构建和运行环境传输镜像的效率。Harbor 支持安装在多个 Registry 节点的镜像资源复制，镜像全部保存在私有 Registry 中， 确保数据和知识产权在公司内部网络中管控。另外，Harbor 也提供了高级的安全特性，诸如用户管理，访问控制和活动审计等。
    基于角色的访问控制 - 用户与 Docker 镜像仓库通过“项目”进行组织管理，一个用户可以对多个镜像仓库在同一命名空间（project）里有不同的权限。
    镜像复制 - 镜像可以在多个 Registry 实例中复制（同步）。尤其适合于负载均衡，高可用，混合云和多云的场景。
    图形化用户界面 - 用户可以通过浏览器来浏览，检索当前 Docker 镜像仓库，管理项目和命名空间。
    AD/LDAP 支持 - Harbor 可以集成企业内部已有的 AD/LDAP，用于鉴权认证管理。
    审计管理 - 所有针对镜像仓库的操作都可以被记录追溯，用于审计管理。
    国际化 - 已拥有英文、中文、德文、日文和俄文的本地化版本。更多的语言将会添加进来。
    RESTful API - RESTful API 提供给管理员对于 Harbor 更多的操控, 使得与其它管理软件集成变得更容易。
    部署简单 - 提供在线和离线两种安装工具， 也可以安装到 vSphere 平台(OVA 方式)虚拟设备

## 2. 环境要求
   Docker  17.06.0+
    Docker Compose 1.18.0+
    openssl 最新版

## 3. 安装
### 3.1 安装 Docker
```shell
#安装证书
curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
#设置阿里云镜像源
sudo add-apt-repository "deb [arch=amd64] https://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"

sudo apt update
sudo apt install -y docker.io
```


### 修改镜像
```shell
mkdir -p /etc/docker
cat << EOF > /etc/docker/daemon.json                                                                                                                                                                                                    
{
"registry-mirrors": ["https://hfd6rsvx.mirror.aliyuncs.com","http://f1361db2.m.daocloud.io","http://hub-mirror.c.163.com","https://registry.docker-cn.com"]
}

EOF
mkdir -p /etc/systemd/system/docker.service.d
systemctl daemon-reload
systemctl restart docker && systemctl enable docker
```

### 3.2 安装 Docker Compose
官方网站：https://github.com/docker/compose/releases

打开 GitHub 官网，找到对应版本下载

Linux：https://github.com/docker/compose/releases/download/1.29.2/docker-compose-Linux-x86_64

Window：https://github.com/docker/compose/releases/download/1.29.2/docker-compose-Windows-x86_64.exe
自动执行命令
```shell
$ curl -L https://github.com/docker/compose/releases/download/1.29.2/run.sh > /usr/local/bin/docker-compose
```
手动安装的话，将下载后的文件放到 /usr/local/bin 目录下，并添加执行权限
```shell
sudo mv docker-compose-Linux-x86_64 /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
# 查看版本
docker-compose -version
```

### 3.3 安装 Harbor
官方网站：https://github.com/goharbor/harbor/releases

下载最新离线 l 版本：
```shell
$ wget -c https://github.com/goharbor/harbor/releases/download/v2.3.2/harbor-offline-installer-v2.3.2.tgz
```

解压安装到你要安装的目录
```shell
$ tar xvf harbor-offline-installer-v2.3.2.tgz  -C /home/ubuntu/softwares/harbor && cd /home/ubuntu/softwares/harbor/
```

修改 harbor.yml 配置文件
```shell
$ cp harbor.yml.tmpl harbor.yml && nano harbor.yml
# 设置主机IP或域名.
hostname: hub.loamen.com

# http related config
http:
# 修改http端口，会自动跳转到https端口
port: 81

# https related config
https:
# https port for harbor, default is 443
port: 443
# 设置SSL证书路径
certificate: /data/cert/loamen.com.crt
private_key: /data/cert/loamen.com.key


# 设置admin密码.
harbor_admin_password: Harbor12345

database:
# 设置数据库密码.
password: Harbor12345

# 设置数据保存路径
data_volume: /home/ubuntu/softwares/harbor/data
```

### 3.4 安装数字证书
这里介绍按照官方方法生成和安装自建证书，如果已有公网证书可不需要生成证书。默认情况下，Harbor 不附带证书。无需安全即可部署 Harbor，可以通过 HTTP 连接。如果直接采用 http 访问，请将harbor.yml配置文件的 https 相关内容全部注释掉。
可以修改hosts或者 DNS 服务器（如：bind9）将域名DNS 记录指向运行 Harbor 的主机。假设域名是：loamen.com
1. 临时修改 DNS
```shell
$ sudo vi /etc/resolv.conf

nameserver 8.8.8.8
```
2. 永久修改
```shell
$ sudo vi /etc/systemd/resolved.conf

[Resolve]
DNS=223.5.5.5
DNS=8.8.8.8
#FallbackDNS=
#Domains=
#LLMNR=no
#MulticastDNS=no
#DNSSEC=no
#Cache=yes
#DNSStubListener=yes

# 修改完成后，重启服务。

$ systemctl restart systemd-resolved.service

# 查看状态
$ systemd-resolve --status

Global
DNS Servers: 223.5.5.5
8.8.8.8
```

#### 3.4.1 生成授权证书
生成 CA 证书私钥
```shell
$ openssl genrsa -out ca.key 4096
```
生成 CA 证书
调整-subj选项中的值以反映您的组织。如果使用FQDN连接Harbor主机，则必须将其指定为通用名称（CN）属性。
```shell
openssl req -x509 -new -nodes -sha512 -days 3650 \
-subj "/C=CN/ST=Beijing/L=Beijing/O=Loamen/OU=Personal/CN=loamen.com" \
-key ca.key \
-out ca.crt
```

#### 3.4.2 生成服务器证书
通常证书包含一个.crt文件和一个.key文件，例如loamen.com.crt和loamen.com.key。
生成服务器私钥
```shell
$ openssl genrsa -out loamen.com.key 4096
```
生成证书签名请求（CSR），修改-subj选项中的组织。
```shell
openssl req -sha512 -new \
-subj "/C=CN/ST=Beijing/L=Beijing/O=Loamen/OU=Personal/CN=loamen.com" \
-key loamen.com.key \
-out loamen.com.csr
```
生成 x509 v3 扩展文件
无论使用 FQDN 还是 IP 地址连接到 Harbor 主机，都必须创建此文件，以便可以为您的 Harbor 主机生成符合主题备用名称（SAN）和 x509 v3 的证书扩展要求。修改 DNS 中域名。
```shell
cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=loamen.com
DNS.2=loamen
DNS.3=hostname
EOF
```

使用 v3.ext 文件生成 Harbor 服务器证书
将 CRS 和 CRT 单文件名替换为 Harbor 主机名称或域名
```shell
openssl x509 -req -sha512 -days 3650 \
-extfile v3.ext \
-CA ca.crt -CAkey ca.key -CAcreateserial \
-in loamen.com.csr \
-out loamen.com.crt
```

#### 3.4.3 应用证书到 Harbor 和 Docker
1. 将服务器证书和密钥复制到 Harbor 主机上的 certficates 文件夹中。
```shell
mkdir -p /data/cert
cp loamen.com.crt /data/cert/
cp loamen.com.key /data/cert/
```
修改上面步骤 harbor.yml 配置文件中对应的路径，确保其正确性。
```shell
# https related config
https:
# https port for harbor, default is 443
port: 443
# 设置SSL证书路径
certificate: /data/cert/loamen.com.crt
private_key: /data/cert/loamen.com.key
```
2. 将证书文件.crt转换为docker的.cert文件
```shell
$ openssl x509 -inform PEM -in loamen.com.crt -out loamen.com.cert
```

3. 将服务器证书，密钥和 CA 文件复制到 Harbor 主机上的 Docker 证书文件夹中。如果目录不存在，则需要创建相应的文件夹。
```shell
mkdir -p /etc/docker/certs.d
mkdir -p /etc/docker/certs.d/loamen.com
cp loamen.com.cert /etc/docker/certs.d/loamen.com/
cp loamen.com.key /etc/docker/certs.d/loamen.com/
cp ca.crt /etc/docker/certs.d/loamen.com/
```
如果想将默认nginx端口 443 映射到其他端口，请创建文件夹/etc/docker/certs.d/loamen.com:port或/etc/docker/certs.d/harbor_IP:port。
以下为使用自定义证书配置的示例：
```shell
/etc/docker/certs.d/
└── loamen.com:port
├── loamen.com.cert  <-- Server certificate signed by CA
├── loamen.com.key   <-- Server key signed by CA
└── ca.crt               <-- Certificate authority that signed the registry certificate

```

4. 重启 docker
```shell
$ systemctl restart docker
```
5. 数字证书安装脚本如下
```shell
#!/bin/bash
cd certs

echo "1.成根证书CA私钥"
openssl genrsa -out ca.key 4096


echo "2.生成根证书CA"
openssl req -x509 -new -nodes -sha512 -days 3650 \
-subj "/C=CN/ST=Beijing/L=Beijing/O=Loamen/OU=Personal/CN=loamen.com" \
-key ca.key \
-out ca.crt


echo "3.生成服务器私钥"
openssl genrsa -out loamen.com.key 4096

echo "4.生成证书签名请求（CSR）"

openssl req -sha512 -new \
-subj "/C=CN/ST=Beijing/L=Beijing/O=Loamen/OU=Personal/CN=loamen.com" \
-key loamen.com.key \
-out loamen.com.csr

echo "5.生成x509 v3扩展文件"

cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=loamen.com
DNS.2=hub.loamen.com
DNS.3=loamen-master
EOF

echo "6.使用v3.ext文件生成Harbor服务器证书"
openssl x509 -req -sha512 -days 3650 \
-extfile v3.ext \
-CA ca.crt -CAkey ca.key -CAcreateserial \
-in loamen.com.csr \
-out loamen.com.crt

echo "7.复制到证书文件夹"
mkdir -p /data/cert
cp loamen.com.crt /data/cert/
cp loamen.com.key /data/cert/

echo "8.转换docer证书格式.cert文件"
openssl x509 -inform PEM -in loamen.com.crt -out loamen.com.cert

echo "9.复制证书到Harbor的Docker证书目录"
mkdir -p /etc/docker/certs.d
mkdir -p /etc/docker/certs.d/loamen.com
cp loamen.com.cert /etc/docker/certs.d/loamen.com/
cp loamen.com.key /etc/docker/certs.d/loamen.com/
cp ca.crt /etc/docker/certs.d/loamen.com/

echo "10.重启docker"
systemctl restart docker
```

### 3.5 启动 Harbor
1. 首次可以直接运行启动脚本
```shell
$ sudo ./install.sh
```
如需重新配置和启动执行脚本
```shell
#加载harbor配置
sudo ./prepare
#安装harbor
docker-compose stop 停止
docker-compose up -d 启动
```

2. 验证 HTTPS 连接
在为Harbor设置 HTTPS 之后，可以通过执行以下步骤来验证 HTTPS 连接。
•打开浏览器，然后输入https://loamen.com。应该显示 Harbor 界面。
某些浏览器可能会显示警告，指出证书颁发机构（CA）未知。使用不是来自受信任的第三方 CA 的自签名 CA 时，会发生这种情况。可以将 CA 导入浏览器以删除警告。
在运行Docker daemon程序的机器上，检查/etc/docker/daemon.json文件以确保-insecure-registry为https://loamen.com。
```shell
{
"registry-mirrors": ["https://hfd6rsvx.mirror.aliyuncs.com","http://f1361db2.m.daocloud.io","http://hub-mirror.c.163.com","https://registry.docker-cn.com"],
"insecure-registries": ["loamen.com"]
}
```
docker 登录，如果你将 443 端口映射到其他端口请在域名后加上端口号，如“docker login loamen.com:Port”，这时候系统会要求提供账号和密码，输入 admin 的账号密码或者在 Harbor 后台新建一个账号来登录。
```shell
$ docker login loamen.com
```

### 3.6 推送镜像到私服
登录成功后，使用如下命令将镜像推送到私服。
```shell

$ docker tag SOURCE_IMAGE[:TAG] hub.loamen.com/res/REPOSITORY[:TAG]
$ docker push hub.loamen.com/res/REPOSITORY[:TAG]

The push refers to repository [hub.loamen.com/res/phusion/baseimage]
3b6418367581: Pushed
4e9f53ea2e18: Pushed
facb372d15a2: Pushed
2f140462f3bc: Pushed
63c99163f472: Pushed
ccdbb80308cc: Pushed
focal-1.0.0: digest: sha256:fcb33a39cfd119c3334eecf52616c697511782f946b58b3fea0348cf3b4ebe6a size: 1571

```

## 4. 在操作系统级别信任证书
可能还需要在操作系统级别信任证书。有关更多信息，请参见Harbor docs | Troubleshooting Harbor Installation (goharbor.io)
1. 如果使用来自证书发行者的中间证书，请将该中间证书与自己的证书合并以创建证书捆绑包。运行以下命令。
```shell
$ cat intermediate-certificate.pem >> loamen.com.crt
```
2. 在Docker daemon的操作系统上运行时，可能需要在操作系统级别信任证书。运行以下命令。

- Ubuntu
```shell
$ sudo cp loamen.com.crt /usr/local/share/ca-certificates/loamen.com.crt && sudo  update-ca-certificates
```
- Red Hat (CentOS etc)
```shell
$ sudo cp loamen.com.crt /etc/pki/ca-trust/source/anchors/loamen.com.crt && sudo update-ca-trust
```

### 5. 问题
当使用 docker login 的时候可能会出现如下错误：
Error response from daemon: Get https://hub.loamen.com/v2: x509: certificate is valid for aaa.loamen.com, not hub.loamen.com
1. 请确保域名指向正确，使用 dig 命令查看
```shell
$ dig hub.loamen.com
```

2. 查看/etc/docker/daemon.json是否添加了"insecure-registries"参数，且值域为你的域名；
```shell
$ cat /etc/docker/daemon.json
```
3. 执行 4.2 步操作，将证书添加到 docker 节点服务器的信任列表。