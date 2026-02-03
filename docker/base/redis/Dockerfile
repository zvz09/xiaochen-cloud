# 基础镜像
FROM redis:8.4.0-bookworm
# author
MAINTAINER zvz09

# 挂载目录
VOLUME /home/zvz09/redis
# 创建目录
RUN mkdir -p /home/zvz09/redis
# 指定路径
WORKDIR /home/zvz09/redis
# 复制conf文件到路径
COPY ./conf/redis.conf /home/zvz09/redis/redis.conf
