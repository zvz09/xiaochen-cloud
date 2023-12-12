FROM openjdk:17-jdk-alpine
# author
MAINTAINER zvz09

# 挂载目录
VOLUME /home/xiaochen
# 创建目录
RUN mkdir -p /home/xiaochen
# 指定路径
WORKDIR /home/xiaochen
# 复制jar文件到路径
COPY xiaochen-*.jar /home/xiaochen/server.jar
# 启动网关服务
ENTRYPOINT ["java","-jar","server.jar"]